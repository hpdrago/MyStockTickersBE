package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.tradeit.TradeItParameter;
import com.stocktracker.servicelayer.tradeit.TradeItService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.Set;

/**
 * This class encapsualtes the TradeIt API call parameters to include validation checks and maintenance of the
 * parameter map that is use with the Spring Rest Template to send the parameters as HTTP Post parameters.
 */
public class TradeItAPICallParameters implements MyLogger
{
    private MultiValueMap<String, String> parameterMap = new LinkedMultiValueMap<>();

    /**
     * Get the parameter value from the map.
     * @param tradeItParameter
     * @return Null if the parameter is not found.
     */
    public String getParameterValue( final TradeItParameter tradeItParameter )
    {
        return this.getParameterValue( tradeItParameter.getTradeItParameterName() );
    }

    /**
     * Get the parameter value from the map.
     * @param parameter
     * @return Null if the parameter is not found.
     */
    public String getParameterValue( final String parameter )
    {
        if ( this.parameterMap.containsKey( parameter ))
        {
            return this.parameterMap.get( parameter ).get( 0 );
        }
        else
        {
            return null;
        }
    }

    /**
     * Checks the parameters to ensure that the map contains all {@code parameters}
     * @param parameters
     * @throws IllegalArgumentException if a parameter is missing.
     */
    public void parameterCheck( final TradeItParameter ... parameters )
    {
        for ( final TradeItParameter parameter: parameters )
        {
            if ( !this.parameterMap.containsKey( parameter.getTradeItParameterName() ))
            {
                throw new IllegalArgumentException( "Parameter map does not contain " + parameter );
            }
        }
    }

    public Set<String> keySet()
    {
        return this.parameterMap.keySet();
    }

    /**
     * Creates a new map.
     * @return
     */
    public static TradeItAPICallParameters newMap()
    {
        return new TradeItAPICallParameters();
    }

    /**
     * Add the parameter to the map.
     * @param tradeItParameter Enum value for the TradeIt parameter.
     * @param parameterValue
     * @return The map instance to provide a builder pattern.
     */
    public TradeItAPICallParameters addParameter( final TradeItParameter tradeItParameter, final String parameterValue )
    {
        this.parameterMap.put( tradeItParameter.getTradeItParameterName(),
                               Collections.singletonList( parameterValue ));
        return this;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "TradeItAPICallParameters{" );
        sb.append( "parameterMap=" ).append( parameterMap );
        sb.append( '}' );
        return sb.toString();
    }

    /**
     * Dumps the contents of the parameter map to the log.
     */
    public void dumpToLog()
    {
        this.parameterMap
            .forEach( ( key, value ) -> logDebug( "dumpToLog", "{0} = {1}", key, value  ));
    }
}
