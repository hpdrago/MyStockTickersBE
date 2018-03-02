package com.stocktracker.common;

/**
 * Generic enum to identify the state of asynchronous database updates.
 */
public enum EntityRefreshStatus
{
    UPDATING,
    UPDATED,
    ERROR
}
