package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.StockSummaryEntity;
import com.stocktracker.repositorylayer.repository.StockSummaryRepository;
import com.stocktracker.weblayer.dto.StockSummaryDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class StockSummaryService extends BaseService<StockSummaryEntity, StockSummaryDTO> implements MyLogger
{
    private StockSummaryRepository stockSummaryRepository;

    /**
     * Get the list of all stock summaries for the customer
     * @param customerId
     * @return
     */
    public List<StockSummaryDTO> getStockSummaries( @NotNull final Integer customerId )
    {
        final String methodName = "getStockSummaries";
        logMethodBegin( methodName, customerId );
        List<StockSummaryEntity> stockSummaryEntities = this.stockSummaryRepository.findByCustomerIdOrderByTickerSymbol( customerId );
        List<StockSummaryDTO> stockSummaryDTOs = this.entitiesToDTOs( stockSummaryEntities );
        logMethodEnd( methodName, "Found " + stockSummaryEntities.size() + " summaries" );
        return stockSummaryDTOs;
    }

    @Override
    protected StockSummaryDTO entityToDTO( final StockSummaryEntity stockEntity )
    {
        Objects.requireNonNull( stockEntity );
        StockSummaryDTO stockDTO = StockSummaryDTO.newInstance();
        BeanUtils.copyProperties( stockEntity, stockDTO );
        return stockDTO;
    }

    @Override
    protected StockSummaryEntity dtoToEntity( final StockSummaryDTO stockDTO )
    {
        Objects.requireNonNull( stockDTO );
        StockSummaryEntity stockEntity = StockSummaryEntity.newInstance();
        BeanUtils.copyProperties( stockDTO, stockEntity );
        return stockEntity;
    }

    @Autowired
    public void setStockSummaryRepository( final StockSummaryRepository stockSummaryRepository )
    {
        this.stockSummaryRepository = stockSummaryRepository;
    }
}
