package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.StockSectorEntity;
import com.stocktracker.repositorylayer.entity.StockSubSectorEntity;
import com.stocktracker.repositorylayer.repository.StockSectorRepository;
import com.stocktracker.weblayer.dto.StockSectorsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockSectorEntityService extends BaseEntityService<StockSectorEntity, StockSectorsDTO> implements MyLogger
{
    private StockSectorRepository stockSectorRepository;

    @Autowired
    public void setStockSectorRepository( final StockSectorRepository stockSectorRepository )
    {
        this.stockSectorRepository = stockSectorRepository;
    }

    /**
     * Get all of the stock sector information
     * @return
     */
    public List<StockSectorsDTO> getStockSectors()
    {
        final String methodName = "getStockSectors";
        logMethodBegin( methodName );
        List<StockSectorEntity> stockSectorEntities = stockSectorRepository.findAll();
        List<StockSectorsDTO> stockSectorList = this.entitiesToDTOs( stockSectorEntities );
        logMethodEnd( methodName );
        return stockSectorList;
    }

    /**
     * Get all of the stock sub sectors information
     * @return
     */
    public List<StockSubSectorEntity> getStockSubSectors()
    {
        final String methodName = "getStockSubSectors";
        logMethodBegin( methodName );
        /*
        List<StockSubSectorEntity> stockSubSectorEntities = stockSubSectorRepository.findAll();
        List<StockSubSectorDE> stockSubSectorList = listCopyStockSubSectorEntityToStockSubSectorDE.copy( stockSubSectorEntities );
        */
        logMethodEnd( methodName );
        //return stockSubSectorList;
        return null;
    }

    @Override
    protected StockSectorsDTO entityToDTO( final StockSectorEntity entity )
    {
        return null;
    }

    @Override
    protected StockSectorEntity dtoToEntity( final StockSectorsDTO dto )
    {
        return null;
    }
}
