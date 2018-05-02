package com.stocktracker.weblayer.dto;

/**
 * This inteface identifies DTOs that have an String id that contains a UUID.
 */
public interface UuidDTO extends VersionedDTO<String>
{
    void setId( final String id );
}
