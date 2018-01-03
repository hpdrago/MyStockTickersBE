package com.stocktracker.weblayer.dto.tradeit;

import com.stocktracker.weblayer.dto.AccountDTO;

/**
 * This is the DTO class to return to the client after making the TradeIt call to get the OAuth access token.
 */
public class GetOAuthAccessTokenDTO extends TradeItAPIResult
{
    private AccountDTO customerAccount;

    public GetOAuthAccessTokenDTO( final GetOAuthAccessToken getOAuthAccessToken )
    {
        super( getOAuthAccessToken );
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
