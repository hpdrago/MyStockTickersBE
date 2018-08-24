package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.StockQuoteEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntityClient;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import com.stocktracker.servicelayer.service.cache.stockpricequote.StockQuoteEntityCacheRequestKey;
import com.stocktracker.weblayer.dto.StockQuoteDTO;
import com.stocktracker.weblayer.dto.common.StockQuoteDTOContainer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Quote;

/**
 * This is the client class that interacts with the {@code StockQuoteEntityCache} to get current IEXTrading Quote
 * updates.
 */
@Service
public class StockQuoteEntityCacheClient extends AsyncCacheDBEntityClient<String,
                                                                          StockQuoteEntity,
                                                                          String,
                                                                          Quote,
                                                                          StockQuoteEntityCacheEntry,
                                                                          StockQuoteEntityCacheDataReceiver,
                                                                          StockQuoteDTOContainer,
                                                                          StockQuoteEntityCacheRequestKey,
                                                                          StockQuoteEntityCacheRequest,
                                                                          StockQuoteEntityCacheResponse,
                                                                          StockQuoteEntityServiceExecutor,
                                                                          StockQuoteEntityCache,
                                                                          StockQuoteEntityService>
{
    @Autowired
    private StockQuoteEntityCache stockQuoteEntityCache;
    @Autowired
    private StockQuoteEntityService stockQuoteEntityService;

    @Override
    protected StockQuoteEntityCache getCache()
    {
        return stockQuoteEntityCache;
    }

    @Override
    protected StockQuoteEntity createCachedDataObject()
    {
        return this.context.getBean( StockQuoteEntity.class );
    }

    @Override
    protected StockQuoteEntityService getEntityService()
    {
        return this.stockQuoteEntityService;
    }

    @Override
    public StockQuoteEntityCacheDataReceiver createDataReceiver()
    {
        return this.context.getBean( StockQuoteEntityCacheDataReceiver.class );
    }

    @Override
    protected StockQuoteEntityCacheRequestKey createRequestKey( final String cacheKey, final String asyncKey )
    {
        return new StockQuoteEntityCacheRequestKey( cacheKey, asyncKey );
    }

    @Override
    protected String getASyncKey( final StockQuoteDTOContainer container )
    {
        return container.getTickerSymbol();
    }

    @Override
    protected String getCacheKey( final StockQuoteDTOContainer container )
    {
        return container.getTickerSymbol();
    }

    @Override
    protected void setCacheState( final StockQuoteDTOContainer container, final AsyncCacheEntryState cacheState )
    {
        this.getStockQuoteDTO( container ).setCacheState( cacheState );
    }

    @Override
    protected void setCacheError( final StockQuoteDTOContainer container, final String cacheError )
    {
        this.getStockQuoteDTO( container ).setCacheError( cacheError );
    }

    @Override
    protected void setCachedData( final StockQuoteDTOContainer container, final StockQuoteEntity cachedData )
    {
        if ( cachedData != null )
        {
            BeanUtils.copyProperties( cachedData, this.getStockQuoteDTO( container ) );
        }
    }

    @Override
    protected void setCacheKey( final StockQuoteDTOContainer container, final String cacheKey )
    {
        this.getStockQuoteDTO( container ).setCacheKey( cacheKey );
    }

    private StockQuoteDTO getStockQuoteDTO( final StockQuoteDTOContainer container )
    {
        if ( container.getStockQuoteDTO() == null )
        {
            final StockQuoteDTO stockQuoteDTO = this.context.getBean( StockQuoteDTO.class );
            container.setStockQuoteDTO( stockQuoteDTO );
        }
        return container.getStockQuoteDTO();
    }
}
