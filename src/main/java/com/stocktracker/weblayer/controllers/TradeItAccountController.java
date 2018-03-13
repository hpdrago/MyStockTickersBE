package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.TradeItAccountNotFoundException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.servicelayer.service.TradeItAccountEntityService;
import com.stocktracker.weblayer.dto.TradeItAccountDTO;
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
 * This class contains all of the TradeItAccountDTO related REST weblayer service call mapping and handling
 * <p>
 * Created by mike on 5/9/2016.
 */
@RestController
@CrossOrigin
public class TradeItAccountController extends AbstractController implements MyLogger
{
    private static final String CONTEXT_URL = "/tradeItAccount";
    private TradeItAccountEntityService tradeItAccountService;

    @Autowired
    public void setTradeItAccountService( final TradeItAccountEntityService tradeItAccountService )
    {
        this.tradeItAccountService = tradeItAccountService;
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
    public TradeItAccountDTO getAccount( @PathVariable int accountId,
                                         @PathVariable int customerId )
        throws TradeItAccountNotFoundException
    {
        final String methodName = "getAccount";
        logMethodBegin( methodName, accountId );
        TradeItAccountDTO tradeItAccountDTO = tradeItAccountService.getAccountDTO( customerId, accountId );
        logMethodEnd( methodName, tradeItAccountDTO );
        return tradeItAccountDTO;
    }

    /**
     * Add the account to the database
     *
     * @return The account that was added
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/customer/{customerId}",
                     method = RequestMethod.POST )
    public ResponseEntity<TradeItAccountDTO> createAccount( @RequestBody final TradeItAccountDTO tradeItAccountDTO,
                                                            @PathVariable final int customerId )
        throws EntityVersionMismatchException
    {
        final String methodName = "createAccount";
        logMethodBegin( methodName, customerId, tradeItAccountDTO );
        TradeItAccountDTO returnStockDTO = this.tradeItAccountService
                                               .createAccount( customerId, tradeItAccountDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( tradeItAccountDTO ).toUri());
        logMethodEnd( methodName, returnStockDTO );
        return new ResponseEntity<>( returnStockDTO, httpHeaders, HttpStatus.CREATED );
    }

    /**
     * Deletes an account
     * @param customerId
     * @param accountId
     * @return
     * @throws TradeItAccountNotFoundException
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{accountId}/customer/{customerId}",
                     method = RequestMethod.DELETE,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Void> deleteAccount( @PathVariable int accountId,
                                               @PathVariable int customerId )
        throws TradeItAccountNotFoundException
    {
        final String methodName = "deleteAccount";
        logMethodBegin( methodName, accountId, customerId );
        try
        {
            this.tradeItAccountService.deleteEntity( accountId );
        }
        catch( VersionedEntityNotFoundException e )
        {
            throw new TradeItAccountNotFoundException( accountId );
        }
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Save the account
     * @param customerId
     * @param tradeItAccountDTO
     * @return
     * @throws EntityVersionMismatchException
     * @throws TradeItAccountNotFoundException
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/customer/{customerId}",
                     method = RequestMethod.PUT )
    public ResponseEntity<TradeItAccountDTO> saveAccount( @PathVariable int customerId,
                                                          @RequestBody TradeItAccountDTO tradeItAccountDTO )
        throws EntityVersionMismatchException
    {
        final String methodName = "saveAccount";
        logMethodBegin( methodName, customerId, tradeItAccountDTO );
        TradeItAccountDTO returnTradeItAccountDTO = this.tradeItAccountService
                                                        .saveDTO( tradeItAccountDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( returnTradeItAccountDTO ).toUri());
        logMethodEnd( methodName, returnTradeItAccountDTO );
        return new ResponseEntity<>( returnTradeItAccountDTO, httpHeaders, HttpStatus.CREATED );
    }

    /**
     * Get all of the customer accounts that are registered with TradeIt.
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/customer/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<TradeItAccountDTO> getAccounts( final @PathVariable int customerId )
    {
        final String methodName = "getAccounts";
        logMethodBegin( methodName, customerId );
        List<TradeItAccountDTO> accounts = this.tradeItAccountService
                                               .getAccounts( customerId );
        logMethodEnd( methodName, "accounts size: " + accounts.size() );
        return accounts;
    }
}
