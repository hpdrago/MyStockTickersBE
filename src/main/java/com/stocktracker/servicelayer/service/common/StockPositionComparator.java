package com.stocktracker.servicelayer.service.common;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.SetComparator;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.entity.StockPositionEntity;
import com.stocktracker.servicelayer.service.StockPositionService;
import com.stocktracker.servicelayer.tradeit.apiresults.GetPositionsAPIResult;
import com.stocktracker.servicelayer.tradeit.types.TradeItPosition;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class compares the positions returned from TradeIt for a single account against the contents in the STOCK_POSITION
 * table in the database.  The differences in what was returned by TradeIt than what is in the database are applied to
 * the database so that the database STOCK_POSITION table represents what TradeIt has reported.
 */
public class StockPositionComparator implements MyLogger
{
    private StockPositionService stockPositionService;
    private LinkedAccountEntity linkedAccountEntity;

    public StockPositionComparator( StockPositionService stockPositionService )
    {
        this.stockPositionService = stockPositionService;
    }

    /**
     * This method will run on its own thread and will compare the positions stored in the database with the positions
     * returned from TradeIt and synchronize (insert, update, delete) the database positions to match the TradeIt
     * positions.
     * @param linkedAccountEntity
     * @param stockPositionEntities
     * @param getPositionsAPIResult
     * @throws EntityVersionMismatchException
     * @throws VersionedEntityNotFoundException
     */
    @Async( "stockPositionEvaluatorThreadPool" )
    public void comparePositions( final LinkedAccountEntity linkedAccountEntity,
                                  final List<StockPositionEntity> stockPositionEntities,
                                  final GetPositionsAPIResult getPositionsAPIResult )
        throws EntityVersionMismatchException,
               VersionedEntityNotFoundException
    {
        final String methodName = "comparePositions";
        logMethodBegin( methodName, linkedAccountEntity );
        this.linkedAccountEntity = linkedAccountEntity;
        final Map<String,StockPositionEntity> currentStockPositionMap = this.createStockPositionMap( stockPositionEntities );
        /*
         * First we need to convert the getPositionsAPIResult to StockPositionEntities so that we can perform
         * set comparisons
         */
        final List<MyStockPositionEntity> tradeItStockPositionEntities =
            this.convertTradeItPositionsToStockPositions( linkedAccountEntity,
                                                          currentStockPositionMap,
                                                          getPositionsAPIResult );
        /*
         * Next we need to convert the list of StockPositionEntities to a list of MyStockPositionEntities because we need these
         * entities to use an equals method implementation that uses the ticker symbol like the TradeItPositions so that the
         * set operations will work as they use the equals method.
         */
        final List<MyStockPositionEntity> myStockPositionEntities =
            this.convertStockPositionEntitiesToMyStockPositionEntities( stockPositionEntities );

        /*
         * Create the comparator, and compare the to lists and identify inserts, updates, and deletes
         */
        final SetComparator<MyStockPositionEntity>.SetComparatorResults comparatorResults =
            new SetComparator<MyStockPositionEntity>().compareSets( tradeItStockPositionEntities, myStockPositionEntities );

        /*
         * Apply the set differences to the database.
         */
        this.addEntities( comparatorResults.getNewItems() );
        this.deleteEntities( comparatorResults.getDeletedItems() );
        this.updateEntities( comparatorResults.getMatchingItems() );
        logMethodEnd( methodName );
    }

    /**
     * Update stock position entities.
     * @param stockPositionEntities
     */
    private void updateEntities( final Set<MyStockPositionEntity> stockPositionEntities )
        throws EntityVersionMismatchException
    {
        for ( final MyStockPositionEntity myStockPositionEntity : stockPositionEntities )
        {
            /*
             * Need to unwrap the class because JPA doesn't know about the My*
             */
            StockPositionEntity stockPositionEntity = StockPositionEntity.newInstance( myStockPositionEntity );
            this.stockPositionService
                .saveEntity( stockPositionEntity );
        }
    }

    /**
     * Delete the stock positions entities.
     * @param stockPositionEntities
     * @throws VersionedEntityNotFoundException
     */
    private void deleteEntities( final Set<MyStockPositionEntity> stockPositionEntities )
        throws VersionedEntityNotFoundException
    {
        for ( MyStockPositionEntity myStockPositionEntity : stockPositionEntities )
        {
            /*
             * Need to unwrap the class because JPA doesn't know about the My*
             */
            StockPositionEntity stockPositionEntity = StockPositionEntity.newInstance( myStockPositionEntity );
            this.stockPositionService
                .deleteEntity( stockPositionEntity );
        }
    }

    /**
     * Add the entities to the database.
     * @param stockPositionEntities
     */
    private void addEntities( final Set<MyStockPositionEntity> stockPositionEntities )
        throws EntityVersionMismatchException
    {
        for ( final MyStockPositionEntity myStockPositionEntity : stockPositionEntities )
        {
            /*
             * Need to unwrap the class because JPA doesn't know about the My*
             */
            StockPositionEntity stockPositionEntity = StockPositionEntity.newInstance( myStockPositionEntity );
            stockPositionEntity.setLinkedAccountByLinkedAccountUuid( this.linkedAccountEntity );
            stockPositionEntity.setVersion( 1 );
            this.stockPositionService
                .saveEntity( stockPositionEntity );
        }
    }

