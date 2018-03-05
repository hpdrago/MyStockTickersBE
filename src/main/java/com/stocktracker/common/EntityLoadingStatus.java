package com.stocktracker.common;

/**
 * Status of loading entities.  Most entities will be LOADED but entities that are loaded asynchronously will be
 * in a LOADING status until they are completed loaded.
 */
public enum EntityLoadingStatus
{
    LOADING,
    LOADED
}
