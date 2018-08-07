package com.stocktracker.weblayer.dto;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stocktracker.common.JSONTimestampDateTimeSerializer;
import com.stocktracker.weblayer.dto.common.CustomerIdContainer;
import com.stocktracker.weblayer.dto.common.UuidDTO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * This class defines the data that will be sent to the client when requesting information for a Customer
 * Created by mike on 5/15/2016.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class TradeItAccountDTO implements UuidDTO, CustomerIdContainer
{
    private String id;
    private String name;
    private String customerId;
    private String brokerage;
    private boolean tradeItAccountFlag;
    private Timestamp authTimestamp;
    private Integer version;

    public String getId()
    {
        return this.id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( final String name )
    {
        this.name = name;
    }

    public String getBrokerage()
    {
        return brokerage;
    }

    public void setBrokerage( final String brokerage )
    {
        this.brokerage = brokerage;
    }

    @Override
    public void setCustomerId( final String customerId )
    {
        this.customerId = customerId;
    }

    @Override
    public String getCustomerId()
    {
        return this.customerId;
    }

    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    public Timestamp getAuthTimestamp()
    {
        return authTimestamp;
    }

    public void setAuthTimestamp( final Timestamp authTimestamp )
    {
        this.authTimestamp = authTimestamp;
    }

    public boolean isTradeItAccount()
    {
        return tradeItAccountFlag;
    }

    public boolean getTradeItAccount()
    {
        return tradeItAccountFlag;
    }

    public void setTradeItAccount( final boolean tradeItAccountFlag )
    {
        this.tradeItAccountFlag = tradeItAccountFlag;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion( final Integer version )
    {
        this.version = version;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "TradeItAccountDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", name='" ).append( name ).append( '\'' );
        sb.append( ", brokerage='" ).append( brokerage ).append( '\'' );
        sb.append( ", customerId='" ).append( customerId ).append( '\'' );
        sb.append( ", isTradeItAccount=" ).append( tradeItAccountFlag );
        sb.append( ", authTimestamp='" ).append( authTimestamp ).append( '\'' );
        sb.append( ", version=" ).append( version );
        sb.append( '}' );
        return sb.toString();
    }
}
