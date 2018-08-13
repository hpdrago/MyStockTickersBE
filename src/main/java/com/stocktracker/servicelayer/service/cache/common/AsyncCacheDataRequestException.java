package com.stocktracker.servicelayer.service.cache.common;

/**
 * This exception is thrown when an exception occurs while requesting information from the asynchronous data source.
 */
public class AsyncCacheDataRequestException extends Exception
{
    public AsyncCacheDataRequestException( final Object asyncKey, final Exception sourceException )
    {
        super( "Error encountered requesting asynchronous data for " + asyncKey, sourceException );
    }
}
