package com.stocktracker.weblayer.dto.common;


import java.io.Serializable;

public interface VersionedDTO<K extends Serializable>
{
    /**
     * Get the primary key id.
     * @return
     */
    K getId();
}
