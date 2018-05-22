package com.stocktracker.servicelayer.service.cache.common;

import java.sql.Timestamp;

public interface AsyncCacheData
{
    /**
     * Determine the expiration date/time of the entity.
     * @return
     */
    Timestamp getExpiration();
}
