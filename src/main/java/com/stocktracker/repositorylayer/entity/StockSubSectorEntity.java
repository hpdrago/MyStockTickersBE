package com.stocktracker.repositorylayer.entity;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Created by mike on 11/20/2016.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "stock_sub_sector", schema = "stocktracker", catalog = "" )
@IdClass( StockSubSectorEntityPK.class )
public class StockSubSectorEntity
{
    private Integer sectorId;
    private Integer subSectorId;
    private String subSector;

    @Id
    @Column( name = "sector_id", nullable = false )
    public Integer getSectorId()
    {
        return sectorId;
    }

    public void setSectorId( final Integer sectorId )
    {
        this.sectorId = sectorId;
    }

    @Id
    @Column( name = "sub_sector_id", nullable = false )
    public Integer getSubSectorId()
    {
        return subSectorId;
    }

    public void setSubSectorId( final Integer subSectorId )
    {
        this.subSectorId = subSectorId;
    }

    @Basic
    @Column( name = "sub_sector", nullable = false, length = 30 )
    public String getSubSector()
    {
        return subSector;
    }

    public void setSubSector( final String subSector )
    {
        this.subSector = subSector;
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
        final StockSubSectorEntity that = (StockSubSectorEntity) o;
        return Objects.equals( sectorId, that.sectorId ) &&
            Objects.equals( subSectorId, that.subSectorId ) &&
            Objects.equals( subSector, that.subSector );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( sectorId, subSectorId, subSector );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockSubSectorEntity{" );
        sb.append( "sectorId=" ).append( sectorId );
        sb.append( ", subSectorId=" ).append( subSectorId );
        sb.append( ", subSector='" ).append( subSector ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
