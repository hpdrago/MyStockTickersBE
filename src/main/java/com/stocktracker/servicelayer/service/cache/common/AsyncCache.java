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
import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.NOT_FOUND;
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
 * @param <CK> - The key type to the cache.
 * @param <CD> - The type of cached data.
 * @param <ASK> - The async key used to fetch the external data.
 * @param <E> - The cache entry type which is a subclass of AsyncCacheEntry<CD>.
 * @param <X> - The interface definition of the class that will be performing synchronous and asynchronous work to
 *              get the information of type CD.
 */
public abstract class AsyncCache<CK extends Serializable,
                                 CD,
                                ASK,
                                  E extends AsyncCacheEntry<CK,CD,ASK>,
                                  X extends AsyncCacheServiceExecutor<CK,CD,ASK>>
    extends BaseService
    implements MyLogger
{
    private Map<CK,E> cacheMap = Collections.synchronizedMap( new HashMap<>() );

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
    public E getCacheEntry( final CK key )
    {
        return this.cacheMap
                   .get( key );
    }

    /**
     * Obtains the information from the cache.
     * @param cacheKey The key to the cache.
     * @param asyncKey The key to search for the information.
     * @return If the information is in the cache and it is not stale, the information will be returned within
     *         the cache entry <E> container.
     *
     *         If the information is not in the cache, then the information will be
     *         retrieved immediately.
     */
    public E synchronousGet( @NotNull final CK cacheKey,
                             @NotNull final ASK asyncKey )
    {
        final String methodName = "synchronousGet";
        logMethodBegin( methodName, cacheKey, asyncKey );
        E returnCacheEntry = null;
        final E cacheEntry = this.cacheMap
                                 .get( cacheKey );
        /*
         * If null, the item is not in the cache
         */
        if ( cacheEntry == null )
        {
            logTrace( methodName, "{0} is not in the cache", cacheKey );
            returnCacheEntry = this.createCacheEntry();
            returnCacheEntry.setCacheKey( cacheKey );
            /*
             * Store an "empty" cache entry for now
             */
            this.cacheMap
                .put( cacheKey, returnCacheEntry );
            logTrace( methodName, "Created cache entry: {0}", cacheEntry );
            this.synchronousFetch( cacheKey, asyncKey, returnCacheEntry );
        }
        else
        {
            if ( cacheEntry.isStale() )
            {
                logTrace( methodName, "{0} is STALE", cacheKey );
                switch ( cacheEntry.getFetchState() )
                {
                    case FETCHING:
                        logTrace( methodName, "{0} already fetching, blocking and waiting now...", cacheKey );
                        returnCacheEntry = cacheEntry;
                        cacheEntry.getCachedDataSyncProcessor()
                                  .subscribe();
                        break;

                    case NOT_FETCHING:
                        logTrace( methodName, "{0} fetching now", cacheKey );
                        this.synchronousFetch( cacheKey, asyncKey, cacheEntry );
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
     * @param cacheKey The key to the cache.
     * @param asyncKey The key to search for the information.
     * @return If the information is in the cache and it is not stale, the information will be returned within
     *         the cache entry <E> container.
     *
     *         If the information is not in the cache, the new cache entry will be
     *         returned with the status information and RxJava subject and the information will be retrieved by another
     *         thread.
     */
    public E asynchronousGet( @NotNull final CK cacheKey,
                              @NotNull final ASK asyncKey )
        throws AsyncCacheDataRequestException
    {
        final String methodName = "asynchronousGet";
        logMethodBegin( methodName, cacheKey, asyncKey );
        E cacheEntry = this.cacheMap
                           .get( cacheKey );
        /*
         * If null, the item is not in the cache
         */
        if ( cacheEntry == null )
        {
            logTrace( methodName, "{0} is not in the map", cacheKey );
            cacheEntry = this.createCacheEntry();
            cacheEntry.setCacheKey( cacheKey );
            cacheEntry.setFetchState( FETCHING );
            cacheEntry.setCacheState( STALE );
            /*
             * Store an "empty" cache entry for now
             */
            this.cacheMap
                .put( cacheKey, cacheEntry );
            logTrace( methodName, "Created cache entry: {0}", cacheEntry );
            this.asynchronousFetch( cacheKey, asyncKey, cacheEntry );
        }
        else
        {
            logTrace( methodName, "cacheEntry: {0}", cacheEntry );
            if ( cacheEntry.isStale() )
            {
                cacheEntry.setCacheState( STALE );
                logTrace( methodName, "{0} is in the map, but is STALE", cacheKey );
                switch ( cacheEntry.getFetchState() )
                {
                    case FETCHING:
                        logTrace( methodName, "{0} is currently being fetched", asyncKey );
                        break;

                    case NOT_FETCHING:
                        logTrace( methodName, "{0} is NOT currently being fetched, fetching asynchronously",
                                  asyncKey );
                        cacheEntry.setFetchState( FETCHING );
                        this.asynchronousFetch( cacheKey, asyncKey, cacheEntry );
                        break;
                }
            }
        }
        logMethodEnd( methodName, cacheEntry );
        return cacheEntry;
    }

    /**
     * Fetch the data synchronously.
     * @param cacheKey
     * @param asyncKey
     * @param cacheEntry
     */
    protected void synchronousFetch( final CK cacheKey,
                                     final ASK asyncKey,
                                     final E cacheEntry )
    {
        final String methodName = "getASyncData";
        logMethodBegin( methodName, cacheKey, asyncKey, cacheEntry );
        CD cacheData = null;
        cacheEntry.setCacheKey( cacheKey );
        try
        {
            cacheEntry.setFetchState( FETCHING );
            try
            {
                cacheData = this.getExecutor()
                                .getASyncData( cacheKey, asyncKey );
                logDebug( methodName, "information: {0}", cacheData );
                cacheEntry.setCachedData( cacheKey, asyncKey, cacheData );
                cacheEntry.setCacheState( CURRENT );
            }
            catch( AsyncCacheDataNotFoundException asyncCacheDataNotFoundException )
            {
                logDebug( methodName, "NOT_FOUND" );
                cacheEntry.setCacheState( NOT_FOUND );
            }
            catch( Throwable e )
            {
                cacheEntry.setCacheState( FAILURE );
                cacheEntry.setFetchThrowable( e );
                logDebug( methodName, "FAILURE: " + e.getMessage() );
            }
        }
        finally
        {
            cacheEntry.setFetchState( NOT_FETCHING );
        }
        logTrace( methodName, "cacheEntry: {0}", cacheEntry );
        logMethodEnd( methodName, cacheData );
    }

    /**
     * Fetches the information asynchronously
     * @param cacheKey
     * @param asyncKey
     * @param cacheEntry
     * @return
     */
    protected void asynchronousFetch( final CK cacheKey,
                                      final ASK asyncKey,
                                      final E cacheEntry )
        throws AsyncCacheDataRequestException
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, cacheKey, asyncKey, cacheEntry );
        cacheEntry.setFetchState( FETCHING );
        /*
         * Call the injected executor to perform the work. This will be done on a separate thread and will return
         * immediately.
         */
        this.getExecutor()
            .asynchronousFetch( cacheKey, asyncKey, cacheEntry.getCachedDataSyncProcessor() );
        logTrace( methodName, "Subscribing to subject for {0}", cacheKey );
        /*
         * Setup the handling to handle the completed request.
         */
        cacheEntry.getCachedDataSyncProcessor()
                  .share()
                  .doOnError( throwable ->
                  {
                      cacheEntry.setCachedData( cacheKey, asyncKey, null );
                      /*
                       * Check for a not found exception first, that's a normal scenario
                       */
                      if ( throwable instanceof AsyncCacheDataNotFoundException )
                      {
                          cacheEntry.setCacheState( NOT_FOUND );
                      }
                      else
                      {
                          cacheEntry.setFetchThrowable( throwable );
                          cacheEntry.setCacheState( FAILURE );
                          logError( methodName, String.format( "subscribe.onError for search Key: ", cacheKey ), throwable );
                          cacheEntry.getCachedDataSyncProcessor().onError( throwable );
                      }
                      cacheEntry.setFetchState( NOT_FETCHING );
                      logTrace( methodName, "cacheEntry: {0}", cacheEntry );
                  })
                  .subscribe( cacheData ->
                  {
                      logTrace( methodName, "subscribe.onNext fetch of key {0} value {1}",
                                cacheKey, cacheData );
                      cacheEntry.setCachedData( cacheKey, asyncKey, cacheData );
                      cacheEntry.setFetchState( NOT_FETCHING );
                      cacheEntry.setCacheState( CURRENT );
                      logTrace( methodName, "cacheEntry: {0}", cacheEntry );
                  });
        logTrace( methodName, "cacheEntry: {0}", cacheEntry );
        logMethodEnd( methodName );
    }

    /**
     * Adds a cache entry to the cache.
     * @param key
     * @param cacheEntry
     */
    protected void addCacheEntry( final CK key, final E cacheEntry )
    {
        final String methodName = "createCacheEntry";
        logMethodBegin( methodName, key, cacheEntry );
        this.cacheMap
            .put( key, cacheEntry );
        logMethodEnd( methodName );
    }

    /**
     * Creates a new cache entry and adds it to the cache.
     * @param cacheKey
     * @param asyncKey
     * @param cachedData
     * @param cacheState
     */
    public void createCacheEntry( final CK cacheKey,
                                  final CD cachedData,
                                  final ASK asyncKey,
                                  final AsyncCacheEntryState cacheState )
    {
        final String methodName = "createCacheEntry";
        logMethodBegin( methodName, cacheKey, cacheState );
        Objects.requireNonNull( cacheKey, "key argument cannot be null" );
        Objects.requireNonNull( cacheState, "cacheState argument cannot be null" );
        E cacheEntry = this.createCacheEntry();
        cacheEntry.setFetchState( NOT_FETCHING );
        cacheEntry.setCacheState( cacheState );
        cacheEntry.setCachedData( cacheKey, asyncKey, cachedData );
        /*
         * Store an "empty" cache entry for now
         */
        this.cacheMap
            .put( cacheKey, cacheEntry );
        logMethodEnd( methodName, cacheKey );
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
