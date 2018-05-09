package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.common.MyLogger;
import io.reactivex.processors.BehaviorProcessor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

/**
 * <T> - The type of information to be retrieved from the third party source.
 * @param <T>
 */
public abstract class InformationCacheEntry<T> implements MyLogger
{
    private InformationCacheEntryState cacheState = InformationCacheEntryState.STALE;
    private long lastRefreshTime;
    private Date expirationTime;
    private InformationCacheFetchState fetchState = InformationCacheFetchState.NOT_FETCHING;
    private BehaviorProcessor<Optional<T>> fetchSubject;
    private Throwable fetchThrowable;
    private T information;

    /**
     * Creates a new instance.
     *   Cache State = STALE
     *   Fetch State = NOT FETCHING
     *   Sets expiration time.
     */
    public InformationCacheEntry()
    {
        this.expirationTime = new Date( System.currentTimeMillis() + this.getCurrentDurationTime() );
        this.fetchSubject = BehaviorProcessor.create();
        this.fetchSubject.serialize();
    }

    /**
     * Set the information object.
     * @param information
     */
    public void setInformation( final T information )
    {
        final String methodName = "setInformation";
        logMethodBegin( methodName, information );
        this.information = information;
        logMethodEnd( methodName );
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
    public InformationCacheEntryState getCacheState()
    {
        if ( this.isStale() )
        {
            this.cacheState = InformationCacheEntryState.STALE;
        }
        return cacheState;
    }

    /**
     * Set the cache state
     * @param cacheState
     */
    public void setCacheState( InformationCacheEntryState cacheState )
    {
        this.cacheState = cacheState;
    }

    public long getLastRefreshTime()
    {
        return lastRefreshTime;
    }

    public void setLastRefreshTime( long lastRefreshTime )
    {
        this.lastRefreshTime = lastRefreshTime;
    }

    /**
     * Determines when the stock price is stale.  Stock prices are stale after 15 minutes during trading hours.
     */
    public Date getExpirationTime()
    {
        return expirationTime;
    }

    public void setExpirationTime( Timestamp expirationTime )
    {
        this.expirationTime = expirationTime;
    }

    /**
     * Determines if the stock price is currently being fetched.
     */
    public InformationCacheFetchState getFetchState()
    {
        return fetchState;
    }

    public void setFetchState( InformationCacheFetchState fetchState )
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

    public void setFetchThrowable( final Throwable fetchThrowable )
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

    /*
    public Observable getAsyncObservable()
    {
        return asyncObservable;
    }

    public void setAsyncObservable( final Observable asyncObservable )
    {
        this.asyncObservable = asyncObservable;
    }
    */

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "InformationCacheEntry{" );
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
