package com.stocktracker.servicelayer.service.stockinformationprovider;

/**
 * This enum defines how this class will behave when a stock quote is not found in the cache or found in the cache
 * but is stale (CachedStockEntry.lastQuoteRefreshTime > EXPIRATION_TIME)
 */
public enum StockQuoteFetchMode
{
    SYNCHRONOUS,
    ASYNCHRONOUS;

    public boolean isSynchronous()
    {
        return this == SYNCHRONOUS;
    }

    public boolean isASynchronous()
    {
        return this == ASYNCHRONOUS;
    }
    }

