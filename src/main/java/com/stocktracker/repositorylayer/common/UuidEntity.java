package com.stocktracker.repositorylayer.common;

import java.util.UUID;

/**
 * This interface identifies entity classes as having and using a UUID as the primary key.
 * Also contains the necessary methods for uuid based JPA entities.
 */
public interface UuidEntity extends VersionedEntity<UUID>
{
    UUID getUuid();
    void setUuid( final UUID id );
}
