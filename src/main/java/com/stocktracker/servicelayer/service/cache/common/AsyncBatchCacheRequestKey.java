package com.stocktracker.servicelayer.service.cache.common;

/**
 * Contains the cache key <CK> and the async key <ASK>.
 * @param <CK> - Used to search the cache.
 * @param <ASK> - Used to retrieve the async data.
 */
public abstract class AsyncBatchCacheRequestKey<CK,
                                                ASK>
{
    private CK cacheKey;
    private ASK asyncKey;

    public AsyncBatchCacheRequestKey()
    {
    }

    public AsyncBatchCacheRequestKey( final CK cacheKey, final ASK asyncKey )
    {
        this.cacheKey = cacheKey;
        this.asyncKey = asyncKey;
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

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "AsyncBatchCacheRequestKey{" );
        sb.append( "cacheKey=" ).append( cacheKey );
        sb.append( ", asyncKey=" ).append( asyncKey );
        sb.append( '}' );
        return sb.toString();
    }
}
