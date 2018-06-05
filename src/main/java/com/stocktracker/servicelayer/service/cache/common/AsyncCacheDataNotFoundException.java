package com.stocktracker.servicelayer.service.cache.common;

/**
 * This exception is used to indicate that retrieval of the required data failed.
 */
public abstract class AsyncCacheDataNotFoundException extends Exception
{
    public AsyncCacheDataNotFoundException( final String message )
    {
        super( message );
    }

    public AsyncCacheDataNotFoundException( final String message, final Exception e )
    {
        super( message, e );
    }
}
