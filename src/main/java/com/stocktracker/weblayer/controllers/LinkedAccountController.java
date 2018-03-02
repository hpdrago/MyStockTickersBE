package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAccountNotFoundException;
import com.stocktracker.servicelayer.service.LinkedAccountEntityService;
import com.stocktracker.common.exceptions.TradeItAuthenticationException;
import com.stocktracker.weblayer.dto.LinkedAccountDTO;
import com.stocktracker.weblayer.dto.tradeit.GetAccountOverviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
public class LinkedAccountController extends AbstractController
{
    private static final String CONTEXT_URL = "/linkedAccount";
    private LinkedAccountEntityService linkedAccountEntityService;

    /**
     * Save the linked account
     * @param linkedAccountId
     * @param customerId
     * @param linkedAccountDTO
     * @return
     * @throws EntityVersionMismatchException
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/id/{linkedAccountId}/customer/{customerId}",
                     method = RequestMethod.PUT )
    public ResponseEntity<LinkedAccountDTO> saveLinkedAccount( @PathVariable int linkedAccountId,
                                                               @PathVariable int customerId,
                                                               @RequestBody LinkedAccountDTO linkedAccountDTO )
        throws EntityVersionMismatchException
    {
        final String methodName = "saveLinkedAccount";
        logMethodBegin( methodName, customerId, linkedAccountId );
        /*
         * Save the Account
         */
        LinkedAccountDTO returnLinkedAccountDTO = this.linkedAccountEntityService
                                                      .saveDTO( linkedAccountDTO );
        /*
         * send the response
         */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( returnLinkedAccountDTO ).toUri());
        logMethodEnd( methodName, returnLinkedAccountDTO );
        return new ResponseEntity<>( returnLinkedAccountDTO, httpHeaders, HttpStatus.CREATED );
    }

    /**
     * Get all of the accounts linked to the brokerage account registered at TradeIt.
     * @param tradeItAccountId
     * @param customerId
     * @return
     * @throws LinkedAccountNotFoundException
     * @throws TradeItAccountNotFoundException
     * @throws EntityVersionMismatchException
     */
    @RequestMapping( value = CONTEXT_URL +
                             "/tradeItAccountId/{tradeItAccountId}" +
                             "/customer/{customerId}",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<LinkedAccountDTO> getLinkedAccounts( final @PathVariable int tradeItAccountId,
                                                     final @PathVariable int customerId )
        throws TradeItAccountNotFoundException,
               EntityVersionMismatchException,
               LinkedAccountNotFoundException
    {
        final String methodName = "getLinkedAccounts";
        logMethodBegin( methodName, tradeItAccountId, customerId );
        List<LinkedAccountDTO> accounts = this.linkedAccountEntityService
                                              .getLinkedAccounts( customerId, tradeItAccountId );
        logMethodEnd( methodName, "accounts size: " + accounts.size() );
        return accounts;
    }

    /**
     * Calls TradeIt to get the account overview for a single brokerage account.
     * @param linkedAccountId
     * @param tradeItAccountId
     * @param customerId
     * @return
     * @throws TradeItAccountNotFoundException
     * @throws TradeItAuthenticationException
     * @throws LinkedAccountNotFoundException
     */
    /*
    @RequestMapping( value = CONTEXT_URL + "/getAccountOverview"
                             + "/linkedAccountId/{linkedAccountId}"
                             + "/tradeItAccountId/{tradeItAccountId}"
                             + "/customer/{customerId}",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public GetAccountOverviewDTO getAccountOverview( @PathVariable final int linkedAccountId,
                                                     @PathVariable final int tradeItAccountId,
                                                     @PathVariable final int customerId )
        throws LinkedAccountNotFoundException,
               TradeItAccountNotFoundException,
               TradeItAuthenticationException

    {
        final String methodName = "getAccountOverview";
        logMethodBegin( methodName, linkedAccountId, tradeItAccountId, customerId );
        final GetAccountOverviewDTO getAccountOverviewDTO = this.linkedAccountEntityService
                                                                .getAccountOverview( customerId, tradeItAccountId, linkedAccountId );
        logMethodEnd( methodName, getAccountOverviewDTO );
        return getAccountOverviewDTO;
    }
*/
    @Autowired
    public void setTradeItAccountService( final LinkedAccountEntityService linkedAccountEntityService )
    {
        this.linkedAccountEntityService = linkedAccountEntityService;
    }

}
