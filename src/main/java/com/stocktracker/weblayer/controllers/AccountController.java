package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.AccountNotFoundException;
import com.stocktracker.servicelayer.service.AccountService;
import com.stocktracker.weblayer.dto.AccountDTO;
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


/**
 * This class contains all of the AccountDTO related REST weblayer service call mapping and handling
 * <p>
 * Created by mike on 5/9/2016.
 */
@RestController
@CrossOrigin
public class AccountController extends AbstractController implements MyLogger
{
    private static final String CONTEXT_URL = "/account";
    private AccountService accountService;

    @Autowired
    public void setAccountService( final AccountService accountService )
    {
        this.accountService = accountService;
    }

    /**
     * Get the account by the account id
     *
     * @param accountId
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{accountId}/customer/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public AccountDTO getAccount( @PathVariable int accountId,
                                  @PathVariable int customerId )
    {
        final String methodName = "getAccount";
        logMethodBegin( methodName, accountId );
        AccountDTO accountDTO = accountService.getAccountDTO( customerId, accountId );
        logMethodEnd( methodName, accountDTO );
        return accountDTO;
    }

    /**
     * Add the account to the database
     *
     * @return The account that was added
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/customer/{customerId}",
                     method = RequestMethod.POST )
    public ResponseEntity<AccountDTO> createAccount( @RequestBody final AccountDTO accountDTO,
                                                     @PathVariable final int customerId )
        throws AccountNotFoundException
    {
        final String methodName = "createAccount";
        logMethodBegin( methodName, customerId, accountDTO );
        AccountDTO returnStockDTO = this.accountService.createAccount( customerId, accountDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( accountDTO ).toUri());
        logMethodEnd( methodName, returnStockDTO );
        return new ResponseEntity<>( returnStockDTO, httpHeaders, HttpStatus.CREATED );
    }

    /**
     * Deletes an account
     * @param customerId
     * @param accountId
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{accountId}/customer/{customerId}",
                     method = RequestMethod.DELETE,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Void> deleteAccount( @PathVariable int accountId,
                                               @PathVariable int customerId )
    {
        final String methodName = "deleteAccount";
        logMethodBegin( methodName, accountId, customerId );
        this.accountService.deleteAccount( customerId, accountId );
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Save the account
     * @param customerId
     * @param accountDTO
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/customer/{customerId}",
                     method = RequestMethod.PUT )
    public ResponseEntity<AccountDTO> saveAccount( @PathVariable int customerId,
                                                   @RequestBody AccountDTO accountDTO )
    {
        final String methodName = "saveAccount";
        logMethodBegin( methodName, customerId, accountDTO );
        AccountDTO returnAccountDTO = this.accountService.updateAccount( customerId, accountDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( returnAccountDTO ).toUri());
        logMethodEnd( methodName, returnAccountDTO );
        return new ResponseEntity<>( returnAccountDTO, httpHeaders, HttpStatus.CREATED );
    }

    /**
     * Get all of the stock to buy for a customer and a
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/customer/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<AccountDTO> getCustomerAccounts( final @PathVariable int customerId )
    {
        final String methodName = "getCustomerAccounts";
        logMethodBegin( methodName, customerId );
        List<AccountDTO> accounts = this.accountService.getAccounts( customerId );
        logMethodEnd( methodName, "accounts size: " + accounts.size() );
        return accounts;
    }
}
