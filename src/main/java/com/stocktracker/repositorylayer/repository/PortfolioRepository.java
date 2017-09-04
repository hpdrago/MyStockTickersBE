package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.PortfolioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mike on 10/23/2016.
 */
public interface PortfolioRepository extends JpaRepository<PortfolioEntity,Integer>
{
    /**
     * Get a list of the portfolios for a customer
     * @param customerId
     * @return
     */
    List<PortfolioEntity> findByCustomerId( int customerId );
}
