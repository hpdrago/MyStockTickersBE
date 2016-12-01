package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.db.entity.PortfolioStockEntity;
import com.stocktracker.servicelayer.entity.PortfolioStockDE;
import org.springframework.stereotype.Service;

/**
 * This is the service class for portfolio stock related methods.
 *
 * Created by mike on 11/26/2016.
 */
@Service
public class PortfolioStockService extends BaseService implements MyLogger
{
    public PortfolioStockDE addPortfolioStock( int portfolioId, String tickerSymbol )
    {
        final String methodName = "addPortfolioSTock";
        logMethodBegin( methodName, portfolioId, tickerSymbol );
        PortfolioStockEntity portfolioStockEntity = PortfolioStockEntity.newInstance( portfolioId, tickerSymbol );
        portfolioStockEntity = portfolioStockRepository.save( portfolioStockEntity );
        PortfolioStockDE portfolioStockDE = PortfolioStockDE.newInstance( portfolioStockEntity );
        logMethodEnd( methodName, portfolioStockDE );
        return portfolioStockDE;
    }

}
