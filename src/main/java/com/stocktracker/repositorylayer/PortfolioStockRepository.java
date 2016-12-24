package com.stocktracker.repositorylayer;

import com.stocktracker.repositorylayer.db.entity.PortfolioStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mike on 10/30/2016.
 */
public interface PortfolioStockRepository extends JpaRepository<PortfolioStockEntity, Integer>
{
    /**
     * Find one by the primary key
     * @param portfolioId
     * @return
     */
    List<PortfolioStockEntity> findByCustomerIdAndPortfolioIdOrderByTickerSymbol( final int customerId,
                                                                                  final int portfolioId );

    /**
     * Find one by the secondary key
     * @param customerId
     * @param portfolioId
     * @param tickerSymbol
     * @return
     */
    PortfolioStockEntity findFirstByCustomerIdAndPortfolioIdAndTickerSymbol( final int customerId,
                                                                             final int portfolioId,
                                                                             final String tickerSymbol );


 }
