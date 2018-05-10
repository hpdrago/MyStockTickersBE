package com.stocktracker.servicelayer.service.cache.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * This enum defines the state of the stock quote returned from this cache.
 * This enum is only needed when the {@code AsyncCacheFetchMode} is asynchronous.
 */
public enum AsyncCacheEntryState
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
     * The stock ticker symbol was not found
     */
    NOT_FOUND,
    /**
     * Identifies when there are issues updating the stock quote
     */
    FAILURE;

    public boolean isCurrent() { return this == CURRENT; }
    public boolean isStale() { return this == STALE; }
    public boolean isNotFound() { return this == NOT_FOUND; }


    @JsonValue
    public int toValue()
    {
        return ordinal();
    }

    public static AsyncCacheEntryState valueOf( final Integer stockQuoteState )
    {
        AsyncCacheEntryState returnValue = null;
        for ( int i = 0; i < values().length; i++ )
        {
            if ( values()[i].ordinal() == stockQuoteState  )
            {
                returnValue = values()[i];
            }
        }
        if ( returnValue == null )
        {
            throw new IllegalArgumentException( stockQuoteState + " is not a valid AsyncCacheEntryState ordinal value" );
        }
        return returnValue;
    }
}

