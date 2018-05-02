package com.stocktracker.weblayer.controllers;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAuthenticationException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.servicelayer.service.LinkedAccountEntityService;
import com.stocktracker.weblayer.dto.LinkedAccountDTO;
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
     * Delete the linked account
     * @param linkedAccountId
     * @param customerId
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/id/{linkedAccountId}/customerId/{customerId}",
                     method = RequestMethod.DELETE )
    public ResponseEntity<Void> deleteLinkedAccount( @PathVariable String linkedAccountId,
                                                     @PathVariable String customerId )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "deleteLinkedAccount";
        logMethodBegin( methodName, linkedAccountId, customerId );
        this.linkedAccountEntityService.deleteEntity( UUIDUtil.uuid( linkedAccountId ));
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }
    /**
     * Save the linked account
     * @param linkedAccountId
     * @param customerId
     * @param linkedAccountDTO
     * @return
     * @throws EntityVersionMismatchException
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/id/{linkedAccountId}/customerId/{customerId}",
                     method = RequestMethod.PUT )
    public ResponseEntity<LinkedAccountDTO> saveLinkedAccount( @PathVariable String linkedAccountId,
                                                               @PathVariable String customerId,
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
     * @return
     * @throws TradeItAccountNotFoundException
     */
    @RequestMapping( value = CONTEXT_URL +
                             "/tradeItAccountId/{tradeItAccountId}" +
                             "/customerId/{customerId}",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<LinkedAccountDTO> getLinkedAccounts( final @PathVariable String tradeItAccountId,
                                                     final @PathVariable String customerId )
        throws TradeItAccountNotFoundException
    {
        final String methodName = "getLinkedAccounts";
        logMethodBegin( methodName, tradeItAccountId, customerId );
        List<LinkedAccountDTO> accounts = this.linkedAccountEntityService
                                              .getLinkedAccounts( UUIDUtil.uuid( tradeItAccountId ));
        logMethodEnd( methodName, "accounts size: " + accounts.size() );
        return accounts;
    }

    /**
     * This is the subsequent call made after a call to {@code getLinkedAccounts}.
     * Obtaining the get account overview information for linked account is an asynchronous process.  When linked accounts
     * are selected from the database, each account is then asynchronously updated by calling the TradeIt get account
     * summary API call.  This effort is done in parallel so that the user can see the linked accounts as quick as possible
     * and while the initial request to get the requests makes it back to the client the linked accounts are being updated
     * in the background.  This method is then called by the client to get the updated (get account overview) information
     * for each linked account.  When called, this method will block until the account information has been updated.
     * @param linkedAccountId
     * @param tradeItAccountId
     * @param customerId
     * @return
     * @throws TradeItAccountNotFoundException
     * @throws TradeItAuthenticationException
     * @throws LinkedAccountNotFoundException
     */
    @RequestMapping( value = CONTEXT_URL + "/getAccountOverview"
                             + "/id/{linkedAccountId}"
                             + "/tradeItAccountId/{tradeItAccountId}"
                             + "/customerId/{customerId}",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public LinkedAccountDTO getUpdatedLinkedAccount( @PathVariable final String linkedAccountId,
                                                     @PathVariable final String tradeItAccountId,
                                                     @PathVariable final String customerId )
    {
        final String methodName = "getUpdatedLinkedAccount";
        logMethodBegin( methodName, linkedAccountId, tradeItAccountId, customerId );
        final LinkedAccountDTO linkedAccountDTO = this.linkedAccountEntityService
                                                      .getUpdatedLinkedAccount( UUIDUtil.uuid( linkedAccountId ) );
        logMethodEnd( methodName, linkedAccountDTO );
        return linkedAccountDTO;
    }

    @Autowired
    public void setTradeItAccountService( final LinkedAccountEntityService linkedAccountEntityService )
    {
        this.linkedAccountEntityService = linkedAccountEntityService;
    }
}
