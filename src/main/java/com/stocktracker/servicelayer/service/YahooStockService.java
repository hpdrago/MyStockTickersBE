package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * This class contains the methods to interact with the Yahoo stock library
 */
@Service
public class YahooStockService implements MyLogger
{
    /**
     * Get the stock price for the {@code tickerSymbol}
     * @param tickerSymbol
     * @return -1 if there is an error, otherwise the last stock price will be returned
     */
    public StockTickerQuote getStockQuote( final String tickerSymbol )
    {
        final String methodName = "getStockQuote";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        StockTickerQuote stockTickerQuote = null;
        try
        {
            Stock stock = getStockFromYahoo( tickerSymbol );
            stockTickerQuote = getStockTickerQuote( stock );
            logMethodEnd( methodName, String.format( "{0} {1}", tickerSymbol, stockTickerQuote.getLastPrice() ));
        }
        catch( Exception e )
        {
            logError( methodName, e );
        }
        return stockTickerQuote;
    }

    /**
     * Create a {@code StockTickerQuote} from a {@code Stock} Yahoo stock instance
     * @param stock
     * @return
     */
    public StockTickerQuote getStockTickerQuote( final Stock stock )
    {
        StockTickerQuote stockTickerQuote = new StockTickerQuote();
        stockTickerQuote.setTickerSymbol( stock.getSymbol() );
        stockTickerQuote.setLastPrice( stock.getQuote().getPrice() );
        if ( stock.getQuote().getLastTradeTime() != null )
        {
            stockTickerQuote.setLastPriceUpdate( new Timestamp( stock.getQuote().getLastTradeTime().getTimeInMillis() ) );
        }
        return stockTickerQuote;
    }

    /**
     * Get the stock information from Yahoo
     * @param tickerSymbol
     * @return
     * @throws IOException
     */
    public Stock getStockFromYahoo( final String tickerSymbol )
        throws IOException
    {
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        return YahooFinance.get( tickerSymbol );
    }

    /**
     * Sets the stock price and company name on the {@code container}
     * @param container
     */
    public void setStockInformation( final YahooStockContainer container )
        throws IOException
    {
        Stock stock = getStockFromYahoo( container.getTickerSymbol() ) ;
        container.setCompanyName( stock.getName() );
        StockTickerQuote stockTickerQuote = this.getStockTickerQuote( stock );
        container.setLastPriceChange( stockTickerQuote.getLastPriceChange() );
        container.setLastPrice( stockTickerQuote.getLastPrice() );
    }

    /**
     * This interface defines the methods necessary to obtain stock information for a ticker symbol.
     */
    public interface YahooStockContainer
    {
        String getTickerSymbol();
        void setLastPrice( final BigDecimal stockPrice );
        void setLastPriceChange( final Timestamp lastPriceChange );
        void setCompanyName( final String companyName );
    }
}
