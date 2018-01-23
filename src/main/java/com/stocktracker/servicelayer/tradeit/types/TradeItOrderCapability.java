package com.stocktracker.servicelayer.tradeit.types;

import java.util.Arrays;

public class TradeItOrderCapability
{
    private String instrument;
    private String tradeItSymbol;
    private boolean symbolSpecific;
    private TradeItDisplayLabelValue actions[];
    private TradeItDisplayLabelValue priceTypes[];
    private TradeItDisplayLabelValue expirationTypes[];

    public String getInstrument()
    {
        return instrument;
    }

    public void setInstrument( String instrument )
    {
        this.instrument = instrument;
    }

    public TradeItDisplayLabelValue[] getActions()
    {
        return actions;
    }

    public void setActions( TradeItDisplayLabelValue actions[] )
    {
        this.actions = actions;
    }

    public TradeItDisplayLabelValue[] getPriceTypes()
    {
        return priceTypes;
    }

    public void setPriceTypes( TradeItDisplayLabelValue priceTypes[] )
    {
        this.priceTypes = priceTypes;
    }

    public TradeItDisplayLabelValue[] getExpirationTypes()
    {
        return expirationTypes;
    }

    public void setExpirationTypes( TradeItDisplayLabelValue expirationTypes[] )
    {
        this.expirationTypes = expirationTypes;
    }

    public String getTradeItSymbol()
    {
        return tradeItSymbol;
    }

    public void setTradeItSymbol( String tradeItSymbol )
    {
        this.tradeItSymbol = tradeItSymbol;
    }

    public boolean isSymbolSpecific()
    {
        return symbolSpecific;
    }

    public void setSymbolSpecific( boolean symbolSpecific )
    {
        this.symbolSpecific = symbolSpecific;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "TradeItOrderCapability{" );
        sb.append( "instrument='" ).append( instrument ).append( '\'' );
        sb.append( ", tradeItSymbol='" ).append( tradeItSymbol ).append( '\'' );
        sb.append( ", symbolSpecific=" ).append( symbolSpecific );
        sb.append( ", actions=" ).append( Arrays.toString( actions ) );
        sb.append( ", priceTypes=" ).append( Arrays.toString( priceTypes ) );
        sb.append( ", expirationTypes=" ).append( Arrays.toString( expirationTypes ) );
        sb.append( '}' );
        return sb.toString();
    }
}
