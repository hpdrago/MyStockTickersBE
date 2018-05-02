package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Created by mike on 5/15/2016.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)  // 404
public class PortfolioNotFoundException extends RuntimeException
{
    /**
     * PortfolioDE id not found
     * @param id
     */
    public PortfolioNotFoundException( final int id )
    {
        super( "PortfolioDE id: " + id + " was not found" );
    }

    /**
     * PortfolioDE portfolioName not found
     * @param portfolioName
     */
    public PortfolioNotFoundException( final String portfolioName )
    {
        super( "PortfolioDE portfolioName: " + portfolioName + " was not found" );
    }

    public PortfolioNotFoundException( final UUID portfolioUuid )
    {
        this( portfolioUuid.toString() );
    }
}
