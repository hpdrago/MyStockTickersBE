package com.stocktracker.weblayer.dto.common;

/**
 * DTO's that contain a customer id (String UUID) implement this interface to enable conversion of the customer
 * UUID from String from/to binary.
 */
public interface CustomerIdContainer
{
    /**
     * Set the customer id (UUID)
     * @param customerId String version of the UUID.
     */
    void setCustomerId( final String customerId );

    /**
     * Get the customer id (UUID).
     * @return String version of the UUID.
     */
    String getCustomerId();
}
