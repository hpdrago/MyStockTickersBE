package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.StockNoteSourceNotFoundException;
import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import com.stocktracker.repositorylayer.repository.StockNoteSourceRepository;
import com.stocktracker.weblayer.dto.StockNoteSourceDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

@Service
public class StockNoteSourceService extends BaseService<StockNoteSourceEntity, StockNoteSourceDTO>
{
    private StockNoteSourceRepository stockNoteSourceRepository;

    @Autowired
    public void setStockNoteSourceRepository( final StockNoteSourceRepository stockNoteSourceRepository )
    {
        this.stockNoteSourceRepository = stockNoteSourceRepository;
    }

    /**
     * Get the stock note source for the stock note source id.
     *
     * @param stockNoteSourceId The id for the stock note source
     * @return StockNoteSourceDE domain entity for stock note source id
     * @throws StockNoteSourceNotFoundException If the stock source is not found
     */
    public StockNoteSourceEntity getStockNoteSource( final int stockNoteSourceId )
    {
        final String methodName = "getStockNoteSource";
        logMethodBegin( methodName, stockNoteSourceId );
        Assert.isTrue( stockNoteSourceId > 0, "stockNoteId must be > 0" );
        StockNoteSourceEntity stockNoteSourceEntity = this.stockNoteSourceRepository.findOne( stockNoteSourceId );
        if ( stockNoteSourceEntity == null )
        {
            throw new StockNoteSourceNotFoundException( stockNoteSourceId );
        }
        logMethodEnd( methodName, stockNoteSourceEntity );
        return stockNoteSourceEntity;
    }

    /**
     * Get all of the stock notes sources for a customer
     *
     * @param customerId The id for the stock note source
     */
    public List<StockNoteSourceDTO> getStockNoteSources( final int customerId )
    {
        final String methodName = "getStockNoteSources";
        logMethodBegin( methodName, customerId );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        List<StockNoteSourceEntity> stockNoteSourceEntities =
            stockNoteSourceRepository.findByCustomerIdOrderByTimesUsedDesc( customerId );
        List<StockNoteSourceDTO> stockNoteSourceDTOs = this.entitiesToDTOs( stockNoteSourceEntities );
        logMethodEnd( methodName, stockNoteSourceEntities.size() + " sources found" );
        return stockNoteSourceDTOs;
    }

    /**
     * Create a new stock note source
     * @param stockNoteSourceDTO
     * @return
     */
    public StockNoteSourceDTO createStockNoteSource( final StockNoteSourceDTO stockNoteSourceDTO )
    {
        final String methodName = "createStockNoteSource";
        logMethodBegin( methodName, stockNoteSourceDTO );
        StockNoteSourceEntity stockNoteSourceEntity = this.dtoToEntity( stockNoteSourceDTO );
        StockNoteSourceEntity newStockNoteSourceEntity = this.stockNoteSourceRepository.save( stockNoteSourceEntity );
        StockNoteSourceDTO newStockNoteSourceDTO = this.entityToDTO( newStockNoteSourceEntity );
        logMethodEnd( methodName, newStockNoteSourceDTO );
        return newStockNoteSourceDTO;
    }

    @Override
    protected StockNoteSourceDTO entityToDTO( final StockNoteSourceEntity stockNoteSourceEntity )
    {
        Objects.requireNonNull( stockNoteSourceEntity );
        StockNoteSourceDTO stockNoteSourceDTO = StockNoteSourceDTO.newInstance();
        BeanUtils.copyProperties( stockNoteSourceEntity, stockNoteSourceDTO );
        return stockNoteSourceDTO;
    }

    @Override
    protected StockNoteSourceEntity dtoToEntity( final StockNoteSourceDTO stockNoteSourceDTO )
    {
        Objects.requireNonNull( stockNoteSourceDTO );
        StockNoteSourceEntity stockNoteSourceEntity = StockNoteSourceEntity.newInstance();
        BeanUtils.copyProperties( stockNoteSourceDTO, stockNoteSourceEntity );
        return stockNoteSourceEntity;
    }
}
