package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.endpoint.stocks.StocksEndpoint;
import pl.zankowski.iextrading4j.client.endpoint.stocks.request.StockRequest;

import java.math.BigDecimal;
//import pl.zankowski.iextrading4j.client.IEXTradingClient;

@Service
public class IEXTradingStockService implements MyLogger
{
    private static final String BASE_URL = "https://api.iextrading.com/";
    private static final String API_VERSION = "1.0";

    public StockTickerQuote getStockQuote( final String tickerSymbol )
    {
        Quote quote = this.getQuote( tickerSymbol );
        StockTickerQuote stockTickerQuote = new StockTickerQuote();
        stockTickerQuote.setLastPrice( new BigDecimal( quote.getLatestPrice() ));
        stockTickerQuote.setCompanyName( quote.getCompanyName() );
        return stockTickerQuote;
    }

    private IEXTradingClient iexTradingClient = IEXTradingClient.create();

    public Quote getQuote( final String tickerSymbol )
    {
        final String methodName = "getQuote";
        logMethodBegin( methodName, tickerSymbol );
        StocksEndpoint stocksEndpoint = iexTradingClient.getStocksEndpoint();
        Quote quote = stocksEndpoint.requestQuote( StockRequest.builder()
                                                               .withSymbol( tickerSymbol )
                                                               .build() );
        logMethodEnd( methodName, quote );
        return quote;
    }
}
