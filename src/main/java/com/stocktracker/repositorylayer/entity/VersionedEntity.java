package com.stocktracker.repositorylayer.entity;

/**
 * Defines the necessary methods for an entity that maintains a version for each table entity row.
 * @param <K>
 */
public interface VersionedEntity<K>
{
    K getId();
    Integer getVersion();
}
