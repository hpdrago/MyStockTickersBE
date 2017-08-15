package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by mike on 5/15/2016.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)  // 404
public class StockNoteSourceNotFoundException extends RuntimeException
{
    /**
     * PortfolioDE id not found
     * @param id
     */
    public StockNoteSourceNotFoundException( final int id )
    {
        super( "Stock note source id: " + id + " was not found" );
    }

}