    /**
     * Converts the list of {@code StockPositionEntity} instances into a list of {@codde MyStockPositionEntity}.
     * @param stockPositionEntities
     * @return
     */
    private List<MyStockPositionEntity> convertStockPositionEntitiesToMyStockPositionEntities( final List<StockPositionEntity>
                                                                                               stockPositionEntities )
    {
        final List<MyStockPositionEntity> returnPositions = new ArrayList<>();
        stockPositionEntities.forEach( stockPositionEntity -> returnPositions.add( new MyStockPositionEntity( stockPositionEntity )));
        return returnPositions;
    }

    /**
     * Converts the TradeIt Position instances into StockPositionEntity instances.
     * @param stockPositionEntityMap This map contains the database entries for the stock positions.  It is used to obtain
     *                               the primary key value for those stocks that match the TradeIt it result so that they
     *                               new values from the TradeIt results can be updated in the database.
     * @param linkedAccountEntity The linked account is the parent account of a {@code StockPositionEntity} so we need to
     *                            assign that parent instance value to add to the database.
     * @param getPositionsAPIResult The TradeIt get positions results that contains the stock positions.
     * @return
     */
    private List<MyStockPositionEntity> convertTradeItPositionsToStockPositions( final LinkedAccountEntity linkedAccountEntity,
                                                                                 final Map<String,StockPositionEntity> stockPositionEntityMap,
                                                                                 final GetPositionsAPIResult getPositionsAPIResult )
    {
        final String methodName = "convertTradeItPositionsToStockPositions";
        logMethodBegin( methodName, linkedAccountEntity.getId() );
        final List<MyStockPositionEntity> tradeItStockPositionEntities = new ArrayList<>();
        if ( getPositionsAPIResult.getPositions() == null ||
             getPositionsAPIResult.getPositions().length == 0 )
        {
            logDebug( methodName, "No positions found" );
        }
        else
        {
            for ( final TradeItPosition tradeItPosition : getPositionsAPIResult.getPositions() )
            {
                /*
                 * We don't want FIXED INCOME or OPTIONS
                 */
                if ( tradeItPosition.getSymbolClass().equals( "EQUITY_OR_ETF" ) ||
                     tradeItPosition.getSymbolClass().equals( "MUTUAL_FUNDS" ) )
                {
                    final StockPositionEntity databaseStockPositionEntity = stockPositionEntityMap
                        .get( tradeItPosition.getSymbol() );
                    /*
                     * The the entities that are not found in the map will be added to the database.
                     */
                    if ( databaseStockPositionEntity == null )
                    {
                        final MyStockPositionEntity stockPositionEntity = new MyStockPositionEntity( tradeItPosition );
                        stockPositionEntity.setLinkedAccountByLinkedAccountUuid( linkedAccountEntity );
                        tradeItStockPositionEntities.add( stockPositionEntity );
                    }
                    /*
                     * The entities that match, will be updated so we need to preserve the database entity values and update
                     * the values from the TradeIt position
                     */
                    else
                    {
                        final MyStockPositionEntity stockPositionEntity = new MyStockPositionEntity( databaseStockPositionEntity );
                        stockPositionEntity.setValues( tradeItPosition );
                        tradeItStockPositionEntities.add( stockPositionEntity );
                    }
                }
            }
        }
        logMethodEnd( methodName, "Total positions: " + tradeItStockPositionEntities.size() );
        return tradeItStockPositionEntities;
    }

    /**
     * Creates a map from the list of {@code StockPositionEntity}
     * @param stockPositionEntities
     * @return
     */
    private Map<String,StockPositionEntity> createStockPositionMap( final List<StockPositionEntity> stockPositionEntities )
    {
        final Map<String,StockPositionEntity> stockPositionMap = new HashMap<>();
        stockPositionEntities.forEach( stockPositionEntity -> stockPositionMap.put( stockPositionEntity.getTickerSymbol(),
                                                                                    stockPositionEntity ));
        return stockPositionMap;
    }

    /**
     * The equals and hashCode method in {@code StockPositionEntity} are based on the id not
     * the ticker symbol. We need to change the equals to the tickerSymbol as that is the key to the
     * TradeIt positions.  The equals method is used in the Set operations.
     */
    private class MyStockPositionEntity extends StockPositionEntity implements Comparable<MyStockPositionEntity>
    {
        public MyStockPositionEntity( final StockPositionEntity stockPositionEntity )
        {
            super( stockPositionEntity );
        }

        public MyStockPositionEntity( final TradeItPosition tradeItPosition )
        {
            super( tradeItPosition );
        }

        @Override
        public int compareTo( final MyStockPositionEntity other )
        {
            return this.getTickerSymbol().compareTo( other.getTickerSymbol() );
        }
    }

}
