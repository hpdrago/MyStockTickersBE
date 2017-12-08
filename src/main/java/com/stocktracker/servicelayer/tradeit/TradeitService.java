package com.stocktracker.servicelayer.tradeit;

import com.stocktracker.weblayer.dto.TradeItBrokersDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * This service contains all of the REST calls to TradeIt
 */
@Service
public class TradeItService
{
    private static final String KEY_TAG = "<API_KEY>";
    private static final String GET_BROKER_LIST = "https://ems.qa.tradingticket.com/api/v1/preference/getBrokerList?apiKey=" + KEY_TAG;
    private static final Logger logger = LoggerFactory.getLogger( TradeItService.class );

    @Autowired
    private TradeItProperties tradeItProperties;


    /**
     * Get the list of brokers supported by TradeIt
     * @return
     */
    public TradeItBrokersDTO getBrokers()
    {
        final String methodName = "getBrokers";
        logger.debug( methodName + ".begin" );
        checkProperties();
        RestTemplate restTemplate = new RestTemplate();
        TradeItBrokersDTO brokers = restTemplate.getForObject( this.getURL( GET_BROKER_LIST ), TradeItBrokersDTO.class );
        logger.debug( methodName + ".end " + brokers );
        return brokers;
    }

    /**
     * Checks the required properties to be present to make rest calls to TradeIt
     */
    private void checkProperties()
    {
        Objects.requireNonNull( this.tradeItProperties, "TradeItProperties cannot be null" );
        Objects.requireNonNull( this.tradeItProperties.getApiKey(), "TradeItAPIKey cannot be null" );
    }

    /**
     * Adds the apiKey to {@code url}
     * @param url
     * @return
     */
    private String getURL( @NotNull final String url )
    {
        Objects.requireNonNull( url, "url cannot be null" );
        if ( !url.contains( KEY_TAG ))
        {
            throw new IllegalArgumentException( "url " + url + " must contain " + KEY_TAG );
        }
        return url.replace( "<API_KEY>", this.tradeItProperties.getApiKey() );
    }
}
