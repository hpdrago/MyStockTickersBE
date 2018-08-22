package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value= HttpStatus.UNPROCESSABLE_ENTITY)  // 422
public class TradeItAccountTokenExpiredException extends Exception
{
    public TradeItAccountTokenExpiredException( final String tradeItAccountId )
    {
        super( "Trade it account " + tradeItAccountId + " has an expired token" );
    }
}
