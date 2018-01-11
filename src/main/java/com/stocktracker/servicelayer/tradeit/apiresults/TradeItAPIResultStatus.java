package com.stocktracker.servicelayer.tradeit.apiresults;

public enum TradeItAPIResultStatus
{
    SUCCESS,
    INFORMATION_NEEDED,
    ERROR;

    public boolean isSuccess() { return this == SUCCESS; }
    public boolean isError() { return this == INFORMATION_NEEDED; }
    public boolean isInformationNeeded() { return this == ERROR; }
}
