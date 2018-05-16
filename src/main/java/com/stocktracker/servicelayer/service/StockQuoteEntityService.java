package com.stocktracker.servicelayer.service;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.repositorylayer.repository.StockQuoteRepository;
import com.stocktracker.servicelayer.service.cache.stockquote.StockQuoteEntityCacheClient;
import com.stocktracker.servicelayer.service.cache.stockquote.StockQuoteEntityCacheDataReceiver;
import com.stocktracker.weblayer.dto.StockQuoteDTO;
import com.stocktracker.weblayer.dto.common.StockQuoteDTOAsyncContainer;
import com.stocktracker.weblayer.dto.common.StockQuoteDTOContainer;
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
    public void setQuoteInformation( final StockQuoteDTOContainer container )
    {
        final String methodName = "getStockQuote";
        logMethodBegin( methodName, container.getTickerSymbol() );
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
        final StockQuoteDTO stockQuoteDTO;
        if ( receiver.getStockQuoteEntity() != null )
        {
            stockQuoteDTO = this.entityToDTO( receiver.getStockQuoteEntity() );
        }
        else
        {
            stockQuoteDTO = this.context.getBean( StockQuoteDTO.class );
        }
        stockQuoteDTO.setCacheState( receiver.getCacheState() );
        stockQuoteDTO.setCacheError( receiver.getError() );
        container.setStockQuote( stockQuoteDTO );
        logMethodEnd( methodName, container );
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
        /*
         * Create the cached data receiver.
         */
        final StockQuoteEntityCacheDataReceiver receiver = new StockQuoteEntityCacheDataReceiver( tickerSymbol );
        /*
         * Block and wait for results
         */
        this.stockQuoteEntityCacheClient
            .getCachedData( tickerSymbol, receiver );
        /*
         * Check the results and extract the values.
         */
        StockQuoteDTO stockQuoteDTO;
        if ( receiver.getStockQuoteEntity() != null )
        {
            stockQuoteDTO = this.entityToDTO( receiver.getStockQuoteEntity() );
        }
        else
        {
            stockQuoteDTO = this.context.getBean( StockQuoteDTO.class );
        }
        stockQuoteDTO.setCacheState( receiver.getCacheState() );
        stockQuoteDTO.setCacheError( receiver.getError() );
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
