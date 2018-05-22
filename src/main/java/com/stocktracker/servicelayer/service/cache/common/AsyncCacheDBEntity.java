package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.repositorylayer.common.VersionedEntity;

import java.io.Serializable;

/**
 * @param <K> Key to the cache.
 */
public interface AsyncCacheDBEntity<K extends Serializable> extends VersionedEntity<K>, AsyncCacheData
{
}
