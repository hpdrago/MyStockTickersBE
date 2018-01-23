package com.stocktracker.servicelayer.tradeit.types;

/**
 * This class contains a the TradeIt Display Label Value fields.
 */
public class TradeItDisplayLabelValue
{
    private String displayLabel;
    private String value;

    public String getDisplayLabel()
    {
        return displayLabel;
    }

    public void setDisplayLabel( String displayLabel )
    {
        this.displayLabel = displayLabel;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "TradeItDisplayLabelValue{" );
        sb.append( "displayLabel='" ).append( displayLabel ).append( '\'' );
        sb.append( ", value='" ).append( value ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
