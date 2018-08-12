package com.stocktracker.servicelayer.service.cache.common;

/**
 * Contains the cache key <CK> and the third party key <TPK>.
 * @param <CK> - Used to search the cache.
 * @param <TPK> - Used to retrieve the third party data.
 */
public abstract class AsyncBatchCacheRequestKey<CK,
                                                TPK>
{
    private CK cacheKey;
    private TPK thirdPartyKey;

    public AsyncBatchCacheRequestKey( final CK cacheKey, final TPK thirdPartyKey )
    {
        this.cacheKey = cacheKey;
        this.thirdPartyKey = thirdPartyKey;
    }

    public CK getCacheKey()
    {
        return cacheKey;
    }

    public void setCacheKey( final CK cacheKey )
    {
        this.cacheKey = cacheKey;
    }

    public TPK getThirdPartyKey()
    {
        return thirdPartyKey;
    }

    public void setThirdPartyKey( final TPK thirdPartyKey )
    {
        this.thirdPartyKey = thirdPartyKey;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "AsyncBatchCacheRequestKey{" );
        sb.append( "cacheKey=" ).append( cacheKey );
        sb.append( ", thirdPartyKey=" ).append( thirdPartyKey );
        sb.append( '}' );
        return sb.toString();
    }
}
