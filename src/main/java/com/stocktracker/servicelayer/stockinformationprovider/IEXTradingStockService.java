package com.stocktracker.servicelayer.stockinformationprovider;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNotFoundException;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Company;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.rest.request.stocks.CompanyRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.PriceRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.QuoteRequestBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
//import pl.zankowski.iextrading4j.client.IEXTradingClient;

/**
 * This service obtains Stock Quotes from the IEXTrading platform
 */
@Service
public class IEXTradingStockService implements MyLogger
{
    private IEXTradingClient iexTradingClient = IEXTradingClient.create();

    public void getPrices( final List<String> tickerSymbols )
    {
        /*
        this.iexTradingClient
            .executeRequest(  )
            */
    }

    /**
     * Get the price for the ticker symbol.
     * @param tickerSymbol
     * @return
     */
    public BigDecimal getPrice( final String tickerSymbol )
    {
        BigDecimal price = this.iexTradingClient
                               .executeRequest( new PriceRequestBuilder().withSymbol( tickerSymbol )
                               .build() );
        return price;
    }

    /**
     * Gets the stock quote synchronously
     * @param tickerSymbol
     * @return IEXTrading quote
     * @throws StockNotFoundException
     */
    public Quote getQuote( final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getIEXTradingQuote";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Quote quote = null;
        try
        {
            quote = this.iexTradingClient.executeRequest( new QuoteRequestBuilder().withSymbol( tickerSymbol )
                                                                                   .build() );
        }
        catch( javax.ws.rs.NotFoundException e )
        {
            throw new StockNotFoundException( "Stock not found for ticker symbol: " + tickerSymbol, e );
        }
        catch( javax.ws.rs.ProcessingException e )
        {
            logError( methodName, "Error for ticker symbol: " + tickerSymbol, e );
        }
        catch( Exception e )
        {
            throw new StockNotFoundException( "Exception encountered getting quote for " + tickerSymbol, e );
        }
        logMethodEnd( methodName, quote );
        return quote;
    }

    /**
     * Get the company information for the stock.
     * @param tickerSymbol
     * @return
     * @throws StockNotFoundException
     */
    public Company getCompany( final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getIEXTradingQuote";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Company company = null;
        try
        {
            company = this.iexTradingClient.executeRequest( new CompanyRequestBuilder().withSymbol( tickerSymbol )
                                                                                       .build() );
        }
        catch( javax.ws.rs.NotFoundException e )
        {
            throw new StockNotFoundException( "Stock not found for ticker symbol: " + tickerSymbol, e );
        }
        logMethodEnd( methodName, company );
        return company;
    }
}
