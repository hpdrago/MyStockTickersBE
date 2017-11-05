package com.stocktracker.servicelayer.service.stockinformationprovider;

import com.stocktracker.common.MyLogger;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.endpoint.stocks.StocksEndpoint;
import pl.zankowski.iextrading4j.client.endpoint.stocks.request.StockRequest;

import java.math.BigDecimal;
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
     */
    public StockTickerQuote getStockTickerQuote( final String tickerSymbol )
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
     */
    public StockTickerQuote getIEXTradingQuote( final String tickerSymbol )
    {
        final String methodName = "getIEXTradingQuote";
        logMethodBegin( methodName, tickerSymbol );
        StocksEndpoint stocksEndpoint = this.iexTradingClient.getStocksEndpoint();
        Quote quote = stocksEndpoint.requestQuote( StockRequest.builder()
                                                               .withSymbol( tickerSymbol )
                                                               .build() );
        StockTickerQuote stockTickerQuote = this.quoteToStockTickerQuote( quote );
        logMethodEnd( methodName, stockTickerQuote );
        return stockTickerQuote;
    }

    /**
     * Converts the IEX Quote into a StockTickerQuote
     * @param quote
     * @return
     */
    private StockTickerQuote quoteToStockTickerQuote( final Quote quote )
    {
        StockTickerQuote stockTickerQuote = new StockTickerQuote();
        stockTickerQuote.setLastPrice( new BigDecimal( quote.getLatestPrice() ));
        stockTickerQuote.setCompanyName( quote.getCompanyName() );
        return stockTickerQuote;
    }
}
