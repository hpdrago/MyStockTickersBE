package com.stocktracker.servicelayer.service;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.repositorylayer.repository.StockQuoteRepository;
import com.stocktracker.servicelayer.service.cache.stockquote.StockQuoteCacheBatchProcessor;
import com.stocktracker.servicelayer.service.cache.stockquote.StockQuoteEntityCacheClient;
import com.stocktracker.servicelayer.service.cache.stockquote.StockQuoteEntityCacheDataReceiver;
import com.stocktracker.weblayer.dto.StockQuoteDTO;
import com.stocktracker.weblayer.dto.common.StockQuoteDTOContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private StockQuoteRepository stockQuoteRepository;
    @Autowired
    private StockQuoteEntityCacheClient stockQuoteEntityCacheClient;
    @Autowired
    private StockQuoteCacheBatchProcessor stockQuoteCacheBatchProcessor;

    /**
     * Gets the stock quote information for all of the DTOs in {@code containers}.  The quotes are fetched as a batch
     * using the stock quote client.
     * @param containers
     */
    public void setStockQuoteInformation( final List<? extends StockQuoteDTOContainer> containers )
    {
        final String methodName = "setStockQuoteInformation";
        logMethodBegin( methodName, containers.size() );
        /*
         * Create DTO for each container.
         */
        final List<StockQuoteDTO> dtos = containers.stream()
                                                   .map( (StockQuoteDTOContainer container) ->
                                                         {
                                                             StockQuoteDTO stockQuoteDTO = this.context
                                                                                               .getBean( StockQuoteDTO.class );
                                                             container.setStockQuoteDTO( stockQuoteDTO );
                                                             stockQuoteDTO.setCacheKey( container.getTickerSymbol() );
                                                             return stockQuoteDTO;
                                                         })
                                                   .collect(Collectors.toList());
        this.stockQuoteCacheBatchProcessor
            .getCachedData( dtos );
        logMethodEnd( methodName );
    }

    /**
     * Updates the stock quote information in {@code container}. It works with the {@code StockQuoteCache} to
     * retrieve the stock quote asynchronously if needed.
     * @param container The container to set the value.
     */
    public void setStockQuoteInformation( final StockQuoteDTOContainer container )
    {
        final String methodName = "setStockQuoteInformation";
        logMethodBegin( methodName, container.getTickerSymbol() );
        /*
         * Create the cached data receiver.
         */
        final StockQuoteEntityCacheDataReceiver receiver = this.context.getBean( StockQuoteEntityCacheDataReceiver.class );
        receiver.setCacheKey( container.getTickerSymbol() );
        /*
         * Get the cached data or make an asynchronous request for the data.
         */
        this.stockQuoteEntityCacheClient
            .getCachedData( receiver );
        /*
         * Set the cache data and status results.
         */
        final StockQuoteDTO stockQuoteDTO;
        if ( receiver.getCachedData() != null )
        {
            stockQuoteDTO = this.entityToDTO( receiver.getCachedData() );
        }
        else
        {
            stockQuoteDTO = this.context.getBean( StockQuoteDTO.class );
        }
        stockQuoteDTO.setCacheState( receiver.getCacheState() );
        stockQuoteDTO.setCacheError( receiver.getCacheError() );
        container.setStockQuoteDTO( stockQuoteDTO );
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
        final StockQuoteEntityCacheDataReceiver receiver = this.context.getBean( StockQuoteEntityCacheDataReceiver.class );
        receiver.setCacheKey( tickerSymbol );
        /*
         * Block and wait for results
         */
        this.stockQuoteEntityCacheClient
            .getCachedData( tickerSymbol, receiver );
        /*
         * Check the results and extract the values.
         */
        StockQuoteDTO stockQuoteDTO;
        if ( receiver.getCachedData() != null )
        {
            stockQuoteDTO = this.entityToDTO( receiver.getCachedData() );
        }
        else
        {
            stockQuoteDTO = this.context.getBean( StockQuoteDTO.class );
        }
        stockQuoteDTO.setCacheState( receiver.getCacheState() );
        stockQuoteDTO.setCacheError( receiver.getCacheError() );
        stockQuoteDTO.setTickerSymbol( tickerSymbol );
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
}
