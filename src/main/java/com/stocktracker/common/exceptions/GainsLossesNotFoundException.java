package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus( value= HttpStatus.NOT_FOUND)  // 404
public class GainsLossesNotFoundException extends Throwable
{
    public GainsLossesNotFoundException( final String stockToBuyId )
    {
        super( "Gains/Losses was not found for id: " + stockToBuyId );
    }

    public GainsLossesNotFoundException( final UUID stockToBuyUuid )
    {
        this( stockToBuyUuid.toString() );
    }

    public GainsLossesNotFoundException( final String stockToBuyId, final VersionedEntityNotFoundException e )
    {
        super( "Gains/losses was not found for id: " + stockToBuyId, e );
    }
}
