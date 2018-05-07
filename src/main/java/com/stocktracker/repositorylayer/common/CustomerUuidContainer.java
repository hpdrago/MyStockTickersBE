package com.stocktracker.repositorylayer.common;

import java.util.UUID;

/**
 * Entities that contain a customer_uuid column value implement this interface to enable conversion from String
 * and binary UUIDs.
 */
public interface CustomerUuidContainer
{
    /**
     * Get the customer UUID.
     * @return
     */
    UUID getCustomerUuid();

    /**
     * Set the customer UUID.
     * @param customerUuid
     */
    void setCustomerUuid( final UUID customerUuid );
}
