package com.stocktracker.servicelayer.tradeit.types;

import com.stocktracker.weblayer.dto.CustomerIdContainer;

/**
 * This class defines the account fields that are returned in TradeIt authenticate call.
 */
public class TradeItAccount implements CustomerIdContainer
{
    private String accountNumber;
    private String customerId;
    private String name;
    private String accountBaseCurrency;
    private String accountIndex;
    private boolean userCanDisableMargin;
    private TradeItOrderCapability[] orderCapabilities;

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public void setAccountNumber( String accountNumber )
    {
        this.accountNumber = accountNumber;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getAccountBaseCurrency()
    {
        return accountBaseCurrency;
    }

    public void setAccountBaseCurrency( String accountBaseCurrency )
    {
        this.accountBaseCurrency = accountBaseCurrency;
    }

    public String getAccountIndex()
    {
        return accountIndex;
    }

    public void setAccountIndex( String accountIndex )
    {
        this.accountIndex = accountIndex;
    }

    public boolean isUserCanDisableMargin()
    {
        return userCanDisableMargin;
    }

    public void setUserCanDisableMargin( boolean userCanDisableMargin )
    {
        this.userCanDisableMargin = userCanDisableMargin;
    }

    public TradeItOrderCapability[] getOrderCapabilities()
    {
        return orderCapabilities;
    }

    public void setOrderCapabilities( TradeItOrderCapability[] orderCapabilities )
    {
        this.orderCapabilities = orderCapabilities;
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

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "TradeItAccountDTO{" );
        sb.append( "accountNumber='" ).append( accountNumber ).append( '\'' );
        sb.append( ", name='" ).append( name ).append( '\'' );
        sb.append( ", customerId='" ).append( customerId ).append( '\'' );
        sb.append( ", accountBaseCurrency='" ).append( accountBaseCurrency ).append( '\'' );
        sb.append( ", accountIndex='" ).append( accountIndex ).append( '\'' );
        sb.append( ", userCanDisableMargin=" ).append( userCanDisableMargin );
        //sb.append( ", orderCapabilities=" ).append( Arrays.toString( orderCapabilities ) );
        sb.append( '}' );
        return sb.toString();
    }
}
