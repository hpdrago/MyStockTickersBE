package com.stocktracker.task;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.StockEntity;
import com.stocktracker.repositorylayer.repository.StockRepository;
import com.stocktracker.servicelayer.service.StockService;
import com.stocktracker.servicelayer.service.stockinformationprovider.StockTickerQuote;
import com.stocktracker.servicelayer.service.stockinformationprovider.YahooStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

/**
 * Created by mike on 12/10/2016.
 */
@Component
public class UpdateStockPricesTask implements MyLogger
{
    private static final long ONE_HOUR_MILLIS = 3600 * 1000;
    private static final long ONE_DAY_MILLIS = ONE_HOUR_MILLIS * 24;
    private boolean updateStockPrices = false;
    private StockRepository stockRepository;
    private StockService stockService;
    private YahooStockService yahooStockService;

    @Scheduled( initialDelay = 0, fixedRate = ONE_HOUR_MILLIS )
    public void updateStockPricesTask()
    {
        if ( this.updateStockPrices )
        {
            final String methodName = "updateStockPricesTask";
            logInfo( methodName, "Updating Stock Prices..." );
            long startTime = System.currentTimeMillis();
            Timestamp yesterday = new Timestamp( startTime - ONE_DAY_MILLIS );
            for ( StockEntity stockEntity : this.stockRepository.findAll() )
            {
                boolean updateNeeded = stockEntity.getLastPriceUpdate() == null ||
                    stockEntity.getLastPriceUpdate().before( yesterday );
                if ( updateNeeded )
                {
                    StockTickerQuote stockTickerQuote = this.yahooStockService.getStockQuote( stockEntity.getTickerSymbol() );
                    logDebug( methodName, "{0} ${1} lastUpdate: {2}", stockEntity.getTickerSymbol(),
                              stockTickerQuote.getLastPrice(), stockTickerQuote.getLastPriceChange() );
                    stockEntity.setLastPrice( stockTickerQuote.getLastPrice() );
                    stockEntity.setLastPriceUpdate( new Timestamp( startTime ) );
                    stockEntity.setLastPriceChange( stockTickerQuote.getLastPriceChange() );
                    this.stockRepository.save( stockEntity );
                }
            }
            long endTime = System.currentTimeMillis();
            long minutes = TimeUnit.MILLISECONDS.toMinutes( endTime - startTime );
            long seconds = TimeUnit.MILLISECONDS.toMinutes( (endTime - startTime) - (minutes * 60 * 1000) );
            logInfo( methodName, "Updating Stock Prices completed in {0}:{1} minutes:seconds", minutes, seconds );
        }
    }

    /**
     * Dependency injection of the StockRepository
     *
     * @param stockRepository
     */
    @Autowired
    public void setStockRepository( final StockRepository stockRepository )
    {
        final String methodName = "setStockRepository";
        logDebug( methodName, "Dependency Injection of: " + stockRepository );
        this.stockRepository = stockRepository;
    }

    @Autowired
    public void setStockService( final StockService stockService )
    {
        this.stockService = stockService;
        logDebug( "setStockService", "Dependency Injection of: " + stockRepository );
    }

    @Autowired
    public void setYahooStockService( final YahooStockService yahooStockService )
    {
        this.yahooStockService = yahooStockService;
        logDebug( "setYahooStockService", "Dependency Injection of: " + yahooStockService );
    }
}
