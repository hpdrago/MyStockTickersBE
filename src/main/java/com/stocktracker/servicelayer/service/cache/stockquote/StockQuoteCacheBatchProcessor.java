package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.StockQuoteEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheBatchProcessor;
import com.stocktracker.weblayer.dto.StockQuoteDTO;
import com.stocktracker.weblayer.dto.common.StockQuoteDTOContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Stock quote batch processors.  Extends the AsyncCacheBatchProcessor which in turn uses the StockQuoteCacheClient
 * to process a batch of DTOs to obtain the stock quote information from the Stock Quote Cache.
 */
@Service
public class StockQuoteCacheBatchProcessor extends AsyncCacheBatchProcessor<String,
                                                                            StockQuoteEntity,
                                                                            StockQuoteEntityCacheDataReceiver,
                                                                            StockQuoteDTO,
                                                                            StockQuoteDTOContainer,
                                                                            StockQuoteEntityCacheClient,
                                                                            StockQuoteEntityService>
{
    @Autowired
    private StockQuoteEntityCacheClient stockQuoteEntityCacheClient;

    @Autowired
    private StockQuoteEntityService stockQuoteEntityService;

    /**
     * Sets the {@code StockQuoteDTO} on the {@code container}.
     * @param dto
     * @param container
     */
    @Override
    protected void setCachedDTO( final StockQuoteDTO dto, final StockQuoteDTOContainer container )
    {
        container.setStockQuote( dto );
    }

    /**
     * Extracts the cache key (ticker symbol) from the container.
     * @param container
     * @return
     */
    @Override
    protected String getCacheKey( final StockQuoteDTOContainer container )
    {
        return container.getTickerSymbol();
    }

    @Override
    public StockQuoteEntityCacheDataReceiver newReceiver()
    {
        return this.context.getBean( StockQuoteEntityCacheDataReceiver.class );
    }

    @Override
    public StockQuoteEntityCacheClient getAsyncCacheClient()
    {
        return this.stockQuoteEntityCacheClient;
    }

    @Override
    public StockQuoteEntityService getEntityService()
    {
        return this.stockQuoteEntityService;
    }
}
