package com.stocktracker.servicelayer.service.cache.stockpricequote;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * This is the result returned for a stock price request.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class GetStockPriceResult
{
    private String tickerSymbol;
    private StockPriceFetchResult stockPriceFetchResult;
    private BigDecimal stockPrice;
    private Exception exception;

    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    public void setStockPriceFetchResult( final StockPriceFetchResult stockPriceFetchResult )
    {
        this.stockPriceFetchResult = stockPriceFetchResult;
    }

    public BigDecimal getStockPrice()
    {
        return stockPrice;
    }

    public void setStockPrice( final BigDecimal stockPrice )
    {
        this.stockPrice = stockPrice;
    }

    public Exception getException()
    {
        return exception;
    }

    public void setException( final Exception exception )
    {
        this.exception = exception;
    }

    public StockPriceFetchResult getStockPriceFetchResult()
    {
        return stockPriceFetchResult;
    }

    public void setStockPriceResult( final StockPriceFetchResult stockPriceFetchResult )
    {
        this.stockPriceFetchResult = stockPriceFetchResult;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "GetStockPriceResult{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", stockPriceFetchResult=" ).append( stockPriceFetchResult );
        sb.append( ", stockPrice=" ).append( stockPrice );
        sb.append( ", exception=" ).append( exception );
        sb.append( '}' );
        return sb.toString();
    }
}
