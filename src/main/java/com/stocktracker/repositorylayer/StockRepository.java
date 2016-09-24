package com.stocktracker.repositorylayer;

import com.stocktracker.repositorylayer.db.entity.StockEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Stock Entity Repository
 * Created by mike on 9/11/2016.
 */
public interface StockRepository extends PagingAndSortingRepository<StockEntity,String>
{
}
