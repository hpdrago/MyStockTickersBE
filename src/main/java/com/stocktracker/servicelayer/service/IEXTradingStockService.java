package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNotFoundException;
import org.glassfish.jersey.message.internal.MessageBodyProviderNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import pl.zankowski.iextrading4j.api.stocks.BatchStocks;
import pl.zankowski.iextrading4j.api.stocks.Company;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.rest.request.stocks.BatchMarketStocksRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.BatchStocksType;
import pl.zankowski.iextrading4j.client.rest.request.stocks.CompanyRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.PriceRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.QuoteRequestBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
//import pl.zankowski.iextrading4j.client.IEXTradingClient;

/**
 * This service obtains Stock Quotes from the IEXTrading platform
 */
@Service
public class IEXTradingStockService implements MyLogger
{
    private IEXTradingClient iexTradingClient = IEXTradingClient.create();

    /**
     * Using the market batch API, get a batch of stock companies.
     * @param tickerSymbols The ticker symbols to get the stock companies for.
     * @return
     */
    public List<BigDecimal> getStockPrices( final List<String> tickerSymbols )
    {
        final String methodName = "getStockPrices";
        logMethodBegin( methodName, tickerSymbols );
        final BatchMarketStocksRequestBuilder batchMarketStocksRequestBuilder = getBatchMarketStocksRequestBuilder( tickerSymbols, BatchStocksType.PRICE );
        final Map<String, BatchStocks> batchStocksMap = iexTradingClient.executeRequest( batchMarketStocksRequestBuilder.build() );
        final List<BigDecimal> stockPrices = batchStocksMap.values()
                                                           .stream()
                                                           .map( batchStocks -> batchStocks.getPrice() )
                                                           .collect(Collectors.toList());
        logMethodEnd( methodName, stockPrices.size() );
        return stockPrices;
    }

    /**
     * Using the market batch API, get a batch of stock companies.
     * @param tickerSymbols The ticker symbols to get the stock companies for.
     * @return
     */
    public List<Company> getCompanies( final List<String> tickerSymbols )
    {
        final String methodName = "getCompanies";
        logMethodBegin( methodName, tickerSymbols );
        final BatchMarketStocksRequestBuilder batchMarketStocksRequestBuilder = getBatchMarketStocksRequestBuilder( tickerSymbols, BatchStocksType.COMPANY );
        final Map<String, BatchStocks> batchStocksMap = iexTradingClient.executeRequest( batchMarketStocksRequestBuilder.build() );
        final List<Company> companies = batchStocksMap.values()
                                                      .stream()
                                                      .map( batchStocks -> batchStocks.getCompany() )
                                                      .collect(Collectors.toList());
        logMethodEnd( methodName, companies.size() );
        return companies;
    }

    /**
     * Using the market batch API, get a back of stock quotes.
     * @param tickerSymbols The ticker symbols for which quotes will be fetched.
     * @return
     */
    public List<Quote> getQuotes( final List<String> tickerSymbols )
    {
        final String methodName = "getQuotes";
        logMethodBegin( methodName, tickerSymbols );
        final BatchMarketStocksRequestBuilder batchMarketStocksRequestBuilder = getBatchMarketStocksRequestBuilder( tickerSymbols, BatchStocksType.QUOTE );
        final Map<String, BatchStocks> batchStocksMap = iexTradingClient.executeRequest( batchMarketStocksRequestBuilder.build() );
        final List<Quote> quotes = batchStocksMap.values()
                                                 .stream()
                                                 .map( batchStocks -> batchStocks.getQuote() )
                                                 .collect(Collectors.toList());
        logMethodEnd( methodName, quotes.size() );
        return quotes;
    }

    /**
     * Creates the batch market request builder, adds the ticker symbols, and sets the batch stock type.
     * @param tickerSymbols The list of tickers to get information for.
     * @param batchStocksType Identifies the type of data to retrieve.
     * @return new instance of batch market builder.
     */
    private BatchMarketStocksRequestBuilder getBatchMarketStocksRequestBuilder( final List<String> tickerSymbols,
                                                                                final BatchStocksType batchStocksType )
    {
        final BatchMarketStocksRequestBuilder batchMarketStocksRequestBuilder = new BatchMarketStocksRequestBuilder();
        for ( final String tickerSymbol : tickerSymbols )
        {
            batchMarketStocksRequestBuilder.withSymbol( tickerSymbol );
        }
        batchMarketStocksRequestBuilder.addType( batchStocksType );
        return batchMarketStocksRequestBuilder;
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
        catch( javax.ws.rs.NotFoundException | IllegalStateException | MessageBodyProviderNotFoundException e )
        {
            throw new StockNotFoundException( "Stock not found for ticker symbol: " + tickerSymbol, e );
        }
        logMethodEnd( methodName, company );
        return company;
    }

    private boolean isStockNotFoundException( final Exception e )
    {
        if ( e instanceof javax.ws.rs.NotFoundException ||
             e instanceof IllegalStateException ||
             e instanceof MessageBodyProviderNotFoundException )
        {
            return true;
        }
        return false;
    }
}
