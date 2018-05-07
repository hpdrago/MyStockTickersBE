package com.stocktracker.weblayer.dto.common;


import java.io.Serializable;

public interface VersionedDTO<K extends Serializable>
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
     * @param version
     */
    void setVersion( final Integer version );
}
