package com.stocktracker.servicelayer.service;

import com.stocktracker.repositorylayer.entity.VStockNoteCountEntity;
import com.stocktracker.repositorylayer.repository.VStockNoteCountRepository;
import com.stocktracker.weblayer.dto.StockNoteCountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class StockNoteCountEntityService extends BaseEntityService<Integer,
                                                                   VStockNoteCountEntity,
                                                                   String,
                                                                   StockNoteCountDTO,
                                                                   VStockNoteCountRepository>
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
            this.vStockNoteCountRepository.findByCustomerUuid( customerId );
        List<StockNoteCountDTO> stockNoteCountDTOs = this.entitiesToDTOs( stockNoteTickerSymbolCountEntities );
        logMethodEnd( methodName, stockNoteTickerSymbolCountEntities.size() );
        return stockNoteCountDTOs;
    }

    @Override
    protected StockNoteCountDTO createDTO()
    {
        return this.context.getBean( StockNoteCountDTO.class );
    }

    @Override
    protected VStockNoteCountEntity createEntity()
    {
        return this.context.getBean( VStockNoteCountEntity.class );
    }

    @Override
    protected VStockNoteCountRepository getRepository()
    {
        return this.vStockNoteCountRepository;
    }
}
