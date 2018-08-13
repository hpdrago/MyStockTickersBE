package com.stocktracker.servicelayer.service.cache.common;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Contains the results of a batch request which includes the requested data and an exception to identify errors
 * encountered for the request.
 *
 * @param <CK> Type of data that is used for the cache key.
 * @param <ASK> Type of data for the async key.
 * @param <ASD> Type of data returned form the async source.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class AsyncBatchCacheResponse<CK,
                                    ASK,
                                    ASD>
{
    private CK cacheKey;
    private ASK asyncKey;
    private ASD data;
    private Exception exception;

    public ASD getData()
    {
        return data;
    }

    public void setData( final ASD data )
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
        return this.cacheKey;
    }

    public void setCacheKey( final CK cacheKey )
    {
        this.cacheKey = cacheKey;
    }

    public ASK getASyncKey()
    {
        return this.asyncKey;
    }

    public void setASyncKey( final ASK asyncKey )
    {
        this.asyncKey = asyncKey;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "AsyncBatchCacheResult{" );
        sb.append( "cacheKey=" ).append( this.cacheKey );
        sb.append( ", asyncKey=" ).append( this.asyncKey );
        sb.append( ", data=" ).append( data );
        sb.append( ", exception=" ).append( exception );
        sb.append( '}' );
        return sb.toString();
    }
}
