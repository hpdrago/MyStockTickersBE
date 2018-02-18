package com.stocktracker.repositorylayer.entity;

/**
 * Defines the necessary methods for an entity that maintains a version for each table entity row.
 * @param <K>
 */
public interface VersionedEntity<K>
{
    /**
     * Get the primary key id.
     * @return
     */
    K getId();

    /**
     * Get the entity version.
     * @return
     */
    Integer getVersion();

    /**
     * Set the entity version.
     * @param i
     */
    void setVersion( final Integer version );
}
