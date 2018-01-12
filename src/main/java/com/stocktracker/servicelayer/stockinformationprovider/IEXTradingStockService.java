package com.stocktracker.servicelayer.stockinformationprovider;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNotFoundException;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.endpoint.stocks.StocksEndpoint;
import pl.zankowski.iextrading4j.client.endpoint.stocks.request.StockRequest;

import java.math.BigDecimal;
import java.sql.Timestamp;
//import pl.zankowski.iextrading4j.client.IEXTradingClient;

/**
 * This service obtains Stock Quotes from the IEXTrading platform
 */
@Service
public class IEXTradingStockService implements MyLogger, StockQuoteServiceProvider
{
    private IEXTradingClient iexTradingClient = IEXTradingClient.create();

    @Override
    public String getProviderName()
    {
        return "IEXTrading";
    }

    /**
     * Gets the stock quote
     * @param tickerSymbol
     * @return StockTickerQuote
     * @throws StockNotFoundException for invalid ticker symbols.
     */
    public StockTickerQuote getStockTickerQuote( final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getStockTickerQuote";
        logMethodBegin( methodName, tickerSymbol );
        StockTickerQuote stockTickerQuote = this.getIEXTradingQuote( tickerSymbol );
        logMethodEnd( methodName, stockTickerQuote );
        return stockTickerQuote;
    }

    /**
     * Gets the stock quote synchronously
     * @param tickerSymbol
     * @return IEXTrading quote
     * @throws StockNotFoundException
     */
    public StockTickerQuote getIEXTradingQuote( final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getIEXTradingQuote";
        logMethodBegin( methodName, tickerSymbol );
        StocksEndpoint stocksEndpoint = this.iexTradingClient.getStocksEndpoint();
        StockTickerQuote stockTickerQuote = null;
        try
        {
            Quote quote = stocksEndpoint.requestQuote( StockRequest.builder()
                                                                   .withSymbol( tickerSymbol )
                                                                   .build() );
            stockTickerQuote = this.quoteToStockTickerQuote( tickerSymbol, quote );
        }
        catch( javax.ws.rs.NotFoundException e )
        {
            throw new StockNotFoundException( "Stock not found for ticker symbol: " + tickerSymbol, e );
        }
        catch( javax.ws.rs.ProcessingException e )
        {
            logError( methodName, "Error for ticker symbol: " + tickerSymbol, e );
        }
        logMethodEnd( methodName, stockTickerQuote );
        return stockTickerQuote;
    }

    /**
     * Converts the IEX Quote into a StockTickerQuote
     *
     * @param tickerSymbol
     * @param quote
     * @return
     */
    private StockTickerQuote quoteToStockTickerQuote( final String tickerSymbol, final Quote quote )
    {
        StockTickerQuote stockTickerQuote = new StockTickerQuote();
        stockTickerQuote.setTickerSymbol( tickerSymbol );
        stockTickerQuote.setLastPrice( new BigDecimal( quote.getLatestPrice() ));
        stockTickerQuote.setLastPriceChange( new Timestamp( quote.getLatestUpdate() ) );
        stockTickerQuote.setCompanyName( quote.getCompanyName() );
        return stockTickerQuote;
    }
}
