package com.stocktracker.weblayer.dto.tradeit;

import com.stocktracker.servicelayer.tradeit.apiresults.GetOAuthAccessTokenUpdateURLAPIResult;

/**
 * This is the DTO class to return to the client after making the TradeIt call to get the OAuth access token.
 */
public class GetOAuthAccessTokenUpdateURLDTO extends RequestOAuthPopUpURLDTO
{
    /**
     * Converts the API Result into a DTO.
     * @param GetOAuthAccessTokenUpdateURLAPIResult
     */
    public GetOAuthAccessTokenUpdateURLDTO( final GetOAuthAccessTokenUpdateURLAPIResult GetOAuthAccessTokenUpdateURLAPIResult )
    {
        super( GetOAuthAccessTokenUpdateURLAPIResult );
    }
}
