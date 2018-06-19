package com.stocktracker.servicelayer.service.cache.common;

import io.reactivex.processors.AsyncProcessor;

import java.sql.Timestamp;

/**
 * This class is the base class for all objects that are cached in the AsyncCache.
 * <T> - The type of cachedData to be retrieved from the third party source.
 * @param <T>
 */
public abstract class AsyncCacheEntry<T>
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
     * The RxJava subject which is used to register to be notified when the asynchronous process completes.
     * It is also used internally to notify all of the subscribers.
     */
    private AsyncProcessor<T> asyncProcessor;

    /**
     * The exception if the asynchronous request failed.
     */
    private Throwable fetchThrowable;

    /**
     * The cachedData being cached.
     */
    private T cachedData;

    /**
     * Creates a new instance.
     *   Cache State = STALE
     *   Fetch State = NOT FETCHING
     *   Sets expiration time.
     */
    public AsyncCacheEntry()
    {
        this.expirationTime = new Timestamp( System.currentTimeMillis() + this.getCurrentDurationTime() );
        this.asyncProcessor = AsyncProcessor.create();
        this.asyncProcessor.serialize();
    }

    /**
     * Set the cachedData object.
     * @param cachedData
     */
    public synchronized void setCachedData( final T cachedData )
    {
        this.cachedData = cachedData;
        if ( cachedData == null )
        {
            T newCacheData = null;
        }
        this.expirationTime = new Timestamp( System.currentTimeMillis() + this.getCurrentDurationTime() );
    }

    /**
     * Get the cachedData.
     * @return
     */
    public synchronized T getCachedData()
    {
        return this.cachedData;
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
    public synchronized AsyncProcessor<T> getAsyncProcessor()
    {
        return asyncProcessor;
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

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "AsyncCacheEntry" );
        sb.append( "@" ).append( hashCode() ).append( "{" );
        sb.append( "cacheState=" ).append( cacheState );
        sb.append( ", lastRefreshTime=" ).append( lastRefreshTime );
        sb.append( ", expirationTime=" ).append( expirationTime );
        sb.append( ", fetchState=" ).append( fetchState );
        sb.append( ", cachedData=" ).append( cachedData );
        sb.append( ", isStale=" ).append( this.isStale() );
        sb.append( '}' );
        return sb.toString();
    }
}
