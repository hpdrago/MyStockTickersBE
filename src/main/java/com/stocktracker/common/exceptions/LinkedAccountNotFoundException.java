package com.stocktracker.common.exceptions;

import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * This exception is thrown when a linked account cannot be found.
 */
@ResponseStatus( value= HttpStatus.NOT_FOUND)  // 404
public class LinkedAccountNotFoundException extends AsyncCacheDataNotFoundException
{
    /**
     * Creates a new exception with an informational message.
     * @param accountNumber
     * @param tradeItAccountEntity
     */
    public LinkedAccountNotFoundException( final String accountNumber, final TradeItAccountEntity tradeItAccountEntity )
    {
        super( "Linked account number " + accountNumber + " was not found for account " +
               tradeItAccountEntity.getName() + " customer " + tradeItAccountEntity.getCustomerUuid() );
    }

    /**
     * Exception for the primary key not found.
     * @param linkedAccountUuid
     */
    public LinkedAccountNotFoundException( final UUID linkedAccountUuid )
    {
        super( "Linked account not found by primary key id " + linkedAccountUuid );
    }
}
