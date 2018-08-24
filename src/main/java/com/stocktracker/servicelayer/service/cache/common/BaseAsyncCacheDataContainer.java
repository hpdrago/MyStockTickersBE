package com.stocktracker.servicelayer.service.cache.common;

import java.sql.Timestamp;

/**
 * Base class for cache data containers.  It contains the cache state, error, and expiration time values.  Subclasses
 * must implement the remaining methods.
 * @param <CK> - Key type.
 * @param <CD> - Cached data type.
 */
public abstract class BaseAsyncCacheDataContainer<CK,CD,ASK> implements AsyncCacheDataContainer<CK,CD,ASK>
{
    private AsyncCacheEntryState cacheState;
    private String cacheError;
    private Timestamp expirationTime;

    @Override
    public void setCacheState( final AsyncCacheEntryState stockQuoteEntityCacheState )
    {
        this.cacheState = stockQuoteEntityCacheState;
    }

    @Override
    public AsyncCacheEntryState getCacheState()
    {
        return this.cacheState;
    }

    @Override
    public void setCacheError( final String stockQuoteCacheError )
    {
        this.cacheError = stockQuoteCacheError;
    }

    @Override
    public String getCacheError()
    {
        return cacheError;
    }

    @Override
    public void setExpirationTime( final Timestamp expirationTime )
    {
        this.expirationTime = expirationTime;
    }

    @Override
    public Timestamp getExpirationTime()
    {
        return this.expirationTime;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "BaseAsyncCacheDataContainer{" );
        sb.append( "cacheState=" ).append( cacheState );
        sb.append( ", cacheError='" ).append( cacheError ).append( '\'' );
        sb.append( ", expirationTime=" ).append( expirationTime );
        sb.append( '}' );
        return sb.toString();
    }
}
