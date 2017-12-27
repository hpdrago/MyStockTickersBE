package com.stocktracker.weblayer.dto.tradeit;

public class TradeItBrokerInstrument
{
    private String instrument;
    private Boolean supportsAccountOverview;
    private Boolean supportsPositions;
    private Boolean supportsTrading;
    private Boolean supportsOrderStatus;
    private Boolean supportsTransactionHistory;
    private Boolean supportsOrderCanceling;
    private Boolean supportsFxRates;
    private Boolean isFeatured;

    public Boolean getSupportsAccountOverview()
    {
        return supportsAccountOverview;
    }

    public void setSupportsAccountOverview( final Boolean supportsAccountOverview )
    {
        this.supportsAccountOverview = supportsAccountOverview;
    }

    public Boolean getSupportsPositions()
    {
        return supportsPositions;
    }

    public void setSupportsPositions( final Boolean supportsPositions )
    {
        this.supportsPositions = supportsPositions;
    }

    public Boolean getSupportsTrading()
    {
        return supportsTrading;
    }

    public void setSupportsTrading( final Boolean supportsTrading )
    {
        this.supportsTrading = supportsTrading;
    }

    public Boolean getSupportsOrderStatus()
    {
        return supportsOrderStatus;
    }

    public void setSupportsOrderStatus( final Boolean supportsOrderStatus )
    {
        this.supportsOrderStatus = supportsOrderStatus;
    }

    public Boolean getSupportsTransactionHistory()
    {
        return supportsTransactionHistory;
    }

    public void setSupportsTransactionHistory( final Boolean supportsTransactionHistory )
    {
        this.supportsTransactionHistory = supportsTransactionHistory;
    }

    public Boolean getSupportsOrderCanceling()
    {
        return supportsOrderCanceling;
    }

    public void setSupportsOrderCanceling( final Boolean supportsOrderCanceling )
    {
        this.supportsOrderCanceling = supportsOrderCanceling;
    }

    public Boolean getSupportsFxRates()
    {
        return supportsFxRates;
    }

    public void setSupportsFxRates( final Boolean supportsFxRates )
    {
        this.supportsFxRates = supportsFxRates;
    }

    public Boolean getFeatured()
    {
        return isFeatured;
    }

    public void setFeatured( final Boolean featured )
    {
        isFeatured = featured;
    }

    public String getInstrument()
    {
        return instrument;
    }

    public void setInstrument( final String instrument )
    {
        this.instrument = instrument;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "TradeitBrokerInstrument{" );
        sb.append( "instrument='" ).append( instrument ).append( '\'' );
        sb.append( ", supportsAccountOverview=" ).append( supportsAccountOverview );
        sb.append( ", supportsPositions=" ).append( supportsPositions );
        sb.append( ", supportsTrading=" ).append( supportsTrading );
        sb.append( ", supportsOrderStatus=" ).append( supportsOrderStatus );
        sb.append( ", supportsTransactionHistory=" ).append( supportsTransactionHistory );
        sb.append( ", supportsOrderCanceling=" ).append( supportsOrderCanceling );
        sb.append( ", supportsFxRates=" ).append( supportsFxRates );
        sb.append( ", isFeatured=" ).append( isFeatured );
        sb.append( '}' );
        return sb.toString();
    }
}
