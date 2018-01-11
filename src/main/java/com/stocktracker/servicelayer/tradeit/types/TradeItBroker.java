package com.stocktracker.servicelayer.tradeit.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties( ignoreUnknown = true)
public class TradeItBroker
{
    public String shortName;
    public String longName;
    public String userName;
    public TradeItBrokerInstrument brokerInstruments[];

    public String getShortName()
    {
        return shortName;
    }

    public String getLongName()
    {
        return longName;
    }

    public String getUserName()
    {
        return userName;
    }

    public TradeItBrokerInstrument[] getBrokerInstruments()
    {
        return brokerInstruments;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "TradeitBroker{" );
        sb.append( "shortName='" ).append( shortName ).append( '\'' );
        sb.append( ", longName='" ).append( longName ).append( '\'' );
        sb.append( ", userName='" ).append( userName ).append( '\'' );
        sb.append( ", brokerInstruments=" ).append( Arrays.toString( brokerInstruments ) );
        sb.append( '}' );
        return sb.toString();
    }
}
