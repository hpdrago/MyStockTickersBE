package com.stocktracker.servicelayer.service.cache.common;

import io.reactivex.processors.AsyncProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Contains the cache request key and the RxJava async processor information for a batch request.
 * @param <CK> Key to the cache.
 * @param <CD> Cached data taype.
 * @param <TPK> Third party key, the key used to retrieve the information from the third part.
 * @param <RK> The request key type that contains the cache key and the third party key.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class AsyncBatchCacheRequest<CK,
                                    CD,
                                   TPK,
                                    RK extends AsyncBatchCacheRequestKey<CK,TPK>>
{
    //private CK cacheKey;
    //private TPK thirdPartyKey;
    private RK requestKey;
    private AsyncProcessor<CD> asyncProcessor;
    private AsyncBatchCacheRequestResult requestResult;
    private Throwable exception;

    public CK getCacheKey()
    {
        return this.requestKey.getCacheKey();
    }

    public void setCacheKey( final CK cacheKey )
    {
        this.requestKey.setCacheKey( cacheKey );
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
        final StringBuilder sb = new StringBuilder( "AsyncBatchCacheRequest{" );
        sb.append( "cacheKey=" ).append( this.requestKey.getCacheKey() );
        sb.append( ", thirdPartyKey=" ).append( this.requestKey.getThirdPartyKey() );
        sb.append( ", requestResult=" ).append( requestResult );
        sb.append( ", exception=" ).append( exception );
        sb.append( '}' );
        return sb.toString();
    }
}
