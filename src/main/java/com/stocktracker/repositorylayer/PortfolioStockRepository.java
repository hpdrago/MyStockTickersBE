package com.stocktracker.repositorylayer;

import com.stocktracker.repositorylayer.db.entity.PortfolioStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mike on 10/30/2016.
 */
public interface PortfolioStockRepository extends JpaRepository<PortfolioStockEntity, Integer>
{
    List<PortfolioStockEntity> findByPortfolioIdOrderByTickerSymbol( int portfolioId );
}
