package com.stocktracker.servicelayer.service.cache.common;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Contains the results of a batch request which includes the requested data and an exception to identify errors
 * encountered for the request.
 *
 * @param <CK> Type of data that is used for the cache key.
 * @param <TPK> Type of data for the Third party key.
 * @param <TPD> Type of data returned from the third party.
 * @param <RK> Type of data for the request key type that contains the cache key and third party key.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class AsyncBatchCacheResponse<CK,
                                    TPK,
                                    TPD,
                                     RK extends AsyncBatchCacheRequestKey<CK,TPK>>
{
    private RK requestKey;
    private TPD data;
    private Exception exception;

    public TPD getData()
    {
        return data;
    }

    public void setData( final TPD data )
    {
        this.data = data;
    }

    public Exception getException()
    {
        return exception;
    }

    public void setException( final Exception exception )
    {
        this.exception = exception;
    }

    public CK getCacheKey()
    {
        return this.requestKey.getCacheKey();
    }

    public void setCacheKey( final CK cacheKey )
    {
        this.requestKey.setCacheKey( cacheKey );
    }

    public TPK getThirdPartyKey()
    {
        return this.requestKey.getThirdPartyKey();
    }

    public void setThirdPartyKey( final TPK thirdPartyKey )
    {
        this.requestKey.setThirdPartyKey( thirdPartyKey );
    }

    public RK getRequestKey()
    {
        return requestKey;
    }

    public void setRequestKey( final RK requestKey )
    {
        this.requestKey = requestKey;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "AsyncBatchCacheResult{" );
        sb.append( "cacheKey=" ).append( this.requestKey.getCacheKey() );
        sb.append( ", thirdPartyKey=" ).append( this.requestKey.getThirdPartyKey() );
        sb.append( ", data=" ).append( data );
        sb.append( ", exception=" ).append( exception );
        sb.append( '}' );
        return sb.toString();
    }
}
