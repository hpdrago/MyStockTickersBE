package com.stocktracker.servicelayer.tradeit.apiresults;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stocktracker.servicelayer.tradeit.apicalls.GetBrokersAPICall;
import com.stocktracker.servicelayer.tradeit.types.TradeItBroker;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * This class contains the list of brokers that are supported by TradeIt.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class GetBrokersAPIResult extends TradeItAPIResult
{
    @JsonProperty("brokerList")
    private TradeItBroker[] brokers;

    public GetBrokersAPIResult()
    {
    }

    public void setResults( final GetBrokersAPIResult getBrokersAPIResult )
    {
        super.setResults( getBrokersAPIResult );
        this.brokers = getBrokersAPIResult.brokers;
    }

    public int brokerCount()
    {
        return this.brokers.length;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "TradeItBrokers{" );
        sb.append( "brokers=" ).append( Arrays.toString( brokers ) );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }

}
