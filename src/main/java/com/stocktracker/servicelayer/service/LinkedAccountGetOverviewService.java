package com.stocktracker.servicelayer.service;

import com.stocktracker.common.EntityRefreshStatus;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAuthenticationException;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.servicelayer.tradeit.TradeItService;
import com.stocktracker.weblayer.dto.tradeit.GetAccountOverviewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


/**
 * This service provides an asynchronous method to get the account overview information which is stored in the
 * {@code LinkedAccountEntity}.  The method calls TradeIt to get the account overview information and updates the
 * database.  Using this class makes the loading of the linked accounts faster on the front end such that the list of
 * accounts is returned quickly while the backend continues to work to load the overview information from TradeIt.
 */
@Service
public class LinkedAccountGetOverviewService
{
    private static final Logger log = LoggerFactory.getLogger( LinkedAccountGetOverviewService.class );
    private TradeItService tradeItService;
    private LinkedAccountEntityService linkedAccountEntityService;

    /**
     * This is an asynchronous method that will update the linked account with the account summary information from
     * TradeIt.
     * @param tradeItAccountEntity This contains the necessary auth token and user id in order to call TradeIt to
     *                             execute the API call to get the account summary information.
     * @param linkedAccountEntity This is the linked account that will be updated with the summary account information.
     */
    @Async("linkedAccountGetOverviewThreadPool")
    public void updateLinkedAccount( final TradeItAccountEntity tradeItAccountEntity,
                                     final LinkedAccountEntity linkedAccountEntity )
    {
        final String methodName = "updateLinkedAccount";
        log.debug( String.format( "%s %d %d", methodName, tradeItAccountEntity.getId(), linkedAccountEntity.getId() ));

        final GetAccountOverviewDTO getAccountOverviewDTO;
        try
        {
            try
            {
                getAccountOverviewDTO = this.tradeItService
                    .getAccountOverview( tradeItAccountEntity,
                                         linkedAccountEntity.getAccountNumber() );
                try
                {
                    linkedAccountEntity.setGetAccountOverviewValues( getAccountOverviewDTO );
                    linkedAccountEntityService
                        .updateGetAccountOverviewStatus( linkedAccountEntity, EntityRefreshStatus.UPDATED );
                }
                catch( EntityVersionMismatchException e )
                {
                    handleEntityMismatchException( linkedAccountEntity, getAccountOverviewDTO );
                }
            }
            catch( TradeItAuthenticationException e )
            {
                log.error( methodName, e );
                linkedAccountEntityService
                    .updateGetAccountOverviewStatus( linkedAccountEntity, EntityRefreshStatus.ERROR );
            }
            catch( LinkedAccountNotFoundException e1 )
            {
                // * this shouldn't happen...
                log.error( methodName, e1 );
                linkedAccountEntityService
                    .updateGetAccountOverviewStatus( linkedAccountEntity, EntityRefreshStatus.ERROR );
            }
        }
        catch( Exception e )
        {
            log.error( methodName, e );
        }
        log.debug( methodName + " " + tradeItAccountEntity.getId() );
    }

    /**
     * When an entity mismatch exception occurs we can handle this event by retrieving the current linked account
     * from the database and then performing the getAccountSummary update.
     * @param linkedAccountEntity
     * @param getAccountOverviewDTO
     * @throws LinkedAccountNotFoundException
     */
    private void handleEntityMismatchException( final LinkedAccountEntity linkedAccountEntity,
                                                final GetAccountOverviewDTO getAccountOverviewDTO )
        throws LinkedAccountNotFoundException
    {
        final String methodName = "handleEntityMismatchException";
        log.warn( String.format( "%s.begin Entity version mismatch for linked account %d",
                                 methodName, linkedAccountEntity.getId() ) );
        /*
         * Refresh the entity and try again.
         */
        final LinkedAccountEntity currentLinkedAccountEntity = this.linkedAccountEntityService
                                                                   .getLinkedAccountEntity( linkedAccountEntity.getCustomerId(),
                                                                                            linkedAccountEntity.getId() );
        try
        {
            linkedAccountEntity.setGetAccountOverviewValues( getAccountOverviewDTO );
            linkedAccountEntityService.updateGetAccountOverviewStatus( currentLinkedAccountEntity,
                                                                       EntityRefreshStatus.UPDATED );
            log.info( String.format( "%s Resolved entity mismatch for linked account %d",
                                     methodName, linkedAccountEntity.getId() ) );
        }
        catch( EntityVersionMismatchException e1 )
        {
            // * this shouldn't happen...
            log.error( String.format( "%s Entity version mismatch %d",
                                      methodName, linkedAccountEntity.getId() ), e1 );
        }
        log.warn( methodName + ".end" );
    }

    @Autowired
    public void setTradeItService( final TradeItService tradeItService )
    {
        this.tradeItService = tradeItService;
    }

    @Autowired
    public void setLinkedAccountEntityService( final LinkedAccountEntityService linkedAccountEntityService )
    {
        this.linkedAccountEntityService = linkedAccountEntityService;
    }

}
