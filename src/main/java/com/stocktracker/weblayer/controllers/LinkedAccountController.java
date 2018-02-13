package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.servicelayer.service.LinkedAccountEntityService;
import com.stocktracker.weblayer.dto.LinkedAccountDTO;
import com.stocktracker.weblayer.dto.tradeit.GetAccountOverviewDTO;
import com.stocktracker.weblayer.dto.tradeit.GetPositionsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Linked accounts are child accounts to the TradeIt account.  Here's how it works, a user's login to a brokerage account
 * can allow him access to multiple accounts -- these are the linked accounts.  The TradeIt account is the account registered
 * with TradeIt but is a "reference" to the main brokerage account.  This controller manages REST queries for the
 * linked accounts.
 *
 * @author michael.earl 1/19/2018
 */
@RestController
@CrossOrigin
public class LinkedAccountController extends AbstractController implements MyLogger
{
    private static final String CONTEXT_URL = "/linkedAccount";
    private LinkedAccountEntityService linkedAccountEntityService;

    /**
     * Get all of the accounts linked to the brokerage account registered at TradeIt.
     * @return
     */
    @RequestMapping( value = CONTEXT_URL +
                             "/tradeItAccountId/{tradeItAccountId}" +
                             "/customer/{customerId}",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<LinkedAccountDTO> getLinkedAccounts( final @PathVariable int tradeItAccountId,
                                                     final @PathVariable int customerId )
    {
        final String methodName = "getLinkedAccounts";
        logMethodBegin( methodName, tradeItAccountId, customerId );
        List<LinkedAccountDTO> accounts = this.linkedAccountEntityService.getLinkedAccounts( customerId, tradeItAccountId );
        logMethodEnd( methodName, "accounts size: " + accounts.size() );
        return accounts;
    }

    /**
     * Get all of the positions for a linked account.
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/positions"
                             + "/linkedAccountId/{linkedAccountId}"
                             + "/tradeItAccountId/{tradeItAccountId}"
                             + "/customer/{customerId}",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public GetPositionsDTO getPositions( final @PathVariable int linkedAccountId,
                                         final @PathVariable int tradeItAccountId,
                                         final @PathVariable int customerId )
        throws LinkedAccountNotFoundException
    {
        final String methodName = "getPositions";
        logMethodBegin( methodName, linkedAccountId, tradeItAccountId, customerId );
        final GetPositionsDTO getPositionsDTO = this.linkedAccountEntityService
                                                    .getPositions( customerId, tradeItAccountId, linkedAccountId );
        logMethodEnd( methodName, "positions size: " + getPositionsDTO.size() );
        return getPositionsDTO;
    }

    /**
     * Calls TradeIt to get the account overview for a single brokerage account.
     * @param linkedAccountId
     * @param tradeItAccountId
     * @param customerId
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/getAccountOverview"
                             + "/linkedAccountId/{linkedAccountId}"
                             + "/tradeItAccountId/{tradeItAccountId}"
                             + "/customer/{customerId}",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public GetAccountOverviewDTO getAccountOverview( @PathVariable final int linkedAccountId,
                                                     @PathVariable final int tradeItAccountId,
                                                     @PathVariable final int customerId )
        throws LinkedAccountNotFoundException

    {
        final String methodName = "getAccountOverview";
        logMethodBegin( methodName, linkedAccountId, tradeItAccountId, customerId );
        final GetAccountOverviewDTO getAccountOverviewDTO = this.linkedAccountEntityService
                                                                .getAccountOverview( customerId, tradeItAccountId, linkedAccountId );
        logMethodEnd( methodName, getAccountOverviewDTO );
        return getAccountOverviewDTO;
    }

    @Autowired
    public void setTradeItAccountService( final LinkedAccountEntityService linkedAccountEntityService )
    {
        this.linkedAccountEntityService = linkedAccountEntityService;
    }

}
