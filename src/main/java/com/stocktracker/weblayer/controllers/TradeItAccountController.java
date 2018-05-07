package com.stocktracker.weblayer.controllers;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.CustomerNotFoundException;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.NotAuthorizedException;
import com.stocktracker.common.exceptions.TradeItAccountNotFoundException;
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
import java.util.UUID;


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
     * @throws TradeItAccountNotFoundException
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{accountId}/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public TradeItAccountDTO getAccount( @PathVariable String accountId,
                                         @PathVariable String customerId )
        throws CustomerNotFoundException,
               NotAuthorizedException,
               VersionedEntityNotFoundException
    {
        final String methodName = "getAccount";
        logMethodBegin( methodName, accountId, customerId );
        this.validateCustomerId( customerId );
        final TradeItAccountDTO tradeItAccountDTO = this.tradeItAccountService
                                                        .getDTO( UUIDUtil.uuid( accountId ));
        logMethodEnd( methodName, tradeItAccountDTO );
        return tradeItAccountDTO;
    }

    /**
     * Add the account to the database
     *
     * @return The account that was added
     * @throws EntityVersionMismatchException
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/customerId/{customerId}",
                     method = RequestMethod.POST )
    public ResponseEntity<TradeItAccountDTO> createAccount( @RequestBody final TradeItAccountDTO tradeItAccountDTO,
                                                            @PathVariable final String customerId )
        throws EntityVersionMismatchException,
               CustomerNotFoundException,
               NotAuthorizedException, DuplicateEntityException
    {
        final String methodName = "createAccount";
        logMethodBegin( methodName, customerId, tradeItAccountDTO );
        this.validateCustomerId( customerId );
        TradeItAccountDTO returnStockDTO = this.tradeItAccountService
                                               .addDTO( tradeItAccountDTO );
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
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{accountId}/customerId/{customerId}",
                     method = RequestMethod.DELETE,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Void> deleteAccount( @PathVariable String accountId,
                                               @PathVariable String customerId )
        throws TradeItAccountNotFoundException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "deleteAccount";
        logMethodBegin( methodName, accountId, customerId );
        this.validateCustomerId( customerId );
        try
        {
            this.tradeItAccountService.deleteEntity( UUIDUtil.uuid( accountId ));
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
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/customerId/{customerId}",
                     method = RequestMethod.PUT )
    public ResponseEntity<TradeItAccountDTO> saveAccount( @PathVariable String customerId,
                                                          @RequestBody TradeItAccountDTO tradeItAccountDTO )
        throws EntityVersionMismatchException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "saveAccount";
        logMethodBegin( methodName, customerId, tradeItAccountDTO );
        this.validateCustomerId( customerId );
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
     * @throws NotAuthorizedException
     * @throws CustomerNotFoundException
     */
    @RequestMapping( value = CONTEXT_URL + "/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<TradeItAccountDTO> getAccounts( final @PathVariable String customerId )
        throws CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "getAccounts";
        logMethodBegin( methodName, customerId );
        final UUID customerUuid = this.validateCustomerId( customerId );
        List<TradeItAccountDTO> accounts = this.tradeItAccountService
                                               .getAccounts( customerUuid );
        logMethodEnd( methodName, "accounts size: " + accounts.size() );
        return accounts;
    }
}
