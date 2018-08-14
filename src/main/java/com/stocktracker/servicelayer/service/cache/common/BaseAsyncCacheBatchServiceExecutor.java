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
 * @param <CK> The type of the cache key.
 * @param <CD> The type of the cached data.
 * @param <ASK> The key to retrieve the async data.
 * @param <ASD> The type of the data to be retrieved form the async source.
 * @param <RQ> - The Async Request Type.
 * @param <RS> - The Async Response Type.
 */
public abstract class BaseAsyncCacheBatchServiceExecutor<CK,
                                                         CD,
                                                         ASK,
                                                         ASD,
                                                         RK extends AsyncBatchCacheRequestKey<CK,ASK>,
                                                         RQ extends AsyncBatchCacheRequest<CK,CD,ASK>,
                                                         RS extends AsyncBatchCacheResponse<CK,ASK,ASD>>
    extends BaseAsyncCacheServiceExecutor<ASK,ASD>
    implements AsyncCacheBatchServiceExecutor<CK,CD,ASK,ASD,RQ,RS>
{
    /**
     * Asynchronous fetching of the information for {@code searchKey}.
     * Subclasses must override this method and add the @Async annotation so that this method executes on a separate
     * thread.
     * @param requests Contains a list of {@code AsyncBatchCacheRequest} instances each of which contains the request
     *                 key and the RxJava async processor instance.
     */
    public void asynchronousFetch( @NotNull final List<RQ> requests )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, requests.size() + " requests: " + requests );
        List<RQ> requestGroup = new ArrayList<>();
        final List<RS> responses = new ArrayList();
        for ( final RQ entry: requests )
        {
            requestGroup.add( entry );
            /*
             * IEXTrading is limited to 100 symbols at a time.
             */
            if ( requestGroup.size() == 100 )
            {
                final List<RS> batchResponse = this.synchronousFetch( requestGroup );
                responses.addAll( batchResponse );
                requestGroup = new ArrayList<>();
            }
        }
        /*
         * If there were more than 100, then this is remaining elements to process, otherwise it contains all of the
         * elements to process.
         */
        if ( requests.size() > 0 )
        {
            final List<RS> batchResponse = this.synchronousFetch( requests );
            responses.addAll( batchResponse );
        }
        this.evaluateResponses( requests, responses );
        logMethodEnd( methodName );
    }

    /**
     * Fetch the stock quote batch from IEXTrading.
     * @param requests Contains the information to make the batch request.
     * @return
     */
    public List<RS> synchronousFetch( final List<RQ> requests )
    {
        final String methodName = "synchronousFetch";
        logMethodBegin( methodName, requests );
        /*
         * Get all of the request keys.
         */
        final List<RK> requestKeys = requests.stream()
                                             .map( request ->
                                                   {
                                                       return this.createRequestKey( request.getCacheKey(),
                                                                                     request.getASyncKey() );
                                                   })
                                             .collect(Collectors.toList());
        /*
         * Make the batch request.
         */
        final Map<ASK,ASD> asyncDataList = this.batchFetch( requestKeys );

        /*
         * Convert the external data to responses.
         */
        final List<RS> responses = asyncDataList.values()
                                                .stream()
                                                .map( asyncData ->
                                                      {
                                                          final RS response = this.newResponse();
                                                          this.processResponse( asyncData, response );
                                                          return response;
                                                      })
                                                .collect( Collectors.toList() );
        logMethodEnd( methodName, responses.size() + " responses" );
        return responses;
    }

    /**
     * Method to create the <RQ> request key which contains the cache key and the async key.
     * @param cacheKey
     * @param asyncKey
     * @return
     */
    protected abstract RK createRequestKey( final CK cacheKey, final ASK asyncKey );

    /**
     * Sets the cache key and data on the response object.
     * @param asyncData
     * @param response
     */
    protected void processResponse( final ASD asyncData,
                                    final RS response )
    {
        response.setData( asyncData );
    }

    /**
     * Subclasses must override this method to retrieve the external data for the {@code requestKeys}.
     * @param requestKeys
     * @return
     */
    protected abstract Map<ASK,ASD> batchFetch( final List<RK> requestKeys );

    /**
     * Cycles through the responses and updates each request
     * @param requests
     * @param responses
     */
    protected void evaluateResponses( final List<RQ> requests,
                                      final List<RS> responses )
    {
        final String methodName = "evaluateResponses";
        logMethodEnd( methodName, String.format( "requests: %d responses: %d",
                                                 requests.size(), responses.size() ));
        logMethodBegin( methodName, "request keys: {0}", requests );
        logMethodBegin( methodName, "response keys: {0}", responses.stream()
                                                                          .map( response -> response.getCacheKey() )
                                                                          .collect( Collectors.toList() ));
        /*
         * Save a lit of the request keys and remove them from the set as we step through the responses so at the end
         * of the step process, we can identify the requests that did not receive a response.
         */
        final List<ASK> requestKeys = new ArrayList<>();
        /*
         * Create a map of the requests for easier matching of requests to responses
         */
        final Map<ASK,RQ> requestMap = new HashMap<>();
        /*
         * Extract the request data into the request keys list and request map.
         */
        requests.forEach( request ->
                          {
                              requestMap.put( request.getASyncKey(), request );
                              requestKeys.add( request.getASyncKey() );
                          });
        /*
         * Cycle through the results, find the associated request entry, and make the results notification through
         * the request's AsyncProcessor.
         */
        for ( final RS response: responses )
        {
            requestKeys.remove( response.getASyncKey() );
            final RQ request = requestMap.get( response.getASyncKey() );
            Objects.requireNonNull( request, "Request not found for cache key: " + response.getCacheKey() );
            if ( response.getException() == null )
            {
                request.setRequestResult( AsyncBatchCacheRequestResult.FOUND );
                /*
                 * The data retrieved form the async source may not be the same as what is cached.
                 * Subclasses can override the {@code convertASyncToCacheData} if this is the case.
                 */
                final CD cachedData;
                try
                {
                    cachedData = this.convertASyncData( response.getCacheKey(),
                                                        response.getASyncKey(),
                                                        response.getData() );
                    request.getAsyncProcessor()
                           .onNext( cachedData );
                    request.getAsyncProcessor()
                           .onComplete();
                }
                catch( AsyncCacheDataNotFoundException e )
                {
                    request.setRequestResult( AsyncBatchCacheRequestResult.ERROR );
                    request.setException( e );
                }
            }
            else
            {
                request.setRequestResult( AsyncBatchCacheRequestResult.ERROR );
                request.setException( response.getException() );
            }
        }

        /*
         * For each request for which a response was not found, notify the caller of the error.
         */
        requestKeys.forEach( asyncKey -> requestMap.get( asyncKey )
                                                   .setRequestResult( AsyncBatchCacheRequestResult.NOT_FOUND ));
        logMethodEnd( methodName );
    }

    /**
     * Subclasses must override this method to convert the async data into a cache data instance.
     * @param cacheKey
     * @param asyncKey
     * @param asyncData
     * @return
     */
    protected abstract CD convertASyncData( @NotNull final CK cacheKey,
                                            @NotNull final ASK asyncKey,
                                            @NotNull final ASD asyncData )
        throws AsyncCacheDataNotFoundException;

    /**
     * Subclasses must override this method to create a new response type.
     * @return
     */
    protected abstract RS newResponse();

}
