package com.stocktracker.weblayer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public class TradeItBrokersDTO extends TradeItResult
{
    @JsonProperty("brokerList")
    private TradeItBroker[] brokers;

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "TradeItBrokers{" );
        sb.append( "brokers=" ).append( Arrays.toString( brokers ) );
        sb.append( ", status='" ).append( status ).append( '\'' );
        sb.append( ", token='" ).append( token ).append( '\'' );
        sb.append( ", shortMessage='" ).append( shortMessage ).append( '\'' );
        sb.append( ", longMessages='" ).append( longMessages ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
