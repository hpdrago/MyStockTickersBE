package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.common.VersionedEntity;
import com.stocktracker.servicelayer.service.BaseService;
import com.stocktracker.servicelayer.service.VersionedEntityService;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Objects;

import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.CURRENT;
import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.STALE;

/**
 * Abstract class that implements the common pattern of obtaining a value from a AsyncCache.
 * @param  <T> Cached data type.
 * @param  <K> Key type for cached data.
 * @param <CE> Cache Entry Type.
 * @param  <X> Cache Executor Type
 * @param  <C> Cache type.
 * @param <DR> Data Receiver.  The type that will receive the cached data
 * @param  <S> The entity service.
 */
public abstract class AsyncCacheDBEntityClient< K extends Serializable,
                                                T extends VersionedEntity<K>,
                                               CE extends AsyncCacheEntry<T>,
                                                X extends AsyncCacheServiceExecutor<K,T>,
                                                C extends AsyncCache<K,T,CE,X>,
                                               DR extends AsyncCacheDataReceiver<K,T>,
                                                S extends VersionedEntityService<K,T,?,?,?>>
    extends BaseService
    implements MyLogger
{
    /**
     * Working with the cache, and the entity service, attempts to retrieve the data from the database.  If it is not
     * found or if the data is not current, an asynchronous fetch will be performed.  If the data is found and is current,
     * the {@code receiver} is updated with the entity information.
     * @param receiver
     */
    public void setCachedData( final DR receiver )
    {
        final String methodName = "setCachedData";
        final K entityKey = receiver.getEntityKey();
        logMethodBegin( methodName, entityKey );
        Objects.requireNonNull( receiver, "receiver argument cannon be null" );
        Objects.requireNonNull( receiver.getEntityKey(), "receiver's entity key cannot be null" );
        try
        {
            /*
             * Retrieve the entity from the database, it might not exist.
             */
            final T entity = this.getEntityService()
                                 .getEntity( receiver.getEntityKey() );
            receiver.setCachedData( entity );
            /*
             * Check to see if the entity needs to be refreshed.
             */
            if ( this.isCurrent( entity ))
            {
                receiver.setCacheDataState( CURRENT );
            }
            else
            {
                receiver.setCacheDataState( STALE );
                /*
                 * The entity is STALE and need to be refreshed.  This is the job for the cache to perform this work
                 * asynchronously.
                 */
                this.asynchronousFetch( receiver, entity );
            }
        }
        catch( VersionedEntityNotFoundException e )
        {
            logDebug( methodName, "{0} was was not found in the database, fetching new data", entityKey );
            this.asynchronousFetch( receiver );
        }
        logMethodEnd( methodName, receiver );
    }

    /**
     * Fetches the data asynchronously and updates the receiver with the cache state and stale data values from
     * {@code entity}.
     * @param receiver
     * @param entity
     */
    protected void asynchronousFetch( final DR receiver, final T entity )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, receiver, entity );
        final CE cacheEntry = this.getCache()
                                  .asynchronousGet( receiver.getEntityKey(), entity );
        receiver.setCacheDataState( cacheEntry.getCacheState() );
        receiver.setCachedData( cacheEntry.getCachedData() );
        logMethodEnd( methodName, receiver );
    }

    /**
     * This method is called when no data exists and we need to cache to fetch the data.
     * @param receiver
     */
    protected void asynchronousFetch( final DR receiver )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, receiver );
        final CE cacheEntry = this.getCache()
                                  .asynchronousGet( receiver.getEntityKey() );
        receiver.setCacheDataState( cacheEntry.getCacheState() );
        receiver.setCachedData( cacheEntry.getCachedData() );
        logMethodEnd( methodName, receiver );
    }

    /**
     * Determines if the entity is current ie. not stale.
     * @param entity
     * @return
     */
    protected abstract boolean isCurrent( final T entity );

    /**
     * Get the cache entry
     * @param searchKey
     * @return null if the entry is not found which means {@code searchKey} is not cached.
     */
    protected CE getCacheEntry( final K searchKey )
    {
        return this.getCache()
                   .getCacheEntry( searchKey );
    }

    /**
     * Get the cached data for a search key.  It is assumed that an attempt has been made to determine if the cached
     * entry exists first.
     * @param searchKey
     * @return
     */
    public T getCachedData( final K searchKey )
    {
        final String methodName = "getCachedData";
        logMethodBegin( methodName, searchKey );
        T cachedData = this.createCachedDataObject();
        final CE cacheEntry = this.getCache()
                                  .getCacheEntry( searchKey );
        if ( cacheEntry != null )
        {
            if ( cacheEntry.getFetchState().isFetching() )
            {
                final T finalCachedData = cachedData;
                cacheEntry.getFetchSubject()
                          .blockingSubscribe( fetchedData ->
                                              {
                                                  if ( fetchedData != null )
                                                  {
                                                      BeanUtils.copyProperties( fetchedData, finalCachedData );
                                                  }
                                                  else
                                                  {
                                                      /*
                                                       * need to figure out what to do here, no one will see this.
                                                       */
                                                      throw new AsyncCacheDataNotFoundException( searchKey );
                                                  }
                                              });
            }
            else
            {
                BeanUtils.copyProperties( cacheEntry.getCachedData(), cachedData );
            }
        }
        else
        {
            logDebug( methodName, searchKey + " is not in the cache, fetching now" );
            cachedData = this.getCache()
                             .synchronousGet( searchKey )
                             .getCachedData();
        }
        logMethodEnd( methodName, cachedData );
        return cachedData;
    }

    /**
     * Get the cache instance.
     * @return
     */
    protected abstract C getCache();

    /**
     * Create an instance of the data type that will be cached.
     * @return
     */
    protected abstract T createCachedDataObject();

    /**
     * Get the instance of the entity service.
     * @return
     */
    protected abstract S getEntityService();
}
