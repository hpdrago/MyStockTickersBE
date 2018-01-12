package com.stocktracker.servicelayer.tradeit.apiresults;

/**
 * This enum contains the various result status values returned from TradeIt API calls.
 */
public enum TradeItAPIResultStatus
{
    SUCCESS,
    INFORMATION_NEEDED,
    ERROR;

    public boolean isSuccess() { return this == SUCCESS; }
    public boolean isError() { return this == ERROR; }
    public boolean isInformationNeeded() { return this == INFORMATION_NEEDED; }
}
