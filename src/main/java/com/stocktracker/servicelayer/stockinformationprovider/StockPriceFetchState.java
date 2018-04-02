package com.stocktracker.servicelayer.stockinformationprovider;

/**
 * This enum identifies the different states of the cache price stock entry.
 */
public enum StockPriceFetchState
{
    FETCHING,
    NOT_FETCHING;

    /**
     * Identifies if the stock price is currently being fetched.
     * @return
     */
    public boolean isFetching() { return this == FETCHING; }
}
