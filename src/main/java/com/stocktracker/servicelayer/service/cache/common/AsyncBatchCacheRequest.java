package com.stocktracker.servicelayer.service.cache.common;

import io.reactivex.processors.AsyncProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Contains the cache request key and the RxJava async processor information for a batch request.
 * @param <K> Key to the cache.
 * @param <T> Type of data to be returned from the third party.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class AsyncBatchCacheRequest<K,T>
{
    private K cacheKey;
    private AsyncProcessor<T> asyncProcessor;
    private AsyncBatchCacheRequestResult requestResult;
    private Throwable exception;

    public K getCacheKey()
    {
        return cacheKey;
    }
    public void setCacheKey( final K cacheKey )
    {
        this.cacheKey = cacheKey;
    }

    public AsyncProcessor<T> getAsyncProcessor()
    {
        return asyncProcessor;
    }

    public void setAsyncProcessor( final AsyncProcessor<T> asyncProcessor )
    {
        this.asyncProcessor = asyncProcessor;
    }

    public AsyncBatchCacheRequestResult getRequestResult()
    {
        return requestResult;
    }

    public void setRequestResult( final AsyncBatchCacheRequestResult requestResult )
    {
        this.requestResult = requestResult;
    }

    public Throwable getException()
    {
        return exception;
    }

    public void setException( final Throwable exception )
    {
        this.exception = exception;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "AsyncBatchCacheRequest{" );
        sb.append( "cacheKey=" ).append( cacheKey );
        sb.append( ", requestResult=" ).append( requestResult );
        sb.append( ", exception=" ).append( exception );
        sb.append( '}' );
        return sb.toString();
    }
}
