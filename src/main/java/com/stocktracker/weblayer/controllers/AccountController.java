package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.service.AccountService;
import com.stocktracker.weblayer.dto.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
     * Get all of the accounts
     *
     * @return
     */
    /*
    @RequestMapping( value = CONTEXT_URL,
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<AccountDTO> getAccounts()
    {
        final String methodName = "getAccounts";
        logMethodBegin( methodName );
        List<AccountDTO> accountDTOs = accountService.getAllAccounts();
        logMethodEnd( methodName, accountDTOs );
        return accountDTOs;
    }
    */

    /**
     * Get the account by the account id
     *
     * @param id
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/{id}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public AccountDTO getAccount( @PathVariable int id )
    {
        final String methodName = "getAccount";
        logMethodBegin( methodName, id );
        AccountDTO accountDTO = accountService.getAccountById( id );
        logMethodEnd( methodName, accountDTO );
        return accountDTO;
    }

}
