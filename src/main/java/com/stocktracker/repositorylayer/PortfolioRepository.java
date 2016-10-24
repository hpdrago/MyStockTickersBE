package com.stocktracker.repositorylayer;

import com.stocktracker.repositorylayer.db.entity.PortfolioEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by mike on 10/23/2016.
 */
public interface PortfolioRepository extends PagingAndSortingRepository<PortfolioEntity,Integer>
{
    List<PortfolioEntity> findByCustomerId( int customerId );
}
