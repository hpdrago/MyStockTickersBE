package com.stocktracker.weblayer.dto.tradeit;

import com.stocktracker.servicelayer.tradeit.apiresults.GetPositionsAPIResult;

public class GetPositionsDTO extends GetPositionsAPIResult
{
    public GetPositionsDTO( final GetPositionsAPIResult getPositionsAPIResult )
    {
        super( getPositionsAPIResult );
    }
}
