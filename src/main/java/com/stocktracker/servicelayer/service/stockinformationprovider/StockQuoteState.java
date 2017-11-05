package com.stocktracker.servicelayer.service.stockinformationprovider;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * This enum defines the state of the stock quote returned from this cache.
 * This enum is only needed when the {@code StockQuoteFetchMode} is asynchronous.
 */
public enum StockQuoteState
{
    /**
     * The stock quote is up to date within the EXPIRATION_TIME
     */
    CURRENT,
    /**
     * The stock quote is stale and not within the EXPIRATION_TIME, but it does exist in the cache
     * and thus a quote is available.
     */
    STALE,
    /**
     * The stock quote is not in the cache and will be retrieved
     */
    NOT_CACHED,
    /**
     * The stock ticker symbol was not found
     */
    NOT_FOUND;

    public boolean isCurrent() { return this == CURRENT; }
    public boolean isStale() { return this == STALE; }
    public boolean isNotCached() { return this == NOT_CACHED; }
    public boolean isNotFound() { return this == NOT_FOUND; }


    @JsonValue
    public int toValue()
    {
        return ordinal();
    }

    public static StockQuoteState valueOf( final Integer stockQuoteState )
    {
        StockQuoteState returnValue = null;
        for ( int i = 0; i < values().length; i++ )
        {
            if ( values()[i].ordinal() == stockQuoteState  )
            {
                returnValue = values()[i];
            }
        }
        if ( returnValue == null )
        {
            throw new IllegalArgumentException( stockQuoteState + " is not a valid StockQuoteState ordinal value" );
        }
        return returnValue;
    }
}

