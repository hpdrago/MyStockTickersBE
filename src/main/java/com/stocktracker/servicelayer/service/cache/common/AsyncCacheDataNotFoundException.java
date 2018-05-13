package com.stocktracker.servicelayer.service.cache.common;

/**
 * This exception is used to indicate that retrieval of the required data failed.
 */
public class AsyncCacheDataNotFoundException extends Exception
{
    public AsyncCacheDataNotFoundException( final Object searchKey )
    {
        super( "Failed to find data to cache for search key: " + searchKey );
    }

    public AsyncCacheDataNotFoundException( final Object searchKey, final Throwable e )
    {
        super( "Failed to find data to cache for search key: " + searchKey, e );
    }
}
