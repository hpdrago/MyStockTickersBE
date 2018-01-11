package com.stocktracker.servicelayer.tradeit.types;

public class TradeItOrderCapability
{
    private String instrument;
    private TradeItDisplayLabelValue actions;
    private TradeItDisplayLabelValue priceTypes;
    private TradeItDisplayLabelValue expirationTypes;

    public String getInstrument()
    {
        return instrument;
    }

    public void setInstrument( String instrument )
    {
        this.instrument = instrument;
    }

    public TradeItDisplayLabelValue getActions()
    {
        return actions;
    }

    public void setActions( TradeItDisplayLabelValue actions )
    {
        this.actions = actions;
    }

    public TradeItDisplayLabelValue getPriceTypes()
    {
        return priceTypes;
    }

    public void setPriceTypes( TradeItDisplayLabelValue priceTypes )
    {
        this.priceTypes = priceTypes;
    }

    public TradeItDisplayLabelValue getExpirationTypes()
    {
        return expirationTypes;
    }

    public void setExpirationTypes( TradeItDisplayLabelValue expirationTypes )
    {
        this.expirationTypes = expirationTypes;
    }
}
