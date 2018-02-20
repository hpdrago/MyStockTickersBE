package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.TradeItAuthenticationException;
import com.stocktracker.servicelayer.tradeit.TradeItProperties;
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
 * @param <T> The response type.  That is, the type returned by TradeIt.
 */
public abstract class TradeItAPIRestCall<T extends TradeItAPIResult> implements MyLogger
{
    private HttpHeaders headers;
    private MultiValueMap<String, String> parameterMap;

    /**
     * This class contains the getURL methods and URL configurations.
     */
    protected TradeItURLs tradeItURLs;

    /**
     * This class contains the API parameter strings for the POST parameters.
     */
    @Autowired
    protected TradeItProperties tradeItProperties;

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
    protected final void addPostParameter( final String name, final String value )
    {
        logDebug( "addPostParameter", "name: {0} value: {1}", name, value );
        this.parameterMap.add( name, value );
    }

    /**
     * This is the default execute method.  It can be used for simple API calls since it doesn't add any parameters
     * to the API call.  Subclasses can create their own execute with parameters to use to set the necessary API
     * call parameters and then call this method.
     * @return
     */
    public final T execute()
        throws TradeItAuthenticationException
    {
        final String methodName = "execute";
        logMethodBegin( methodName );
        final T apiResult = this.callTradeIt() ;
        logMethodEnd( methodName, apiResult );
        return apiResult;
    }

    /**
     * Make the REST call
     * @return API Result.
     * @throws TradeItAuthenticationException
     */
    private T callTradeIt()
        throws TradeItAuthenticationException
    {
        final String methodName = "callTradeIt";
        logMethodBegin( methodName );
        final String url = this.getAPIURL();
        logDebug( methodName, "url: " + url );
        this.addPostParameter( this.tradeItProperties.API_KEY_PARAM, this.tradeItProperties.getApiKey() );
        final HttpEntity<MultiValueMap<String, String>> request = this.createHttpEntity();
        final RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<T> responseEntity = restTemplate.postForEntity( url, request, this.getAPIResultsClass() );
        logDebug( methodName, "ResponseEntity: {0}", responseEntity );
        T response = null;
        /**
         * Check for redirection error
         */
        if ( responseEntity.getStatusCode().is3xxRedirection() &&
             responseEntity.getStatusCodeValue() == 302 )
        {
            handleRedirection();
        }
        else
        {
            response = responseEntity.getBody();
        }
        logMethodEnd( methodName, response );
        return response;
    }

    /**
     * Creates a response object that indicates an authentication is required.
     * @return
     * @throws TradeItAuthenticationException
     */
    private void handleRedirection()
        throws TradeItAuthenticationException
    {
        final String methodName = "handleRedirection";
        final String message = "Received 302 redirection. Authentication required";
        logDebug( methodName, message );
        throw new TradeItAuthenticationException( message );
    }

    /**
     * Subclasses must override to define the correct URL to use for the API call.
     * @return
     */
    protected abstract String getAPIURL();

    /**
     * Creates the HttpHeaders
     */
    private void createHttpHeaders()
    {
        headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_FORM_URLENCODED );
    }

    /**
     * Creates the HttpEntity with contains the HttpHeader and the method parameters.
     * @return
     */
    private HttpEntity<MultiValueMap<String, String>> createHttpEntity()
    {
        return new HttpEntity<>( this.parameterMap, this.headers );
    }

    /**
     * Subclasses must specify the TradeIt result type.
     * @return
     */
    protected abstract Class<T> getAPIResultsClass();

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

