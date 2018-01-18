package com.stocktracker.weblayer.dto.tradeit;


import com.stocktracker.servicelayer.tradeit.apiresults.CloseSessionAPIResult;

public class CloseSessionDTO extends CloseSessionAPIResult
{
    public CloseSessionDTO( final CloseSessionAPIResult keepSessionAliveAPIResult )
    {
        super( keepSessionAliveAPIResult );
    }
}
