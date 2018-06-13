package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.service.BaseService;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.CURRENT;
import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.FAILURE;
import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.STALE;
import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheFetchState.FETCHING;
import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheFetchState.NOT_FETCHING;

/**
 * This is a generic cache class that provides for the synchronous and asynchronous fetching of information from 
 * IEXTrading or other external parties.  It encapsulates the cache of information and the current status of the cache 
 * items as to whether the information is current or stale or if the information is currently being fetch or not.
 * 
 * Asynchronous callers can make a request for information and if the that information is current, it will be returned
 * to the caller.  If the information is not current, the caller will be returned the stale information (or empty if 
 * there currently is no information for that entity), with an indicator that the information is being fetched.  The
 * result will be passed back to the client which will then make a subsequent call to get the information.  This allows
 * for a quick query of information by the client with results returned quickly even if the results are stale. 
 *
 * @param <T> - The type of cached data to obtain from the third party.
 * @param <K> - The key type to the cache -- this is key used to query the information from the third party.
 * @param <E> - The cache entry type which is a subclass of AsyncCacheEntry<T>.
 * @param <X> - The interface definition of the class that will be performing synchronous and asynchronous work to
 *              get the information of type T.
 */
public abstract class AsyncCache<K extends Serializable,
                                 T,
                                 E extends AsyncCacheEntry<T>,
                                 X extends AsyncCacheServiceExecutor<K,T>>
    extends BaseService
    implements MyLogger
{
    private Map<K,E> cacheMap = Collections.synchronizedMap( new HashMap<>() );

    /**
     * Constructor.
     */
    protected AsyncCache()
    {
    }

    /**
     * Get a entry from the cache.
     * @param key
     * @return
     */
    public E getCacheEntry( final K key )
    {
        return this.cacheMap
                   .get( key );
    }

    /**
     * Obtains the information from the cache.
     * @param searchKey The key to search for the information.
     * @return If the information is in the cache and it is not stale, the information will be returned within
     *         the cache entry <E> container.
     *
     *         If the information is not in the cache, then the information will be
     *         retrieved immediately.
     */
    public E synchronousGet( @NotNull final K searchKey )
    {
        final String methodName = "synchronousGet";
        logMethodBegin( methodName, searchKey );
        E returnCacheEntry = null;
        final E cacheEntry = this.cacheMap
                                 .get( searchKey );
        /*
         * If null, the item is not in the cache
         */
        if ( cacheEntry == null )
        {
            logTrace( methodName, "{0} is not in the cache", searchKey );
            returnCacheEntry = this.createCacheEntry();
            returnCacheEntry.setFetchState( FETCHING );
            returnCacheEntry.setCacheState( STALE );
            /*
             * Store an "empty" cache entry for now
             */
            this.cacheMap
                .put( searchKey, returnCacheEntry );
            logTrace( methodName, "Created cache entry: {0}", cacheEntry );
            this.synchronousFetch( searchKey, returnCacheEntry );
        }
        else
        {
            if ( cacheEntry.isStale() )
            {
                logTrace( methodName, "{0} is STALE", searchKey );
                switch ( cacheEntry.getFetchState() )
                {
                    case FETCHING:
                        logTrace( methodName, "{0} already fetching", searchKey );
                        returnCacheEntry = cacheEntry;
                        break;

                    case NOT_FETCHING:
                        logTrace( methodName, "{0} fetching now", searchKey );
                        this.synchronousFetch( searchKey, cacheEntry );
                        returnCacheEntry = cacheEntry;
                        break;
                }
            }
            else
            {
                returnCacheEntry = cacheEntry;
            }
        }
        logTrace( methodName, "cacheEntry: {0}", returnCacheEntry );
        logMethodEnd( methodName, returnCacheEntry );
        return returnCacheEntry;
    }

    /**
     * Obtains the information from the cache asynchronously.
     * @param searchKey The key to search for the information.
     * @return If the information is in the cache and it is not stale, the information will be returned within
     *         the cache entry <E> container.
     *
     *         If the information is not in the cache, the new cache entry will be
     *         returned with the status information and RxJava subject and the information will be retrieved by another
     *         thread.
     */
    public E asynchronousGet( @NotNull final K searchKey )
    {
        final String methodName = "asynchronousGet";
        logMethodBegin( methodName, searchKey );
        E cacheEntry = this.cacheMap
                           .get( searchKey );
        /*
         * If null, the item is not in the cache
         */
        if ( cacheEntry == null )
        {
            logTrace( methodName, "{0} is not in the map", searchKey );
            cacheEntry = this.createCacheEntry();
            cacheEntry.setFetchState( FETCHING );
            cacheEntry.setCacheState( STALE );
            /*
             * Store an "empty" cache entry for now
             */
            this.cacheMap
                .put( searchKey, cacheEntry );
            logTrace( methodName, "Created cache entry: {0}", cacheEntry );
            this.asynchronousFetch( searchKey, cacheEntry );
        }
        else
        {
            logTrace( methodName, "cacheEntry: {0}", cacheEntry );
            if ( cacheEntry.isStale() )
            {
                cacheEntry.setCacheState( STALE );
                logTrace( methodName, "{0} is in the map, but is STALE", searchKey );
                switch ( cacheEntry.getFetchState() )
                {
                    case FETCHING:
                        logTrace( methodName, "{0} is currently being fetched", searchKey );
                        break;

                    case NOT_FETCHING:
                        logTrace( methodName, "{0} is NOT currently being fetched, fetching asynchronously", searchKey );
                        cacheEntry.setFetchState( FETCHING );
                        this.asynchronousFetch( searchKey, cacheEntry );
                        break;
                }
            }
            else
            {
                cacheEntry.setCacheState( CURRENT );
            }
        }
        logMethodEnd( methodName, cacheEntry );
        return cacheEntry;
    }

    /**
     * Fetch the data synchronously.
     * @param searchKey
     * @param cacheEntry
     */
    protected void synchronousFetch( final K searchKey, final E cacheEntry )
    {
        final String methodName = "synchronousFetch";
        logMethodBegin( methodName, searchKey, cacheEntry );
        T information = null;
        try
        {
            cacheEntry.setFetchState( FETCHING );
            try
            {
                information = this.getExecutor()
                                  .synchronousFetch( searchKey );
                cacheEntry.setCachedData( information );
                cacheEntry.setCacheState( CURRENT );
            }
            catch( AsyncCacheDataNotFoundException asyncCacheDataNotFoundException )
            {
                cacheEntry.setCacheState( FAILURE );
                cacheEntry.setFetchThrowable( asyncCacheDataNotFoundException );
            }
        }
        finally
        {
            cacheEntry.setFetchState( NOT_FETCHING );
        }
        logTrace( methodName, "cacheEntry: {0}", cacheEntry );
        logMethodEnd( methodName, information );
    }

    /**
     * Fetches the information asynchronously
     * @param searchKey
     * @param cacheEntry
     * @return
     */
    protected void asynchronousFetch( final K searchKey, final E cacheEntry )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, searchKey, cacheEntry );
        cacheEntry.setFetchState( FETCHING );
        /*
         * Call the injected executor to perform the work. This will be done on a separate thread and will return
         * immediately.
         */
        this.getExecutor()
            .asynchronousFetch( searchKey, cacheEntry.getAsyncProcessor() );
        logTrace( methodName, "Subscribing to subject for {0}", searchKey );
        /*
         * Setup the handling to handle the completed request.
         */
        cacheEntry.getAsyncProcessor()
                  .share()
                  .doOnError( throwable ->
                  {
                      logError( methodName, String.format( "subscribe.onError for search Key: ", searchKey ), throwable );
                      cacheEntry.setFetchThrowable( throwable );
                      cacheEntry.setCachedData( null );
                      cacheEntry.setFetchState( NOT_FETCHING );
                      cacheEntry.getAsyncProcessor().onError( throwable );
                      logTrace( methodName, "cacheEntry: {0}", cacheEntry );
                  })
                  .subscribe( cacheData ->
                  {
                      logTrace( methodName, "subscribe.onNext fetch of key {0} value {1}",
                                searchKey, cacheData );
                      cacheEntry.setCachedData( cacheData );
                      cacheEntry.setFetchState( NOT_FETCHING );
                      cacheEntry.setCacheState( CURRENT );
                      logTrace( methodName, "cacheEntry: {0}", cacheEntry );
                  });
        logTrace( methodName, "cacheEntry: {0}", cacheEntry );
        logMethodEnd( methodName );
    }

    /**
     * Creates a new cache entry and adds it to the cache.
     * @param key
     * @param cachedData
     * @param cacheDataState
     */
    public void createCacheEntry( final K key, final T cachedData, final AsyncCacheEntryState cacheDataState )
    {
        final String methodName = "createCacheEntry";
        logMethodBegin( methodName, key, cacheDataState );
        E cacheEntry = this.createCacheEntry();
        cacheEntry.setFetchState( NOT_FETCHING );
        cacheEntry.setCacheState( cacheDataState );
        cacheEntry.setCachedData( cachedData );
        /*
         * Store an "empty" cache entry for now
         */
        this.cacheMap
            .put( key, cacheEntry );
        logMethodEnd( methodName, key );
    }

    /**
     * Creates a new cache entry instance.
     * @return
     */
    protected abstract E createCacheEntry();

    /**
     * Creates the service executor.
     * @return
     */
    protected abstract X getExecutor();

    /**
     * Identies whether to keep cached data or remove it after all subscribers have been sent the data.
     * @return
     */
    protected abstract AsyncCacheStrategy getCacheStrategy();

}
