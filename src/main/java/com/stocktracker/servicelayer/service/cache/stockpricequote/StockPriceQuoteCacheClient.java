package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.servicelayer.service.cache.common.AsyncCacheBatchClient;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import com.stocktracker.weblayer.dto.StockPriceQuoteDTO;
import com.stocktracker.weblayer.dto.common.StockPriceQuoteDTOContainer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Client interface with the Stock Price Quote Cache.
 */
@Service
public class StockPriceQuoteCacheClient extends AsyncCacheBatchClient<String,
                                                                      StockPriceQuote,
                                                                      String,
                                                                      BigDecimal,
                                                                      StockPriceQuoteCacheEntry,
                                                                      StockPriceQuoteCacheDataReceiver,
                                                                      StockPriceQuoteDTOContainer,
                                                                      StockPriceQuoteCacheRequestKey,
                                                                      StockPriceQuoteCacheRequest,
                                                                      StockPriceQuoteCacheResponse,
                                                                      StockPriceQuoteServiceExecutor,
                                                                      StockPriceQuoteCache>
{
    private StockPriceQuoteCache stockPriceQuoteCache;

    @Override
    protected StockPriceQuoteCache getCache()
    {
        return this.stockPriceQuoteCache;
    }

    @Override
    protected StockPriceQuote createCachedDataObject()
    {
        return context.getBean( StockPriceQuote.class );
    }

    @Autowired
    public void setStockPriceQuoteCache( final StockPriceQuoteCache stockPriceQuoteCache )
    {
        this.stockPriceQuoteCache = stockPriceQuoteCache;
    }

    @Override
    public StockPriceQuoteCacheDataReceiver createDataReceiver()
    {
        return this.context.getBean( StockPriceQuoteCacheDataReceiver.class );
    }

    @Override
    protected StockPriceQuoteCacheRequestKey createRequestKey( final String cacheKey, final String asyncKey )
    {
        return new StockPriceQuoteCacheRequestKey( cacheKey, asyncKey );
    }

    @Override
    protected String getASyncKey( final StockPriceQuoteDTOContainer container )
    {
        return container.getTickerSymbol();
    }

    @Override
    protected String getCacheKey( final StockPriceQuoteDTOContainer container )
    {
        return container.getTickerSymbol();
    }

    @Override
    protected void setCacheState( final StockPriceQuoteDTOContainer container, final AsyncCacheEntryState cacheState )
    {
        container.getStockPriceQuote().setCacheState( cacheState );
    }

    @Override
    protected void setCacheError( final StockPriceQuoteDTOContainer container, final String cacheError )
    {
        container.getStockPriceQuote().setCacheError( cacheError );
    }

    @Override
    protected void setCachedData( final StockPriceQuoteDTOContainer container, final StockPriceQuote cachedData )
    {
        final StockPriceQuoteDTO stockPriceQuoteDTO = this.context.getBean( StockPriceQuoteDTO.class );
        if ( cachedData != null )
        {
            BeanUtils.copyProperties( cachedData, stockPriceQuoteDTO );
        }
        container.setStockPriceQuote( stockPriceQuoteDTO );
    }

    @Override
    protected void setCacheKey( final StockPriceQuoteDTOContainer container, final String cacheKey )
    {
        container.setTickerSymbol( cacheKey );
    }
}
