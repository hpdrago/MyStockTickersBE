package com.stocktracker.servicelayer.service.cache.common;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Contains the results of a batch request which includes the requested data and an exception to identify errors
 * encountered for the request.
 *
 * @param <K> Type of data that is used for the cache key.
 * @param <T> Type of data returned from the third party.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class AsyncBatchCacheResponse<K,T>
{
    private K cacheKey;
    private T data;
    private Exception exception;

    public T getData()
    {
        return data;
    }

    public void setData( final T data )
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

    public K getCacheKey()
    {
        return cacheKey;
    }

    public void setCacheKey( final K cacheKey )
    {
        this.cacheKey = cacheKey;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "AsyncBatchCacheResult{" );
        sb.append( "cacheKey=" ).append( cacheKey );
        sb.append( ", data=" ).append( data );
        sb.append( ", exception=" ).append( exception );
        sb.append( '}' );
        return sb.toString();
    }
}
