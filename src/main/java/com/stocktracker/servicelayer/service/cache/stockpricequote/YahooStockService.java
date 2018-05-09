package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNotFoundException;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * This class contains the methods to interact with the Yahoo stock library
 */
@Service
public class YahooStockService implements MyLogger, StockQuoteServiceProvider
{
    @Override
    public String getProviderName()
    {
        return "Yahoo";
    }

    /**
     * Get the stock price for the {@code tickerSymbol}
     * @param tickerSymbol
     * @return -1 if there is an error, otherwise the last stock price will be returned
     * @throws StockNotFoundException if the ticker symbol is no longer valid.
     */
    public BigDecimal getStockPrice( final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getStockPriceQuote";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        BigDecimal stockPrice = null;
        try
        {
            Stock stock = getStock( tickerSymbol );
            if ( stock == null )
            {
                logError( methodName, "Failed to get quote for " + tickerSymbol + " from Yahoo." );
            }
            else
            {
                stockPrice = stock.getQuote().getPrice();
                logDebug( methodName, "{0} {1}", tickerSymbol, stockPrice );
            }
        }
        catch( FileNotFoundException e )
        {
            throw new StockNotFoundException( tickerSymbol );
        }
        catch( Exception e )
        {
            logError( methodName, e );
        }
        logMethodEnd( methodName, stockPrice );
        return stockPrice;
    }

    /**
     * Create a {@code StockPriceQuoteEntity} from a {@code Stock} Yahoo stock instance
     * @param stock
     * @return
     */
    /*
    public StockPriceQuoteEntity getStockPriceQuote( final Stock stock )
    {
        final String methodName = "getStockPriceQuote";
        logMethodBegin( methodName, stock );
        Objects.requireNonNull( stock, "stock cannot be null" );
        Objects.requireNonNull( stock.getSymbol(), "stock cannot be null" );
        StockPriceQuoteEntity stockTickerQuote = new StockPriceQuoteEntity();
        stockTickerQuote.setTickerSymbol( stock.getSymbol() );
        stockTickerQuote.setStockPrice( stock.getQuote().getPrice() );
        stockTickerQuote.setOpenPrice( stock.getQuote().getOpen() );
        if ( stock.getQuote().getLastTradeTime() != null )
        {
            stockTickerQuote.setLastPriceChange( new Timestamp( stock.getQuote().getLastTradeTime().getTimeInMillis() ) );
        }
        logMethodEnd( methodName, stockTickerQuote );
        return stockTickerQuote;
    }
    */

    /**
     * Get the stock information from Yahoo
     * @param tickerSymbol
     * @return
     * @throws IOException For communication errors
     * @throws FileNotFoundException For invalid or not longer in use ticker symbol.
     */
    public Stock getStock( final String tickerSymbol )
        throws IOException
    {
        final String methodName = "getStock";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Stock stock = YahooFinance.get( tickerSymbol );
        logMethodEnd( methodName, stock );
        return stock;
    }

    /**
     * Sets the stock price and company name on the {@code container}
     * @param container
     */
    /*
    public void setStockInformation( final YahooStockContainer container )
        throws IOException
    {
        final String methodName = "setStockInformation";
        logMethodBegin( methodName, container.getTickerSymbol() );
        Stock stock = getStock( container.getTickerSymbol() ) ;
        container.setCompanyName( stock.getName() );
        StockPriceQuoteEntity stockTickerQuote = this.getStockPriceQuote( stock );
        container.setLastPriceChangeTimestamp( stockTickerQuote.getLastPriceChange() );
        container.setStockPrice( stockTickerQuote.getStockPriceQuote() );
        logMethodEnd( methodName, container.getTickerSymbol() + " " + container.getStockPriceQuote() );
    }
    */

    /**
     * This interface defines the methods necessary to obtain stock information for a ticker symbol.
     */
    public interface YahooStockContainer
    {
        String getTickerSymbol();
        void setLastPrice( final BigDecimal stockPrice );
        void setLastPriceChangeTimestamp( final Timestamp lastPriceChange );
        void setCompanyName( final String companyName );
        BigDecimal getLastPrice();
        Timestamp getLastPriceChangeTimestamp();
        String getCompanyName();
    }

}
