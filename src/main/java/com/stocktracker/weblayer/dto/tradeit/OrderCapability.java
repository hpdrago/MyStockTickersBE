package com.stocktracker.weblayer.dto.tradeit;

public class OrderCapability
{
    private String instrument;
    private DisplayLabelValue actions;
    private DisplayLabelValue priceTypes;
    private DisplayLabelValue expirationTypes;

    public String getInstrument()
    {
        return instrument;
    }

    public void setInstrument( String instrument )
    {
        this.instrument = instrument;
    }

    public DisplayLabelValue getActions()
    {
        return actions;
    }

    public void setActions( DisplayLabelValue actions )
    {
        this.actions = actions;
    }

    public DisplayLabelValue getPriceTypes()
    {
        return priceTypes;
    }

    public void setPriceTypes( DisplayLabelValue priceTypes )
    {
        this.priceTypes = priceTypes;
    }

    public DisplayLabelValue getExpirationTypes()
    {
        return expirationTypes;
    }

    public void setExpirationTypes( DisplayLabelValue expirationTypes )
    {
        this.expirationTypes = expirationTypes;
    }
}
