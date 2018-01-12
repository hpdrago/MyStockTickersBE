package com.stocktracker.weblayer.dto.tradeit;

import com.stocktracker.servicelayer.tradeit.apiresults.GetBrokersAPIResult;

/**
 * DTO wrapper for the TradeIt GetBrokersAPIResult.
 */
public class GetBrokersDTO extends GetBrokersAPIResult
{
    public GetBrokersDTO( final GetBrokersAPIResult getBrokersAPIResult )
    {
        super( getBrokersAPIResult );
    }
}
