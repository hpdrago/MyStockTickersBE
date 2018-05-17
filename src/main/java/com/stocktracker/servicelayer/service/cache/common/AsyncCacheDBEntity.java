package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.repositorylayer.common.VersionedEntity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Get the epxiration date/time of the entity.
 * @param <K>
 */
public interface AsyncCacheDBEntity<K extends Serializable> extends VersionedEntity<K>
{
    /**
     * Determine the expiration date/time of the entity.
     * @return
     */
    Timestamp getExpiration();
}
