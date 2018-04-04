package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.repositorylayer.repository.StockQuoteRepository;
import com.stocktracker.servicelayer.stockinformationprovider.IEXTradingStockService;
import com.stocktracker.weblayer.dto.StockQuoteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

/**
 * This service contains the methods for the StockQuoteEntity.
 */
@Service
public class StockQuoteEntityService extends VersionedEntityService<String,
                                                                    StockQuoteEntity,
                                                                    StockQuoteDTO,
                                                                    StockQuoteRepository>
{
    private StockQuoteRepository stockQuoteRepository;
    private IEXTradingStockService iexTradingStockService;

    /**
     * Get a stock quote for the ticker symbol.
     * @param tickerSymbol
     * @return
     * @throws StockQuoteNotFoundException
     */
    public StockQuoteEntity getStockQuote( final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getStockPriceQuote";
        logMethodBegin( methodName, tickerSymbol );
        StockQuoteEntity stockQuoteEntity = null;
        try
        {
            stockQuoteEntity = this.getEntity( tickerSymbol );
            /*
             * If the quote is more than 6 hours old.
             */
            if ( TimeUnit.MILLISECONDS.toHours( stockQuoteEntity.getLastQuoteRequestDate().getTime() ) > 6 )
            {
                stockQuoteEntity = getQuoteFromIEXTrading( tickerSymbol );
            }
        }
        catch( VersionedEntityNotFoundException e )
        {
            stockQuoteEntity = getQuoteFromIEXTrading( tickerSymbol );
        }
        logMethodEnd( methodName, stockQuoteEntity );
        return stockQuoteEntity;
    }

    /**
     * Contact IEXTrading to get the Quote for the stock.
     * @param tickerSymbol
     * @return
     * @throws StockNotFoundException
     */
    private StockQuoteEntity getQuoteFromIEXTrading( final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getQuoteFromIEXTrading";
        logMethodBegin( methodName, tickerSymbol );
        final Quote quote = this.iexTradingStockService
                                .getQuote( tickerSymbol );
        StockQuoteEntity stockQuoteEntity = quoteToStockQuoteEntity( quote );
        try
        {
            if ( this.stockQuoteRepository.exists( tickerSymbol ) )
            {
                this.saveEntity( stockQuoteEntity );
            }
            else
            {
                this.addEntity( stockQuoteEntity );
            }
        }
        catch( EntityVersionMismatchException e1 )
        {
            /*
             * Ignore this exception, if it ever happens it means that two threads were trying to adding/updating the same
             * entity thus we can simply select the entity from the db.
             */
            try
            {
                stockQuoteEntity = this.getEntity( tickerSymbol );
            }
            catch( VersionedEntityNotFoundException e )
            {
                throw new StockNotFoundException( tickerSymbol, e );
            }
        }
        logMethodEnd( methodName, stockQuoteEntity );
        return stockQuoteEntity;
    }

    /**
     * Converts the quote to the quote entity.
     * @param quote
     * @return
     */
    private StockQuoteEntity quoteToStockQuoteEntity( final Quote quote )
    {
        final StockQuoteEntity stockQuoteEntity = this.createEntity();
        stockQuoteEntity.setTickerSymbol( quote.getSymbol() );
        stockQuoteEntity.setCalculationPrice( quote.getCalculationPrice() );
        stockQuoteEntity.setOpenPrice( quote.getOpen() );
        stockQuoteEntity.setClosePrice( quote.getClose() );
        stockQuoteEntity.setHighPrice( quote.getHigh() );
        stockQuoteEntity.setLowPrice( quote.getLow() );
        stockQuoteEntity.setLatestPrice( quote.getLatestPrice() );
        stockQuoteEntity.setLatestPriceSource( quote.getLatestSource() );
        stockQuoteEntity.setLatestPriceTime( quote.getLatestTime() );
        stockQuoteEntity.setLatestUpdate( quote.getLatestUpdate() );
        stockQuoteEntity.setLatestVolume( quote.getLatestVolume().longValue() );
        stockQuoteEntity.setDelayedPrice( quote.getDelayedPrice() );
        stockQuoteEntity.setDelayedPriceTime( quote.getDelayedPriceTime() );
        stockQuoteEntity.setPreviousClose( quote.getPreviousClose() );
        stockQuoteEntity.setChangeAmount( quote.getChange() );
        stockQuoteEntity.setChangePercent( quote.getChangePercent() );
        stockQuoteEntity.setThirtyDayAvgVolume( quote.getAvgTotalVolume().longValue() );
        stockQuoteEntity.setMarketCap( quote.getMarketCap().longValue() );
        stockQuoteEntity.setPeRatio( quote.getPeRatio() );
        stockQuoteEntity.setWeek52High( quote.getWeek52High() );
        stockQuoteEntity.setWeek52Low( quote.getWeek52Low() );
        stockQuoteEntity.setYtdChangePercent( quote.getYtdChange() );
        stockQuoteEntity.setLastQuoteRequestDate( new Timestamp( System.currentTimeMillis() ));
        return stockQuoteEntity;
    }

    @Override
    protected StockQuoteDTO createDTO()
    {
        return this.context.getBean( StockQuoteDTO.class );
    }

    @Override
    protected StockQuoteEntity createEntity()
    {
        return this.context.getBean( StockQuoteEntity.class );
    }

    @Override
    protected StockQuoteRepository getRepository()
    {
        return this.stockQuoteRepository;
    }

    @Autowired
    public void setStockQuoteRepository( final StockQuoteRepository stockQuoteRepository )
    {
        this.stockQuoteRepository = stockQuoteRepository;
    }

    @Autowired
    public void setIexTradingStockService( final IEXTradingStockService iexTradingStockService )
    {
        this.iexTradingStockService = iexTradingStockService;
    }

}
