package com.stocktracker.servicelayer.tradeit.apiresults;

/**
 * This class defines the return result from a close session request.
 * Only the standard TradeIt API results are needed as there are no custom values returned from this call.
 */
public class CloseSessionAPIResult extends TradeItAPIResult<CloseSessionAPIResult>
{
    /**
     * Creates a new instance with values copied from {@code keepSessionAliveAPIResult}.
     * @param keepSessionAliveAPIResult
     */
    public CloseSessionAPIResult( final CloseSessionAPIResult keepSessionAliveAPIResult )
    {
        super( keepSessionAliveAPIResult );
    }
}
