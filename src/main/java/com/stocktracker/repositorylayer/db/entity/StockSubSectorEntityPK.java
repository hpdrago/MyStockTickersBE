package com.stocktracker.repositorylayer.db.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by mike on 11/20/2016.
 */
public class StockSubSectorEntityPK implements Serializable
{
    private Integer sectorId;
    private Integer subSectorId;

    @Column( name = "sector_id", nullable = false )
    @Id
    public Integer getSectorId()
    {
        return sectorId;
    }

    public void setSectorId( final Integer sectorId )
    {
        this.sectorId = sectorId;
    }

    @Column( name = "sub_sector_id", nullable = false )
    @Id
    public Integer getSubSectorId()
    {
        return subSectorId;
    }

    public void setSubSectorId( final Integer subSectorId )
    {
        this.subSectorId = subSectorId;
    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        final StockSubSectorEntityPK that = (StockSubSectorEntityPK) o;
        return Objects.equals( sectorId, that.sectorId ) &&
            Objects.equals( subSectorId, that.subSectorId );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( sectorId, subSectorId );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockSubSectorEntityPK{" );
        sb.append( "sectorId=" ).append( sectorId );
        sb.append( ", subSectorId=" ).append( subSectorId );
        sb.append( '}' );
        return sb.toString();
    }
}
