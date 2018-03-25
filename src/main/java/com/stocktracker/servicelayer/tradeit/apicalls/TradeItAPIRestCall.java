package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.TradeItServiceUnavailableException;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;

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
     * This is the method that is called by the TradeItService and must be implemented by all TradeIt API Calls.
     * @param parameterMap
     * @return
     */
    public abstract T execute( final TradeItAPICallParameters parameterMap );

    /**
     * Make the REST call
     * @param parameterMap Contains the parameters for the TradeIt API Call.
     * @return API Result.
     */
    protected T callTradeIt( final TradeItAPICallParameters parameterMap )
    {
        final String methodName = "callTradeIt";
        logMethodBegin( methodName );
        final String url = this.getAPIURL();
        logDebug( methodName, "url: " + url );
        this.addPostParameters( parameterMap );
        this.addPostParameter( this.tradeItProperties.API_KEY_PARAM, this.tradeItProperties.getApiKey() );
        final HttpEntity<MultiValueMap<String, String>> request = this.createHttpEntity();
        final RestTemplate restTemplate = new RestTemplate();
        T response = null;
        try
        {
            final ResponseEntity<T> responseEntity = restTemplate.postForEntity( url, request, this.getAPIResultsClass() );
            logDebug( methodName, "ResponseEntity: {0}", responseEntity );
            response = responseEntity.getBody();
        }
        catch( HttpClientErrorException e )
        {
            throw new TradeItServiceUnavailableException( e );
        }
        catch( HttpServerErrorException e )
        {
            throw new TradeItServiceUnavailableException( e );
        }
        catch( UnknownHttpStatusCodeException e )
        {
            throw new TradeItServiceUnavailableException( e );
        }
        logMethodEnd( methodName, response );
        return response;
    }

    /**
     * Adds all of the parameters to the internal parameter map.
     * @param parameterMap
     */
    protected void addPostParameters( final TradeItAPICallParameters parameterMap )
    {
        for ( final String parameter: parameterMap.keySet() )
        {
            this.parameterMap.set( parameter, parameterMap.getParameterValue( parameter ) );
        }
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
        this.tradeItURLs = tradeItURLs;
    }
}

