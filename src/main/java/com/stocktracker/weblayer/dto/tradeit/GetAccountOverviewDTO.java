package com.stocktracker.weblayer.dto.tradeit;

import com.stocktracker.servicelayer.tradeit.apiresults.GetAccountOverViewAPIResult;

/**
 * This DTO is returned when retrieving the account overview for an account.
 */
public class GetAccountOverviewDTO extends GetAccountOverViewAPIResult
{
    /**
     * Create a new instance with values from {@code getAccountOverviewAPIResult}
     * @param getAccountOverViewAPIResult
     */
    public GetAccountOverviewDTO( final GetAccountOverViewAPIResult getAccountOverViewAPIResult )
    {
        super( getAccountOverViewAPIResult );
    }
}
