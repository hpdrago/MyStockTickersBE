package com.stocktracker.servicelayer.service.cache.common;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This is the base service executor for making batch requests to third parties.
 *
 * @param <K> The type of the key.
 * @param <T> The type of the data to be retrieved.
 * @param <RQ> - The Async Request Type.
 * @param <RS> - The Async Response Type.
 */
public abstract class BaseAsyncCacheBatchServiceExecutor<K,
                                                         T,
                                                         RQ extends AsyncBatchCacheRequest<K,T>,
                                                         RS extends AsyncBatchCacheResponse<K,T>>
    extends BaseAsyncCacheServiceExecutor<K,T>
    implements AsyncCacheBatchServiceExecutor<K,T,RQ,RS>
{
    /**
     * Asynchronous fetching of the information for {@code searchKey}.
     * Subclasses must override this method and add the @Async annotation so that this method executes on a separate
     * thread.
     * @param requests Contains a list of {@code AsyncBatchCacheRequest} instances each of which contains the request
     *                 key and the RxJava async processor instance.
     */
    public void asynchronousFetch( @NotNull final Map<K,RQ> requests )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, requests.size() + " requests: " + requests.keySet() );
        Map<K, RQ> requestMap = new HashMap<>();
        for ( final RQ entry: requests.values() )
        {
            requestMap.put( entry.getCacheKey(), entry );
            /*
             * IEXTrading is limited to 100 symbols at a time.
             */
            if ( requestMap.size() == 100 )
            {
                this.synchronousFetch( requestMap );
                requestMap = new HashMap<>();
            }
        }
        /*
         * If there were more than 100, then this is remaining elements to process, otherwise it contains all of the
         * elements to process.
         */
        if ( requestMap.size() > 0 )
        {
            this.synchronousFetch( requestMap );
        }
        /*
         * Execute the batch request.
         */
        final List<RS> responses = this.synchronousFetch( requests );
        /*
         * Need to ensure that we have the same number of results as requests or something went wrong.
         */
        if ( requests.size() != responses.size() )
        {
            throw new IllegalArgumentException( String.format( "Request (%d) and results (%d) sizes do not match.",
                                                               requests.size(), responses.size() ));
        }
        this.evaluateResponses( requests, responses );
        logMethodEnd( methodName );
    }

    /**
     * Fetch the stock quote batch from IEXTrading.
     * @param requests Contains the information to make the batch request.
     * @return
     */
    public List<RS> synchronousFetch( final Map<K, RQ> requests )
    {
        final String methodName = "getExternalData";
        logMethodBegin( methodName, requests.keySet() );
        /*
         * Get all of the request keys.
         */
        final List<K> requestKeys = new ArrayList<>( requests.keySet() );
        /*
         * Make the batch request.
         */
        final List<T> externalDataBatch = this.getExternalData( requestKeys );
        /*
         * Convert the external data to responses.
         */
        final List<RS> responses = externalDataBatch.stream()
                                                    .map( externalData ->
                                                    {
                                                        final RS response = this.newResponse();
                                                        this.processResponse( externalData, response );
                                                        return response;
                                                    })
                                                    .collect( Collectors.toList() );
        logMethodEnd( methodName, responses.size() + " responses" );
        return responses;
    }

    /**
     * Sets the cache key and data on the response object.
     * @param externalData
     * @param response
     */
    protected void processResponse( final T externalData, final RS response )
    {
        response.setCacheKey( this.getCacheKey( externalData ));
        response.setData( externalData );
    }

    /**
     * Subclasses must override this method to retrieve the external data for the {@code requestKeys}.
     * @param requestKeys
     * @return
     */
    protected abstract List<T> getExternalData( final List<K> requestKeys );

    /**
     * Cycles through the responses and updates each request
     * @param requests
     * @param responses
     */
    protected void evaluateResponses( final @NotNull Map<K,RQ> requests, final List<RS> responses )
    {
        /*
         * Cycle through the results, find the associated request entry, and make the results notification through
         * the request's AsyncProcessor.
         */
        for ( final AsyncBatchCacheResponse<K,T> result: responses )
        {
            final AsyncBatchCacheRequest<K,T> request = requests.get( result.getCacheKey() );
            Objects.requireNonNull( request, "Request not found for cache key: " + result.getCacheKey() );
            if ( result.getException() == null )
            {
                request.getAsyncProcessor()
                       .onNext( result.getData() );
                request.getAsyncProcessor()
                       .onComplete();
            }
            else
            {
                request.getAsyncProcessor()
                       .onError( result.getException() );
            }
        }
    }

    /**
     * Extracts the cache key from the external data.
     * @param externalData
     * @return
     */
    protected abstract K getCacheKey( final T externalData );

    /**
     * Subclasses must override this method to create a new response type.
     * @return
     */
    protected abstract RS newResponse();

}
