package com.stocktracker.repositorylayer.entity;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table( name = "watch_list", schema = "stocktracker", catalog = "" )
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class WatchListEntity extends UUIDEntity
{
    private UUID customerUuid;
    private String name;
    private Collection<WatchListStockEntity> watchListStocksByUuid;

    @Basic
    @Column( name = "customer_uuid" )
    public UUID getCustomerUuid()
    {
        return customerUuid;
    }

    public void setCustomerUuid( final UUID customerUuid )
    {
        this.customerUuid = customerUuid;
    }

    @Basic
    @Column( name = "name" )
    public String getName()
    {
        return name;
    }

    public void setName( final String name )
    {
        this.name = name;
    }

    @OneToMany( mappedBy = "watchListByWatchListUuid" )
    public Collection<WatchListStockEntity> getWatchListStocksByUuid()
    {
        return watchListStocksByUuid;
    }

    public void setWatchListStocksByUuid( final Collection<WatchListStockEntity> watchListStocksByUuid )
    {
        this.watchListStocksByUuid = watchListStocksByUuid;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "WatchListEntity{" );
        sb.append( "customerUuid=" ).append( customerUuid );
        sb.append( ", name='" ).append( name ).append( '\'' );
        sb.append( ", stocks=" ).append( watchListStocksByUuid );
        sb.append( "}, super=" ).append( super.toString() );
        return sb.toString();
    }
}
