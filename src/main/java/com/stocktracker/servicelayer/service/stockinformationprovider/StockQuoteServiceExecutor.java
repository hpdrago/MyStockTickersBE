package com.stocktracker.servicelayer.service.stockinformationprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class StockQuoteServiceExecutor
{
    private IEXTradingStockService iexTradingStockService;

    @PostConstruct
    public void init()
    {
    }

    /**
     * Gets a stock quote for {@code tickerSymbol} asynchronously by creating a new thread.
     * @param tickerSymbol
     * @param handleStockQuoteResult This interface method will be called when the stock quote has been retreived.
     * @return
     */
    @Async( "stockQuoteThreadPool")
    public void asynchronousGetStockQuote( final String tickerSymbol,
                                           final HandleStockQuoteReturn handleStockQuoteResult )
    {
        StockTickerQuote stockTickerQuote = this.iexTradingStockService.getStockTickerQuote( tickerSymbol );
        handleStockQuoteResult.handleStockQuoteReturn( stockTickerQuote );
    }

    /**
     * Fetch the stock quote synchronously.
     * @param tickerSymbol
     * @return
     */
    public StockTickerQuote synchronousGetStockQuote( final String tickerSymbol )
    {
        StockTickerQuote stockTickerQuote = this.iexTradingStockService.getStockTickerQuote( tickerSymbol );
        return stockTickerQuote;
    }

    @Autowired
    public void setIexTradingStockService( final IEXTradingStockService iexTradingStockService )
    {
        this.iexTradingStockService = iexTradingStockService;
    }

}
