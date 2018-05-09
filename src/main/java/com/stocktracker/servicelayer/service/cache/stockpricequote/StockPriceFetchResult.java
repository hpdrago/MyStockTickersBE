package com.stocktracker.servicelayer.service.cache.stockpricequote;

/**
 * This enum is used to identify the results of getting a stock price from IEXTrading or Yahoo.
 */
public enum StockPriceFetchResult
{
    SUCCESS,
    DISCONTINUED,
    NOT_FOUND,
    EXCEPTION;

    public boolean isDiscontinued()
    {
        return this == DISCONTINUED;
    }

    public boolean isNotFound() { return this == NOT_FOUND; }

    public boolean isSuccess()
    {
        return this == SUCCESS;
    }

    public boolean isException()
    {
        return this == EXCEPTION;
    }
}
