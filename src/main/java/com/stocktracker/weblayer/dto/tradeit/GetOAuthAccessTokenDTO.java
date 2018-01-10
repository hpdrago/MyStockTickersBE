package com.stocktracker.weblayer.dto.tradeit;

import com.stocktracker.servicelayer.tradeit.apiresults.GetOAuthAccessTokenAPIResult;
import com.stocktracker.servicelayer.tradeit.apiresults.TradeItAPIResult;
import com.stocktracker.weblayer.dto.AccountDTO;

/**
 * This is the DTO class to return to the client after making the TradeIt call to get the OAuth access token.
 */
public class GetOAuthAccessTokenDTO extends TradeItAPIResult
{
    private AccountDTO customerAccount;

    public GetOAuthAccessTokenDTO( final GetOAuthAccessTokenAPIResult getOAuthAccessTokenAPIResult )
    {
        super( getOAuthAccessTokenAPIResult );
    }

    public AccountDTO getCustomerAccount()
    {
        return customerAccount;
    }

    public void setCustomerAccount( final AccountDTO customerAccountDTO )
    {
        this.customerAccount = customerAccountDTO;
    }
}
