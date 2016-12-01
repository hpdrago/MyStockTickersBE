package com.stocktracker.servicelayer.entity;

/**
 * Created by mike on 11/19/2016.
 */
public class StockSectorDE
{
    private Integer id;
    private String sector;

    public Integer getId()
    {
        return id;
    }

    public void setId( final Integer id )
    {
        this.id = id;
    }

    public String getSector()
    {
        return sector;
    }

    public void setSector( final String sector )
    {
        this.sector = sector;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockSectorDE{" );
        sb.append( "id=" ).append( id );
        sb.append( ", sector='" ).append( sector ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
