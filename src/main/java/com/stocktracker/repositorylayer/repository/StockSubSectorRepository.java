package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockSubSectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This portfoliostock handles access to the stock_sub_sector table
 * Created by mike on 11/19/2016.
 */
public interface StockSubSectorRepository extends JpaRepository<StockSubSectorEntity, Integer>
{
}
