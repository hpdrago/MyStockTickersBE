package com.stocktracker.repositorylayer.common;

import java.sql.Timestamp;

/**
 * Defines the necessary methods for an entity that maintains a version for each table entity row.
 * @param <K> The database key.
 */
public interface VersionedEntity<K>
{
    /**
     * Get the primary key id.
     * @return
     */
    K getId();

    /**
     * Set the id.
     * @param id
     */
    void setId( K id );

    /**
     * Get the entity version.
     * @return
     */
    Integer getVersion();

    /**
     * Set the entity version.
     * @param version
     */
    void setVersion( final Integer version );

    /**
     * Set the create date.
     * @param createDate
     */
    void setCreateDate( final Timestamp createDate );

}
