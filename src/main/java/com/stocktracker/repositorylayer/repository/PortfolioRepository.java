package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.PortfolioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Created by mike on 10/23/2016.
 */

@Transactional( readOnly = true )
public interface PortfolioRepository extends JpaRepository<PortfolioEntity,UUID>
{
    /**
     * Get a list of the portfolios for a customer
     *
     * @param customerUuid
     * @return
     */
    List<PortfolioEntity> findByCustomerUuid( UUID customerUuid );

    @Override
    @Transactional
    @Modifying
    PortfolioEntity save( PortfolioEntity portfolioEntity );

    @Override
    @Transactional
    @Modifying
    <S extends PortfolioEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( PortfolioEntity portfolioEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<PortfolioEntity> iterable );
}
