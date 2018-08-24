package com.stocktracker.servicelayer.service.cache.common;

import io.reactivex.processors.AsyncProcessor;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * This class is the base class for all objects that are cached in the AsyncCache.
 * @param <CK> - The type of the cache key.
 * @param <CD> - The type of cachedData to be retrieved form the async source source.
 * @param <ASK> - The type of the async key.
 */
public abstract class AsyncCacheEntry<CK,
                                      CD,
                                     ASK,
                                     ASD>
{
    /**
     * The current state of the cached item.
     */
    private AsyncCacheEntryState cacheState = AsyncCacheEntryState.STALE;

    /**
     * The last time the cached cachedData was fetched.
     */
    private long lastRefreshTime;

    /**
     * Identifies when the cached data will expire and will become STATE.
     */
    private Timestamp expirationTime;

    /**
     * Identifies if the cached item is in the process of being fetched or not.
     */
    private AsyncCacheFetchState fetchState = AsyncCacheFetchState.NOT_FETCHING;

    /**
     * This async process is used to synchronize the entire background processing of fetching the external data and
     * whatever additional processing the cache needs to perform before sending the results to the requester.
     * This is the processor that the client is waiting on.
     */
    private AsyncProcessor<CD> cachedDataSyncProcessor;

    /**
     * This async processor is used to synchronize with the retrieval of the async data.  It is only used internally
     * within the cache itself to synchronize the retrieving of the background fetching of of the async data.
     */
    private AsyncProcessor<ASD> asyncDataSyncProcessor;

    /**
     * The exception if the asynchronous request failed.
     */
    private Throwable fetchThrowable;

    /**
     * The cachedData being cached.
     */
    private CD cachedData;

    /**
     * The key to the cache. Added so the key appears in the log file.
     */
    private CK cacheKey;

    /**
     * The key used to retrieve the information form the async source.
     */
    private ASK asyncKey;

    /**
     * Creates a new instance.
     *   Cache State = STALE
     *   Fetch State = NOT FETCHING
     *   Sets expiration time.
     */
    public AsyncCacheEntry()
    {
        this.expirationTime = new Timestamp( System.currentTimeMillis() + this.getCurrentDurationTime() );
        this.cachedDataSyncProcessor = AsyncProcessor.create();
        this.cachedDataSyncProcessor.serialize();
        this.asyncDataSyncProcessor = AsyncProcessor.create();
        this.asyncDataSyncProcessor.serialize();
    }

    /**
     * Set the cachedData object.
     * @param cachedData
     */
    public synchronized void setCachedData( final CK cacheKey,
                                            final ASK asyncKey,
                                            final CD cachedData )
    {
        Objects.requireNonNull( cacheKey, "cacheKey argument cannot be null" );
        this.cachedData = cachedData;
        this.asyncKey = asyncKey;
        this.expirationTime = new Timestamp( System.currentTimeMillis() + this.getCurrentDurationTime() );
    }

    /**
     * Get the cachedData.
     * @return
     */
    public synchronized CD getCachedData()
    {
        return this.cachedData;
    }

    public void setCachedData( final CD cachedData )
    {
        this.cachedData = cachedData;
    }

    /**
     * Identifies how long a cache entry will be CURRENT before transitioning to STALE.
     * @return
     */
    protected abstract long getCurrentDurationTime();

    /**
     * Identifies the cached state: CURRENT, STALE, FAILURE, NOT_FOUND
     */
    public synchronized AsyncCacheEntryState getCacheState()
    {
        if ( this.cacheState.isCurrent() && this.isStale() )
        {
            this.cacheState = AsyncCacheEntryState.STALE;
        }
        return cacheState;
    }

    /**
     * Set the cache state
     * @param cacheState
     */
    public synchronized void setCacheState( AsyncCacheEntryState cacheState )
    {
        this.cacheState = cacheState;
    }

    public synchronized long getLastRefreshTime()
    {
        return lastRefreshTime;
    }

    protected synchronized void setLastRefreshTime( long lastRefreshTime )
    {
        this.lastRefreshTime = lastRefreshTime;
    }

    /**
     * Determines when the stock price is stale.  Stock prices are stale after 15 minutes during trading hours.
     */
    public synchronized Timestamp getExpirationTime()
    {
        return expirationTime;
    }

    /**
     * Set the expiration time.
     * @param expirationTime
     */
    protected synchronized void setExpirationTime( final Timestamp expirationTime )
    {
        this.expirationTime = expirationTime;
    }

    /**
     * Determines if the stock price is currently being fetched.
     */
    public synchronized AsyncCacheFetchState getFetchState()
    {
        return fetchState;
    }

    /**
     * Set the fetch state.
     * @param fetchState
     */
    protected synchronized void setFetchState( AsyncCacheFetchState fetchState )
    {
        this.fetchState = fetchState;
    }

    /**
     * The RxJava subject to contain any requests waiting for the stock price while it's being fetched.
     */
    public synchronized AsyncProcessor<CD> getCachedDataSyncProcessor()
    {
        return cachedDataSyncProcessor;
    }

    /**
     * The exception if any occurred while fetchingProcessCashBatch.
     */
    public synchronized Throwable getFetchThrowable()
    {
        return fetchThrowable;
    }

    /**
     * Set the exception throwable.
     * @param fetchThrowable
     */
    public synchronized void setFetchThrowable( final Throwable fetchThrowable )
    {
        this.fetchThrowable = fetchThrowable;
    }

    /**
     * Returns try if the entry has expired.
     * @return
     */
    public synchronized boolean isStale()
    {
        return this.cacheState.isStale();
    }

    public CK getCacheKey()
    {
        return cacheKey;
    }

    public void setCacheKey( final CK cacheKey )
    {
        this.cacheKey = cacheKey;
    }

    public ASK getASyncKey()
    {
        return asyncKey;
    }

    public void setASyncKey( final ASK asyncKey )
    {
        this.asyncKey = asyncKey;
    }

    /**
     * Get the Asynchronous processor
     * @return
     */
    public AsyncProcessor<ASD> getASyncDataSyncProcessor()
    {
        return asyncDataSyncProcessor;
    }

    public ASK getAsyncKey()
    {
        return asyncKey;
    }

    public void setAsyncKey( final ASK asyncKey )
    {
        this.asyncKey = asyncKey;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "AsyncCacheEntry" );
        sb.append( "@" ).append( hashCode() ).append( "{" );
        sb.append( "cacheKey=" ).append( cacheKey );
        sb.append( ", cacheState=" ).append( cacheState );
        sb.append( ", lastRefreshTime=" ).append( lastRefreshTime );
        sb.append( ", expirationTime=" ).append( expirationTime );
        sb.append( ", fetchState=" ).append( fetchState );
        sb.append( ", cachedData=" ).append( cachedData );
        sb.append( ", isStale=" ).append( this.isStale() );
        sb.append( '}' );
        return sb.toString();
    }
}
