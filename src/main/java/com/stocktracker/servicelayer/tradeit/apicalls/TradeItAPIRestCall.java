package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.tradeit.TradeItURLs;
import com.stocktracker.servicelayer.tradeit.apiresults.TradeItAPIResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * This class encapsulates the logic to make a REST API call to TradeIt.
 * @param <T> The reponse type.  That is, the type returned by TradeIt.
 */
public abstract class TradeItAPIRestCall<T extends TradeItAPIResult> implements MyLogger
{
    private HttpHeaders headers;
    private MultiValueMap<String, String> parameterMap;

    protected TradeItURLs tradeItURLs;

    /**
     * Creates a new instance.
     */
    public TradeItAPIRestCall()
    {
        this.parameterMap = new LinkedMultiValueMap<>();
        createHttpHeaders();
    }

    /**
     * Adds a POST parameter to the REST call.
     * @param name
     * @param value
     */
    protected void addPostParameter( final String name, final String value )
    {
        logDebug( "addPostParameter", "name: {0} value: {1}", name, value );
        this.parameterMap.add( name, value );
    }

    /**
     * Make the REST call
     * @param url
     * @return
     */
    protected T callTradeIt( final String url )
    {
        final String methodName = "callTradeIt";
        logMethodBegin( methodName, url );
        this.addPostParameter( TradeItURLs.API_KEY_PARAM, tradeItURLs.getAPIKey() );
        final HttpEntity<MultiValueMap<String, String>> request = this.createHttpEntity();
        final RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<T> responseEntity = restTemplate.postForEntity( url, request, this.getApiResponseClass() );
        final T response = responseEntity.getBody();
        logMethodEnd( methodName, response );
        return response;
    }

    /**
     * Creates the HttpHeaders
     */
    protected void createHttpHeaders()
    {
        headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_FORM_URLENCODED );
    }

    /**
     * Creates the HttpEntity with contains the HttpHeader and the method parameters.
     * @return
     */
    protected HttpEntity<MultiValueMap<String, String>> createHttpEntity()
    {
        return new HttpEntity<>( this.parameterMap, this.headers );
    }

    /**
     * Subclasses must specify the TradeIt result type.
     * @return
     */
    protected abstract Class<T> getApiResponseClass();

    /**
     * DI injection point.
     * @param tradeItURLs
     */
    @Autowired
    public void setTradeItURLs( final TradeItURLs tradeItURLs )
    {
        logInfo( "setTradeItURLs", "Dependency injection of " + tradeItURLs );
        this.tradeItURLs = tradeItURLs;
    }
}

