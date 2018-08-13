package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAPIException;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.servicelayer.tradeit.TradeItService;
import com.stocktracker.servicelayer.tradeit.apiresults.GetAccountOverviewAPIResult;
import io.reactivex.Observable;
import io.reactivex.subjects.AsyncSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * This service provides an asynchronous method to get the account overview information which is stored in the
 * {@code LinkedAccountEntity}.  The method calls TradeIt to get the account overview information and updates the
 * database.  Using this class makes the loading of the linked accounts faster on the front end such that the list of
 * accounts is returned quickly while the backend continues to work to load the overview information from TradeIt.
 */
@Service
public class TradeItAsyncUpdateService
{
    private static final Logger log = LoggerFactory.getLogger( TradeItAsyncUpdateService.class );
    private static final Map<UUID, AsyncSubject<LinkedAccountEntity>> getAccountOverviewSubjectMap =
        Collections.synchronizedMap( new HashMap<>() );

    @Autowired
    private TradeItService tradeItService;
    @Autowired
    private LinkedAccountEntityService linkedAccountEntityService;

    /**
     * This is an asynchronous method that will update the linked account with the account summary information from
     * TradeIt.
     * @param tradeItAccountEntity This contains the necessary auth token and user id in order to call TradeIt to
     *                             execute the API call to get the account summary information.
     * @param linkedAccountEntity This is the linked account that will be updated with the summary account information.
     */
    public void updateLinkedAccount( final TradeItAccountEntity tradeItAccountEntity,
                                     final LinkedAccountEntity linkedAccountEntity )
    {
        final String methodName = "updateLinkedAccount";
        log.debug( String.format( "%s.begin %s %s", methodName, tradeItAccountEntity.getUuid(),
                                  linkedAccountEntity.getUuid() ));
        try
        {
            final GetAccountOverviewAPIResult getAccountOverviewDTO = this.tradeItService
                                                                    .getAccountOverview( tradeItAccountEntity,
                                                                                         linkedAccountEntity.getAccountNumber() );
            if ( getAccountOverviewDTO.isSuccessful() )
            {
                linkedAccountEntity.setGetAccountOverviewValues( getAccountOverviewDTO );
                try
                {
                    /*
                     * Update the map first so that it's available now to the expected call from the UI.
                     */
                    this.onGetAccountOverviewSuccess( linkedAccountEntity );
                    this.linkedAccountEntityService
                        .saveEntity( linkedAccountEntity );
                }
                catch( EntityVersionMismatchException e )
                {
                    this.handleEntityMismatchException( linkedAccountEntity, getAccountOverviewDTO );
                }
            }
            else
            {
                TradeItAPIException tradeItAPIException = new TradeItAPIException( getAccountOverviewDTO );
                this.onGetAccountOverviewException( linkedAccountEntity.getUuid(), tradeItAPIException );
            }
        }
        catch( Exception e )
        {
            log.error( methodName, e );
            this.onGetAccountOverviewException( linkedAccountEntity.getUuid(), e );
        }
        log.debug( methodName + ".end " + tradeItAccountEntity.getUuid() );
    }

    /**
     * When an entity mismatch exception occurs we can handle this event by retrieving the current linked account
     * from the database and then performing the getAccountSummary update.
     * @param linkedAccountEntity
     * @param getAccountOverviewDTO
     * @throws LinkedAccountNotFoundException
     */
    private void handleEntityMismatchException( final LinkedAccountEntity linkedAccountEntity,
                                                final GetAccountOverviewAPIResult getAccountOverviewDTO )
        throws LinkedAccountNotFoundException
    {
        final String methodName = "handleEntityMismatchException";
        log.warn( String.format( "%s.begin Entity version mismatch for linked account %s",
                                 methodName, linkedAccountEntity.getUuid() ) );
        /*
         * Refresh the entity and try again.
         */
        final LinkedAccountEntity currentLinkedAccountEntity = this.linkedAccountEntityService
                                                                   .getLinkedAccountEntity( linkedAccountEntity.getUuid() );
        try
        {
            currentLinkedAccountEntity.setGetAccountOverviewValues( getAccountOverviewDTO );
            this.linkedAccountEntityService
                .saveEntity( currentLinkedAccountEntity );
            log.info( String.format( "%s Resolved entity mismatch for linked account %s",
                                     methodName, linkedAccountEntity.getUuid() ) );
        }
        catch( EntityVersionMismatchException e1 )
        {
            // * this shouldn't happen...
            log.error( String.format( "%s Entity version mismatch %s",
                                      methodName, linkedAccountEntity.getUuid() ), e1 );
            this.onGetAccountOverviewException( linkedAccountEntity.getUuid(), e1 );
        }
        catch( DuplicateEntityException e )
        {
            log.error( String.format( "%s Duplicate entity exception %s",
                                      methodName, linkedAccountEntity.getUuid() ), e );
            this.onGetAccountOverviewException( linkedAccountEntity.getUuid(), e );
        }
        log.warn( methodName + ".end" );
    }

    /**
     * Notifies the observer that an exception occurred.
     * @param linkedAccountUuid
     * @param exception
     */
    private void onGetAccountOverviewException( final UUID linkedAccountUuid,
                                                final Exception exception )
    {
        final String methodName = "onGetAccountOverviewException";
        log.debug( String.format( "%s.begin linkedAccountId: %s", methodName, linkedAccountUuid ));
        final AsyncSubject<LinkedAccountEntity> getAccountOverviewSubject = this.getAccountOverviewSubjectMap
                                                                                .get( linkedAccountUuid );
        getAccountOverviewSubject.onError( exception );
        getAccountOverviewSubject.onComplete();
        log.debug( String.format( "%s.end linkedAccountId: %s", methodName, linkedAccountUuid ));
    }

    /**
     * This method will obtain the async subject for the {@code linkedAccountId} and notify any current or future
     * observers  that the get account overview process has completed.
     * @param linkedAccountEntity the updated linked account
     */
    private void onGetAccountOverviewSuccess( final LinkedAccountEntity linkedAccountEntity )
    {
        final String methodName = "onGetAccountOverviewSuccess";
        log.debug( String.format( "%s.begin linkedAccountId: %s", methodName, linkedAccountEntity.getUuid() ) );
        final AsyncSubject<LinkedAccountEntity> getAccountOverviewSubject = this.getAccountOverviewSubjectMap
                                                                                .get( linkedAccountEntity.getUuid() );
        getAccountOverviewSubject.onNext( linkedAccountEntity );
        getAccountOverviewSubject.onComplete();
        log.debug( String.format( "%s.end linkedAccountId: %s", methodName, linkedAccountEntity.getUuid() ) );
    }
}
