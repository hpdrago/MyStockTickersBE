package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.VPortfolioStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mike on 10/30/2016.
 */
public interface VPortfolioStockRepository extends JpaRepository<VPortfolioStockEntity, Integer>
{
    List<VPortfolioStockEntity> findByPortfolioIdOrderByTickerSymbol( int portfolioId );
}
