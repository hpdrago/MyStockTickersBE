package com.stocktracker.servicelayer.service;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.repositorylayer.repository.StockQuoteRepository;
import com.stocktracker.servicelayer.service.cache.stockquote.StockQuoteEntityCacheClient;
import com.stocktracker.servicelayer.service.cache.stockquote.StockQuoteEntityCacheDataReceiver;
import com.stocktracker.servicelayer.service.cache.stockquote.StockQuoteEntityContainer;
import com.stocktracker.weblayer.dto.StockQuoteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service contains the methods for the StockQuoteEntity.
 */
@Service
public class StockQuoteEntityService extends VersionedEntityService<String,
                                                                    StockQuoteEntity,
                                                                    String,
                                                                    StockQuoteDTO,
                                                                    StockQuoteRepository>
{
    private StockQuoteRepository stockQuoteRepository;
    private StockQuoteEntityCacheClient stockQuoteEntityCacheClient;

    /**
     * Updates the stock quote information in {@code container}. It works with the {@code StockQuoteCache} to
     * retrieve the stock quote asynchronously if needed.
     * @param container The container to set the value.
     */
    public void setQuoteInformation( final StockQuoteEntityContainer container )
    {
        final String methodName = "getStockQuoteDTO";
        logMethodBegin( methodName, container.getTickerSymbol() );
        /*
         * Create the DTO.
         */
        final StockQuoteDTO stockQuoteDTO = (StockQuoteDTO)this.context.getBean( "stockQuoteDTO" );
        /*
         * Create the cached data receiver.
         */
        final StockQuoteEntityCacheDataReceiver receiver = new StockQuoteEntityCacheDataReceiver( container.getTickerSymbol() );
        /*
         * Get the cached data or make an asynchronous request for the data.
         */
        this.stockQuoteEntityCacheClient.setCachedData( receiver );
        /*
         * Set the cache data and status results.
         */
        stockQuoteDTO.setStockQuoteEntity( receiver.getStockQuoteEntity() );
        stockQuoteDTO.setStockQuoteCacheEntryState( receiver.getCacheState() );
        stockQuoteDTO.setStockQuoteCacheError( receiver.getError() );
        logMethodEnd( methodName, stockQuoteDTO );
    }

    /**
     * Checks the stock quote cache to see if the stock quote is being fetched and if so, will block and wait for
     * the fetch to complete.  If the quote is in the cache but not being fetch, the cached version will be returned.
     * Otherwise, database entity will be loaded.
     * @param tickerSymbol
     * @return Stock Quote DTO
     */
    public StockQuoteDTO getStockQuoteDTO( final String tickerSymbol )
    {
        final String methodName = "getStockQuoteDTO";
        logMethodBegin( methodName, tickerSymbol );
        final StockQuoteEntity stockQuoteEntity = this.stockQuoteEntityCacheClient.getCachedData( tickerSymbol );
        final StockQuoteDTO stockQuoteDTO = this.entityToDTO( stockQuoteEntity );
        logMethodEnd( methodName, stockQuoteDTO );
        return stockQuoteDTO;
    }

    @Override
    protected StockQuoteDTO createDTO()
    {
        return this.context.getBean( StockQuoteDTO.class );
    }

    @Override
    protected StockQuoteEntity createEntity()
    {
        return this.context.getBean( StockQuoteEntity.class );
    }

    @Override
    protected StockQuoteRepository getRepository()
    {
        return this.stockQuoteRepository;
    }

    @Autowired
    public void setStockQuoteRepository( final StockQuoteRepository stockQuoteRepository )
    {
        this.stockQuoteRepository = stockQuoteRepository;
    }

    @Autowired
    public void setStockQuoteEntityCacheClient( final StockQuoteEntityCacheClient stockQuoteEntityCacheClient )
    {
        this.stockQuoteEntityCacheClient = stockQuoteEntityCacheClient;
    }
}
