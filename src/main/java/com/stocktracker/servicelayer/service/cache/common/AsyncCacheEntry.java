package com.stocktracker.servicelayer.service.cache.common;

import io.reactivex.processors.BehaviorProcessor;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * This class is the base class for all objects that are cached in the AsyncCache.
 * <T> - The type of information to be retrieved from the third party source.
 * @param <T>
 */
public abstract class AsyncCacheEntry<T>
{
    /**
     * The current state of the cached item.
     */
    private AsyncCacheEntryState cacheState = AsyncCacheEntryState.STALE;

    /**
     * The last time the cached information was fetched.
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
    private BehaviorProcessor<Optional<T>> fetchSubject;

    /**
     * The exception if the asynchronous request failed.
     */
    private Throwable fetchThrowable;

    /**
     * The information being cached.
     */
    private T information;

    /**
     * Creates a new instance.
     *   Cache State = STALE
     *   Fetch State = NOT FETCHING
     *   Sets expiration time.
     */
    public AsyncCacheEntry()
    {
        this.expirationTime = new Timestamp( System.currentTimeMillis() + this.getCurrentDurationTime() );
        this.fetchSubject = BehaviorProcessor.create();
        this.fetchSubject.serialize();
    }

    /**
     * Set the information object.
     * @param information
     */
    public void setInformation( final T information )
    {
        this.information = information;
    }

    /**
     * Get the information.
     * @return
     */
    public T getInformation()
    {
        return this.information;
    }

    /**
     * Identifies how long a cache entry will be CURRENT before transitioning to STALE.
     * @return
     */
    protected abstract long getCurrentDurationTime();

    /**
     * Identifies the cached state: CURRENT, STALE, FAILURE, NOT_FOUND
     */
    public AsyncCacheEntryState getCacheState()
    {
        if ( this.isStale() )
        {
            this.cacheState = AsyncCacheEntryState.STALE;
        }
        return cacheState;
    }

    /**
     * Set the cache state
     * @param cacheState
     */
    protected void setCacheState( AsyncCacheEntryState cacheState )
    {
        this.cacheState = cacheState;
    }

    public long getLastRefreshTime()
    {
        return lastRefreshTime;
    }

    protected void setLastRefreshTime( long lastRefreshTime )
    {
        this.lastRefreshTime = lastRefreshTime;
    }

    /**
     * Determines when the stock price is stale.  Stock prices are stale after 15 minutes during trading hours.
     */
    public Timestamp  getExpirationTime()
    {
        return expirationTime;
    }

    /**
     * Set the expiration time.
     * @param expirationTime
     */
    protected void setExpirationTime( Timestamp expirationTime )
    {
        this.expirationTime = expirationTime;
    }

    /**
     * Determines if the stock price is currently being fetched.
     */
    public AsyncCacheFetchState getFetchState()
    {
        return fetchState;
    }

    /**
     * Set the fetch state.
     * @param fetchState
     */
    protected void setFetchState( AsyncCacheFetchState fetchState )
    {
        this.fetchState = fetchState;
    }

    /**
     * The RxJava subject to contain any requests waiting for the stock price while it's being fetched.
     */
    public BehaviorProcessor<Optional<T>> getFetchSubject()
    {
        return fetchSubject;
    }

    /**
     * The exception if any occurred while fetchingProcessCashBatch.
     */
    public Throwable getFetchThrowable()
    {
        return fetchThrowable;
    }

    /**
     * Set the exception throwable.
     * @param fetchThrowable
     */
    protected void setFetchThrowable( final Throwable fetchThrowable )
    {
        this.fetchThrowable = fetchThrowable;
    }

    /**
     * Returns try if the entry has expired.
     * @return
     */
    public boolean isStale()
    {
        return System.currentTimeMillis() > this.expirationTime.getTime();
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "AsyncCacheEntry{" );
        sb.append( "cacheState=" ).append( cacheState );
        sb.append( ", lastRefreshTime=" ).append( lastRefreshTime );
        sb.append( ", expirationTime=" ).append( expirationTime );
        sb.append( ", fetchState=" ).append( fetchState );
        sb.append( ", information=" ).append( information );
        sb.append( ", isStale=" ).append( this.isStale() );
        sb.append( '}' );
        return sb.toString();
    }
}
