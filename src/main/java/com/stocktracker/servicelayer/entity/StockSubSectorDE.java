package com.stocktracker.servicelayer.entity;

/**
 * Created by mike on 11/19/2016.
 */
public class StockSubSectorDE
{
    private Integer sectorId;
    private Integer subSectorId;
    private String subSector;

    public Integer getSectorId()
    {
        return sectorId;
    }

    public void setSectorId( final Integer sectorId )
    {
        this.sectorId = sectorId;
    }

    public Integer getSubSectorId()
    {
        return subSectorId;
    }

    public void setSubSectorId( final Integer subSectorId )
    {
        this.subSectorId = subSectorId;
    }

    public String getSubSector()
    {
        return subSector;
    }

    public void setSubSector( final String subSector )
    {
        this.subSector = subSector;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockSubSectorDE{" );
        sb.append( "sectorId=" ).append( sectorId );
        sb.append( ", subSectorId=" ).append( subSectorId );
        sb.append( ", subSector='" ).append( subSector ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }

}
