package com.stocktracker.servicelayer.service.stockinformationprovider;

import com.stocktracker.common.MyLogger;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.endpoint.stocks.StocksEndpoint;
import pl.zankowski.iextrading4j.client.endpoint.stocks.request.StockRequest;

import java.math.BigDecimal;
//import pl.zankowski.iextrading4j.client.IEXTradingClient;

@Service
public class IEXTradingStockService implements MyLogger, StockQuoteServiceProvider
{
    private static final String BASE_URL = "https://api.iextrading.com/";
    private static final String API_VERSION = "1.0";
    private IEXTradingClient iexTradingClient = IEXTradingClient.create();

    @Override
    public String getProviderName()
    {
        return "IEXTrading";
    }

    public StockTickerQuote getStockQuote( final String tickerSymbol )
    {
        Quote quote = this.getQuote( tickerSymbol );
        StockTickerQuote stockTickerQuote = new StockTickerQuote();
        stockTickerQuote.setLastPrice( new BigDecimal( quote.getLatestPrice() ));
        stockTickerQuote.setCompanyName( quote.getCompanyName() );
        return stockTickerQuote;
    }

    public Quote getQuote( final String tickerSymbol )
    {
        final String methodName = "getQuote";
        logMethodBegin( methodName, tickerSymbol );
        StocksEndpoint stocksEndpoint = this.iexTradingClient.getStocksEndpoint();
        Quote quote = stocksEndpoint.requestQuote( StockRequest.builder()
                                                               .withSymbol( tickerSymbol )
                                                               .build() );
        logMethodEnd( methodName, quote );
        return quote;
    }
}
