package com.stocktracker.servicelayer.service;

import com.stocktracker.common.ListCopyStockDomainEntityToStockDTO;
import com.stocktracker.common.ListCopyStockEntityToStockDomainEntity;
import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.StockRepository;
import com.stocktracker.repositorylayer.db.entity.StockEntity;
import com.stocktracker.servicelayer.entity.StockDomainEntity;
import com.stocktracker.weblayer.dto.StockDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mike on 9/11/2016.
 */
@Service
public class StockService implements MyLogger
{
    private StockRepository stockRepository;
    private ListCopyStockEntityToStockDomainEntity listCopyStockEntityToStockDomainEntity;
    private ListCopyStockDomainEntityToStockDTO listCopyStockDomainEntityToStockDTO;

    /**
     * Dependency injection of the StockRepository
     *
     * @param stockRepository
     */
    @Autowired
    public void setStockRepository( final StockRepository stockRepository )
    {
        final String methodName = "setStockRepository";
        logDebug( methodName, "Dependency Injection of: " + stockRepository );
        this.stockRepository = stockRepository;
    }

    @Autowired
    public void setListCopyStockEntityToStockDomainEntity(
        final ListCopyStockEntityToStockDomainEntity listCopyStockEntityToStockDomainEntity )
    {
        this.listCopyStockEntityToStockDomainEntity = listCopyStockEntityToStockDomainEntity;
    }

    @Autowired
    public void setListCopyStockDomainEntityToStockDTO(
        final ListCopyStockDomainEntityToStockDTO listCopyStockDomainEntityToStockDTO )
    {
        this.listCopyStockDomainEntityToStockDTO = listCopyStockDomainEntityToStockDTO;
    }

    /**
     * Transforms {@code Page<ENTITY>} objects into {@code Page<DTO>} objects.
     *
     * @param pageRequest The information of the requested page.
     * @param source      The {@code Page<ENTITY>} object.
     * @return The created {@code Page<DTO>} object.
     */
    private Page<StockDTO> mapEntityPageIntoDTOPage( Pageable pageRequest, Page<StockEntity> source )
    {
        List<StockDomainEntity> stockDomainEntities = listCopyStockEntityToStockDomainEntity.copy( source.getContent() );
        List<StockDTO> stockDTOs = listCopyStockDomainEntityToStockDTO.copy( stockDomainEntities );
        return new PageImpl<>( stockDTOs, pageRequest, source.getTotalElements() );
    }

    /**
     * Get a page of StockDTO's
     * @param pageRequest
     * @return
     */
    public Page<StockDTO> getPage( final Pageable pageRequest )
    {
        final String methodName = "getPage";
        logMethodBegin( methodName, pageRequest );
        /*
         * Get the page from the database
         */
        Page<StockEntity> stockEntities = stockRepository.findAll( pageRequest );
        /*
         * Map from Entity to DomainEntity to DTO
         */
        Page<StockDTO> stockDTOPage = mapEntityPageIntoDTOPage( pageRequest, stockEntities );
        logMethodEnd( methodName, stockDTOPage );
        return stockDTOPage;
    }

    /**
     * Gets a single stock for the {@code tickerSymbol} from the database
     * @param tickerSymbol
     * @return
     */
    public StockDTO getStock( final String tickerSymbol )
    {
        final String methodName = "getStock";
        logMethodBegin( methodName, tickerSymbol );
        /*
         * Get the stock from the database
         */
        StockEntity stockEntity = stockRepository.findOne( tickerSymbol );
        /*
         * Map from Entity to DomainEntity to DTO
         */
        StockDTO stockDTO = new StockDTO();
        BeanUtils.copyProperties( stockEntity, stockDTO );
        logMethodEnd( methodName, stockDTO );
        return stockDTO;
    }
}
