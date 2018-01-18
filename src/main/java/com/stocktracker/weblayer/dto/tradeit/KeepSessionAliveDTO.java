package com.stocktracker.weblayer.dto.tradeit;

import com.stocktracker.servicelayer.tradeit.apiresults.KeepSessionAliveAPIResult;

/**
 * The DTO returned when calling TradeIt to keep the session alive.
 */
public class KeepSessionAliveDTO extends KeepSessionAliveAPIResult
{
    /**
     * Creates a new instance with values copied from {@code keepSessionAliveAPIResult}.
     * @param keepSessionAliveAPIResult
     */
    public KeepSessionAliveDTO( final KeepSessionAliveAPIResult keepSessionAliveAPIResult )
    {
        super( keepSessionAliveAPIResult );
    }
}
