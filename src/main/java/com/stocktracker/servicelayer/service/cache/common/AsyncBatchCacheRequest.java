package com.stocktracker.servicelayer.service.cache.common;

import io.reactivex.processors.AsyncProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Contains the cache request key and the RxJava async processor information for a batch request.
 * @param <CK> Key to the cache.
 * @param <CD> Cached data taype.
 * @param <ASK> async key, the key used to retrieve the information from the third part.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class AsyncBatchCacheRequest<CK,
                                    CD,
                                   ASK>

{
    private CK cacheKey;
    private ASK asyncKey;
    private AsyncProcessor<CD> asyncProcessor;
    private AsyncBatchCacheRequestResult requestResult;
    private Throwable exception;

    public CK getCacheKey()
    {
        return this.cacheKey;
    }

    public void setCacheKey( final CK cacheKey )
    {
        this.cacheKey = cacheKey;
    }

    public AsyncProcessor<CD> getAsyncProcessor()
    {
        return asyncProcessor;
    }

    public void setAsyncProcessor( final AsyncProcessor<CD> asyncProcessor )
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
        final StringBuilder sb = new StringBuilder( "AsyncBatchCacheRequest{" );
        sb.append( "cacheKey=" ).append( cacheKey );
        sb.append( ", asyncKey=" ).append( asyncKey );
        sb.append( ", requestResult=" ).append( requestResult );
        sb.append( ", exception=" ).append( exception );
        sb.append( '}' );
        return sb.toString();
    }
}
