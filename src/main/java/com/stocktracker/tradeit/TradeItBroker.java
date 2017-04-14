package com.stocktracker.tradeit;

/**
 * Created by michael.earl on 4/14/2017.
 */
public enum TradeItBroker
{
    ETRADE( "Etrade", "E*Trade" ),
    FIDELITY( "Fidelity", "Fidelity" ),
    OPTIONS_HOUSE( "OptionsHouse", "OptionsHouse" ),
    ROBIN_HOOD( "Robinhood", "Robinhood" ),
    SCOTTRADE( "Scottrade", "Scottrade" ),
    TD_AMERITRADE( "TD", "TD Ameritrade" ),
    TRADE_STATION( "TradeStation", "TradeStation" ),
    TRADIER( "Tradier", "Tradier Brokerage" ),
    TRADE_KINg( "Trade King", "Trade King" ),
    FOREX( "FOREX.com", "FOREX.com" );

    private String shortName;
    private String longName;

    TradeItBroker( final String shortName, final String longName )
    {
        this.setShortName( shortName );
        this.setLongName( longName );
    }

    public String getShortName()
    {
        return shortName;
    }

    public void setShortName( String shortName )
    {
        this.shortName = shortName;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName( String longName )
    {
        this.longName = longName;
    }
}
