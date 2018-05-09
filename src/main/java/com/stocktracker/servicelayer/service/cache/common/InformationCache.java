package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.common.MyLogger;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.stocktracker.servicelayer.service.cache.common.InformationCacheEntryState.CURRENT;
import static com.stocktracker.servicelayer.service.cache.common.InformationCacheEntryState.STALE;
import static com.stocktracker.servicelayer.service.cache.common.InformationCacheFetchState.FETCHING;
import static com.stocktracker.servicelayer.service.cache.common.InformationCacheFetchState.NOT_FETCHING;

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
 * @param <T> - The type of information to obtain from the third party.
 * @param <K> - The key type to the cache -- this is key used to query the information from the third party.
 * @param <E> - The cache entry type which is a subclass of InformationCacheEntry<T>.
 * @param <X> - The interface definition of the class that will be performing synchronous and asynchronous work to
 *              get the information of type T.
 */
public abstract class InformationCache<T,K,E extends InformationCacheEntry<T>,
                                       X extends InformationCacheServiceExecutor<K,T>>
    implements MyLogger
{
    private Map<K,E> cacheMap = Collections.synchronizedMap( new HashMap<>() );

    /**
     * Constructor.
     */
    protected InformationCache()
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
        final E cacheEntry = this.cacheMap.get( searchKey );
        /*
         * If null, the item is not in the cache
         */
        if ( cacheEntry == null )
        {
            returnCacheEntry = this.createCacheEntry();
            returnCacheEntry.setFetchState( FETCHING );
            returnCacheEntry.setCacheState( STALE );
            /*
             * Store an "empty" cache entry for now
             */
            this.cacheMap
                .put( searchKey, returnCacheEntry );
            this.synchronousFetch( searchKey, returnCacheEntry );
        }
        else
        {
            if ( cacheEntry.isStale() )
            {
                switch ( cacheEntry.getFetchState() )
                {
                    case FETCHING:
                        returnCacheEntry = cacheEntry;
                        break;

                    case NOT_FETCHING:
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
        logMethodEnd( methodName, returnCacheEntry );
        return returnCacheEntry;
    }

    /**
     * Obtains the information from the cache.
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
        E returnCacheEntry = null;
        final E cacheEntry = this.cacheMap.get( searchKey );
        /*
         * If null, the item is not in the cache
         */
        if ( cacheEntry == null )
        {
            returnCacheEntry = this.createCacheEntry();
            returnCacheEntry.setFetchState( FETCHING );
            returnCacheEntry.setCacheState( STALE );
            /*
             * Store an "empty" cache entry for now
             */
            this.cacheMap
                .put( searchKey, returnCacheEntry );
            this.asynchronousFetch( searchKey, returnCacheEntry );
        }
        else
        {
            if ( cacheEntry.isStale() )
            {
                switch ( cacheEntry.getFetchState() )
                {
                    case FETCHING:
                        returnCacheEntry = cacheEntry;
                        break;

                    case NOT_FETCHING:
                        this.asynchronousFetch( searchKey, cacheEntry );
                        returnCacheEntry = cacheEntry;
                        break;
                }
            }
            else
            {
                returnCacheEntry = cacheEntry;
            }
        }
        logMethodEnd( methodName, returnCacheEntry );
        return returnCacheEntry;
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
        Optional<T> information = null;
        try
        {
            cacheEntry.setFetchState( FETCHING );
            information = this.getExecutor()
                              .synchronousFetch( searchKey );
            cacheEntry.setInformation( information.get() );
        }
        finally
        {
            cacheEntry.setFetchState( NOT_FETCHING );
        }

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
            .asynchronousFetch( searchKey, cacheEntry.getFetchSubject() );
        /*
         * Setup the handling to handle the completed request.
         */
        cacheEntry.getFetchSubject()
                  .share()
                  .doOnError( throwable ->
                              {
                                  logError( methodName, String.format( "Search Key: %s", searchKey ), throwable );
                                  cacheEntry.setFetchThrowable( throwable );
                                  cacheEntry.setInformation( null );
                                  cacheEntry.setFetchState( NOT_FETCHING );
                                  cacheEntry.getFetchSubject().onError( throwable );
                              })
                  .subscribe( optionalT ->
                              {
                                  logDebug( methodName, "Completed fetch of key {0} value {1}",
                                            searchKey, optionalT.get() );
                                  cacheEntry.setInformation( optionalT.get() );
                                  cacheEntry.setFetchState( NOT_FETCHING );
                                  cacheEntry.setCacheState( CURRENT );
                              });
        logMethodEnd( methodName );
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

}
