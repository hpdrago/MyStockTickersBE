package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Created by mike on 5/15/2016.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)  // 404
public class StockNoteSourceNotFoundException extends RuntimeException
{
    /**
     * PortfolioDE id not found
     * @param uuid
     */
    public StockNoteSourceNotFoundException( final String uuid )
    {
        super( "Stock note source id: " + uuid + " was not found" );
    }

    public StockNoteSourceNotFoundException( final UUID stockNoteSourceUuid )
    {
        this( stockNoteSourceUuid.toString() );
    }
}
