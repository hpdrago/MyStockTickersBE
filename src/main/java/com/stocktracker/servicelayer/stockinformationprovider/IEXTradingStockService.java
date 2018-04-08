package com.stocktracker.servicelayer.stockinformationprovider;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
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
     * @return price for ticker symbol.
     * @throws StockNotFoundException
     */
    public BigDecimal getPrice( final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getPrice";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol );
        Assert.isTrue( !tickerSymbol.equalsIgnoreCase( "null" ), "ticker symbol cannot be 'null'");
        try
        {
            final BigDecimal price = this.iexTradingClient
                .executeRequest( new PriceRequestBuilder().withSymbol( tickerSymbol )
                                                          .build() );
            logMethodEnd( methodName, tickerSymbol + "=" + price );
            return price;
        }
        catch( IllegalStateException e )
        {
            throw new StockNotFoundException( tickerSymbol, e );
        }
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
        final String methodName = "getQuote";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Assert.isTrue( !tickerSymbol.equalsIgnoreCase( "null" ), "ticker symbol cannot be 'null'");
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
        final String methodName = "getCompany";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Assert.isTrue( !tickerSymbol.equalsIgnoreCase( "null" ), "ticker symbol cannot be 'null'");
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
