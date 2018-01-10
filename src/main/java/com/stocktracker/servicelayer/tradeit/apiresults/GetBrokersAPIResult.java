package com.stocktracker.servicelayer.tradeit.apiresults;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stocktracker.weblayer.dto.tradeit.TradeItBroker;

import java.util.Arrays;

public class GetBrokersAPIResult extends TradeItAPIResult
{
    @JsonProperty("brokerList")
    private TradeItBroker[] brokers;

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "TradeItBrokers{" );
        sb.append( "brokers=" ).append( Arrays.toString( brokers ) );
        sb.append( ", status='" ).append( getStatus() ).append( '\'' );
        sb.append( ", token='" ).append( getToken() ).append( '\'' );
        sb.append( ", shortMessage='" ).append( getShortMessage() ).append( '\'' );
        sb.append( ", longMessages='" ).append( getLongMessages() ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }

}
