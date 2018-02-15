package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockPositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TradeItAccount entity repository
 *
 * Created by mike on 12/4/2017.
 */
public interface StockPositionRepository extends JpaRepository<StockPositionEntity, Integer>
{
}
