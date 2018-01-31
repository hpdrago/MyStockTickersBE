package com.stocktracker.servicelayer.tradeit.apiresults;

/**
 * This class contains the values returned from a keepSessionAlive API call.
 * There are no custom values returned from this call so the standard TradeIt result values are sufficient.
 */
public class KeepSessionAliveAPIResult<T extends KeepSessionAliveAPIResult<T>> extends TradeItAPIResult<T>
{
    /**
     * Default constructor.
     */
    public KeepSessionAliveAPIResult()
    {
    }
}
