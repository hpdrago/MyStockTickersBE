package com.stocktracker.servicelayer.service;

import com.stocktracker.repositorylayer.entity.VStockNoteCountEntity;
import com.stocktracker.repositorylayer.repository.VStockNoteCountRepository;
import com.stocktracker.weblayer.dto.StockNoteCountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class StockNoteCountEntityService extends BaseEntityService<UUID,
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
     * @param customerUuid The customer id.
     * @return List of {@code StockNoteCountDE} instances.
     */
    public List<StockNoteCountDTO> getStockNotesCount( final UUID customerUuid )
    {
        final String methodName = "getStockNotesCount";
        logMethodBegin( methodName, customerUuid );
        Objects.requireNonNull( customerUuid, "customerUuid cannot be null" );
        List<VStockNoteCountEntity> stockNoteTickerSymbolCountEntities =
            this.vStockNoteCountRepository.findByCustomerUuid( customerUuid );
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
