package com.stocktracker.weblayer.dto.tradeit;

import com.stocktracker.servicelayer.tradeit.apiresults.GetOAuthAccessTokenAPIResult;
import com.stocktracker.servicelayer.tradeit.apiresults.TradeItAPIResult;
import com.stocktracker.weblayer.dto.TradeItAccountDTO;

/**
 * This is the DTO class to return to the client after making the TradeIt call to get the OAuth access token.
 */
public class GetOAuthAccessTokenDTO extends TradeItAPIResult
{
    private TradeItAccountDTO tradeItAccount;

    public GetOAuthAccessTokenDTO()
    {
    }

    public GetOAuthAccessTokenDTO( final GetOAuthAccessTokenAPIResult getOAuthAccessTokenAPIResult )
    {
        super( getOAuthAccessTokenAPIResult );
    }

    public TradeItAccountDTO getTradeItAccount()
    {
        return tradeItAccount;
    }

    public void setTradeItAccount( final TradeItAccountDTO customerTradeItAccountDTO )
    {
        this.tradeItAccount = customerTradeItAccountDTO;
    }
}
