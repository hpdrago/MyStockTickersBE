package com.stocktracker.servicelayer.service;

import com.stocktracker.repositorylayer.entity.StockNoteStockEntity;
import com.stocktracker.repositorylayer.repository.StockNoteStockRepository;
import com.stocktracker.weblayer.dto.StockNoteStockDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

@Service
public class StockNoteStockService extends BaseService<StockNoteStockEntity, StockNoteStockDTO>
{
    private StockNoteStockRepository stockNoteStockRepository;

    @Autowired
    public void setStockNoteStockRepository( final StockNoteStockRepository stockNoteStockRepository )
    {
        this.stockNoteStockRepository = stockNoteStockRepository;
    }

    /**
     * Get the stock notes for the customer and the ticker symbol.
     *
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    public List<StockNoteStockDTO> getStockNoteStocks( final int customerId, final String tickerSymbol )
    {
        final String methodName = "getStocks";
        logMethodBegin( methodName, customerId, tickerSymbol );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        List<StockNoteStockEntity> stockNoteStockEntities =
            this.stockNoteStockRepository.findStockNoteStockEntitiesByCustomerIdAndTickerSymbol( customerId, tickerSymbol );
        logMethodEnd( methodName, stockNoteStockEntities.size() );
        return null;// stockNoteStockEntities;
    }

    @Override
    protected StockNoteStockDTO entityToDTO( final StockNoteStockEntity stockNoteStockEntity )
    {
        Objects.requireNonNull( stockNoteStockEntity );
        StockNoteStockDTO stockNoteStockDTO = StockNoteStockDTO.newInstance();
        BeanUtils.copyProperties( stockNoteStockEntity, stockNoteStockDTO );
        stockNoteStockDTO.setStockNotesId( stockNoteStockEntity.getStockNoteEntity().getId() );
        return stockNoteStockDTO;
    }

    @Override
    protected StockNoteStockEntity dtoToEntity( final StockNoteStockDTO stockNoteStockDTO )
    {
        Objects.requireNonNull( stockNoteStockDTO );
        StockNoteStockEntity stockNoteStockEntity = StockNoteStockEntity.newInstance();
        BeanUtils.copyProperties( stockNoteStockDTO, stockNoteStockEntity );
        return stockNoteStockEntity;
    }
}
