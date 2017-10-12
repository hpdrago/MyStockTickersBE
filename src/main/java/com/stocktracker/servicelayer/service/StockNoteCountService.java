package com.stocktracker.servicelayer.service;

import com.stocktracker.repositorylayer.entity.VStockNoteCountEntity;
import com.stocktracker.repositorylayer.repository.VStockNoteCountRepository;
import com.stocktracker.weblayer.dto.StockNoteCountDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

@Service
public class StockNoteCountService extends BaseService<VStockNoteCountEntity, StockNoteCountDTO>
{
    private VStockNoteCountRepository vStockNoteCountRepository;

    @Autowired
    public void setvStockNoteCountRepository( final VStockNoteCountRepository vStockNoteCountRepository )
    {
        this.vStockNoteCountRepository = vStockNoteCountRepository;
    }

    /**
     * Aggregates the number of notices for each stock (ticker symbol) for a customer.
     * @param customerId The customer id.
     * @return List of {@code StockNoteCountDE} instances.
     */
    public List<StockNoteCountDTO> getStockNotesCount( final int customerId )
    {
        final String methodName = "getStockNotesCount";
        logMethodBegin( methodName, customerId );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        List<VStockNoteCountEntity> stockNoteTickerSymbolCountEntities =
            this.vStockNoteCountRepository.findByCustomerId( customerId );
        List<StockNoteCountDTO> stockNoteCountDTOs = this.entitiesToDTOs( stockNoteTickerSymbolCountEntities );
        logMethodEnd( methodName, stockNoteTickerSymbolCountEntities.size() );
        return stockNoteCountDTOs;
    }

    @Override
    protected StockNoteCountDTO entityToDTO( final VStockNoteCountEntity stockNoteCountEntity )
    {
        Objects.requireNonNull( stockNoteCountEntity );
        StockNoteCountDTO stockNoteCountDTO = StockNoteCountDTO.newInstance();
        BeanUtils.copyProperties( stockNoteCountEntity, stockNoteCountDTO );
        return stockNoteCountDTO;
    }

    @Override
    protected VStockNoteCountEntity dtoToEntity( final StockNoteCountDTO stockNoteCountDTO )
    {
        Objects.requireNonNull( stockNoteCountDTO );
        VStockNoteCountEntity vStockNoteCountEntity = VStockNoteCountEntity.newInstance();
        BeanUtils.copyProperties( stockNoteCountDTO, vStockNoteCountEntity );
        return vStockNoteCountEntity;
    }
}
