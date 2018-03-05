package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.servicelayer.tradeit.TradeItService;
import com.stocktracker.weblayer.dto.tradeit.GetAccountOverviewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.subjects.AsyncSubject;

import java.util.HashMap;
import java.util.Map;

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
    private TradeItService tradeItService;
    private LinkedAccountEntityService linkedAccountEntityService;
    private Map<Integer, AsyncSubject<LinkedAccountEntity>> getAccountOverViewSubjectMap = new HashMap<>();

    /**
     * Creates and registeres a subject for each linked account that will be updated.
     * @param linkedAccountEntity
     */
    public void prepareToUpdateLinkedAccount( final LinkedAccountEntity linkedAccountEntity )
    {
        this.getAccountOverViewSubjectMap.put( linkedAccountEntity.getId(), AsyncSubject.create() );
    }

    /**
     * Register to receive the getAccountOverview result when it has completed.
     * @param linkedAccountId The account to wait for.
     * @return
     */
    public Observable<LinkedAccountEntity> subscribeToGetAccountOverview( final Integer linkedAccountId )
    {
        this.checkGetAccountOverviewExists( linkedAccountId );
        return this.getAccountOverViewSubjectMap
                   .get( linkedAccountId )
                   .asObservable();
    }

    /**
     * Checks that a request has been made for the {@code linkedAccountId}
     * @param linkedAccountId
     * @throws IllegalArgumentException if the {@code linkedAccountId} request does not exist.
     */
    public void checkGetAccountOverviewExists( final int linkedAccountId )
    {
        if ( this.getAccountOverViewSubjectMap.get( linkedAccountId ) == null )
        {
            throw new IllegalArgumentException( "Get account overview for linked account " + linkedAccountId +
                                                " has not been requested" );
        }
    }

    /**
     * Removes the get account overview request.
     * @param linkedAccountId
     */
    public void removeGetAccountOverviewRequest( final Integer linkedAccountId )
    {
        this.getAccountOverViewSubjectMap.remove( linkedAccountId );
    }

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

        try
        {
            final GetAccountOverviewDTO getAccountOverviewDTO = this.tradeItService
                                        .getAccountOverview( tradeItAccountEntity,
                                                             linkedAccountEntity.getAccountNumber() );
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
        catch( Exception e )
        {
            log.error( methodName, e );
            this.onGetAccountOverviewException( linkedAccountEntity.getId(), e );
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
            currentLinkedAccountEntity.setGetAccountOverviewValues( getAccountOverviewDTO );
            this.linkedAccountEntityService
                .saveEntity( currentLinkedAccountEntity );
            log.info( String.format( "%s Resolved entity mismatch for linked account %d",
                                     methodName, linkedAccountEntity.getId() ) );
        }
        catch( EntityVersionMismatchException e1 )
        {
            // * this shouldn't happen...
            log.error( String.format( "%s Entity version mismatch %d",
                                      methodName, linkedAccountEntity.getId() ), e1 );
            this.onGetAccountOverviewException( linkedAccountEntity.getId(), e1 );
        }
        log.warn( methodName + ".end" );
    }

    /**
     * Notifies the observer that an exception occurred.
     * @param linkedAccountId
     * @param exception
     */
    private void onGetAccountOverviewException( final Integer linkedAccountId,
                                                final Exception exception )
    {
        final String methodName = "onGetAccountOverviewException";
        log.debug( String.format( "%s.begin linkedAccountId: %d", methodName, linkedAccountId ));
        final AsyncSubject<LinkedAccountEntity> getAccountOverviewSubject = this.getAccountOverViewSubjectMap
                                                                                .get( linkedAccountId );
        getAccountOverviewSubject.onError( exception );
        getAccountOverviewSubject.onCompleted();
        log.debug( String.format( "%s.end linkedAccountId: %d", methodName, linkedAccountId ));
    }

    /**
     * This method will obtain the async subject for the {@code linkedAccountId} and notify any current or future
     * observers  that the get account overview process has completed.
     * @param linkedAccountEntity the updated linked account
     */
    private void onGetAccountOverviewSuccess( final LinkedAccountEntity linkedAccountEntity )
    {
        final String methodName = "onGetAccountOverviewSuccess";
        log.debug( String.format( "%s.begin linkedAccountId: %d", methodName, linkedAccountEntity.getId() ) );
        final AsyncSubject<LinkedAccountEntity> getAccountOverviewSubject = this.getAccountOverViewSubjectMap
                                                                                .get( linkedAccountEntity.getId() );
        getAccountOverviewSubject.onNext( linkedAccountEntity );
        getAccountOverviewSubject.onCompleted();
        log.debug( String.format( "%s.end linkedAccountId: %d", methodName, linkedAccountEntity.getId() ) );
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
