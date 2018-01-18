package com.stocktracker.servicelayer.tradeit.apiresults;

/**
 * This class contains the values returned from a keepSessionAlive API call.
 * There are no custom values returned from this call so the standard TradeIt result values are sufficient.
 */
public class KeepSessionAliveAPIResult extends TradeItAPIResult
{
    /**
     * Create a new instance with values from {@code keepSessionAliveAPIResult}
     * @param keepSessionAliveAPIResult
     */
    public KeepSessionAliveAPIResult( final KeepSessionAliveAPIResult keepSessionAliveAPIResult )
    {
        super( keepSessionAliveAPIResult );
    }
}
