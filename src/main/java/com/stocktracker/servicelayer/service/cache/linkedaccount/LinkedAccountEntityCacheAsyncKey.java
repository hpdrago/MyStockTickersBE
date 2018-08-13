package com.stocktracker.servicelayer.service.cache.linkedaccount;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class LinkedAccountEntityCacheAsyncKey
{
    private String accountNumber;
    private UUID tradeItAccountUuid;
    private UUID linkedAccountUuid;

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public void setAccountNumber( final String accountNumber )
    {
        this.accountNumber = accountNumber;
    }

    public UUID getTradeItAccountUuid()
    {
        return tradeItAccountUuid;
    }

    public void setTradeItAccountUuid( final UUID tradeItAccountUuid )
    {
        this.tradeItAccountUuid = tradeItAccountUuid;
    }

    public UUID getLinkedAccountUuid()
    {
        return linkedAccountUuid;
    }

    public void setLinkedAccountUuid( final UUID linkedAccountUuid )
    {
        this.linkedAccountUuid = linkedAccountUuid;
    }


    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "LinkedAccountEntityCacheAsyncKey{" );
        sb.append( "accountNumber='" ).append( accountNumber ).append( '\'' );
        sb.append( ", tradeItAccountUuid=" ).append( tradeItAccountUuid );
        sb.append( '}' );
        return sb.toString();
    }
}
