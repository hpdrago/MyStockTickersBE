package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by mike on 12/7/2017.
 */
@ResponseStatus(value= HttpStatus.CONFLICT, reason="Duplicate analyst consensus")
public class DuplicateAnalystConsensusException extends RuntimeException
{
    public DuplicateAnalystConsensusException( final String tickerSymbol )
    {
        super( "Analyst consensus already exists for " + tickerSymbol );
    }
}
