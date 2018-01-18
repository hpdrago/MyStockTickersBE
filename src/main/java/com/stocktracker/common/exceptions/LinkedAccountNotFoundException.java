package com.stocktracker.common.exceptions;

import com.stocktracker.repositorylayer.entity.AccountEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when a linked account cannot be found.
 */
@ResponseStatus( value= HttpStatus.NOT_FOUND)  // 404
public class LinkedAccountNotFoundException extends Exception
{
    /**
     * Creates a new exception with an informational message.
     * @param accountNumber
     * @param accountEntity
     */
    public LinkedAccountNotFoundException( final String accountNumber, final AccountEntity accountEntity )
    {
        super( "Linked account number " + accountNumber + " was not found for account " +
               accountEntity.getName() + " customer " + accountEntity.getCustomerId() );
    }
}
