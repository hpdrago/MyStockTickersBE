package com.stocktracker.weblayer.dto;


import com.stocktracker.common.JSONDateConverter;

import java.sql.Timestamp;

/**
 * This class defines the data that will be sent to the client when requesting information for a Customer
 * Created by mike on 5/15/2016.
 */
public class AccountDTO
{
    private Integer id;
    private Integer customerId;
    private String name;
    private String brokerage;
    private String authTimestamp;

    /**
     * Creates a new instance from (@code accountEntity)
     * @return
     */
    public static final AccountDTO newInstance()
    {
        AccountDTO accountDTO = new AccountDTO();
        return accountDTO;
    }

    private AccountDTO()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( final Integer customerId )
    {
        this.customerId = customerId;
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

    public void setAuthTimestamp( final Timestamp authTimestamp )
    {
        if ( authTimestamp == null )
        {
            this.authTimestamp = null;
        }
        else
        {
            this.authTimestamp = JSONDateConverter.toDateAndTime( authTimestamp );
        }
    }

    public void setAuthTimestamp( final String authTimestamp )
    {
        this.authTimestamp = authTimestamp;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "AccountDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", name='" ).append( name ).append( '\'' );
        sb.append( ", brokerage='" ).append( brokerage ).append( '\'' );
        sb.append( ", authTimestamp='" ).append( authTimestamp ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
