package com.stocktracker.repositorylayer;

import com.stocktracker.repositorylayer.db.entity.CustomerStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mike on 10/30/2016.
 */
public interface CustomerStockRepository extends JpaRepository<CustomerStockEntity, Integer>
{
}
