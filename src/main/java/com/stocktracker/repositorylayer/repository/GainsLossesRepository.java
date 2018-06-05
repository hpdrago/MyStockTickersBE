package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.GainsLossesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Created by mike on 05/28/2018.
 */
public interface GainsLossesRepository extends JpaRepository<GainsLossesEntity,UUID>
{
    /**
     * Get all of the stocks to buy records for a customer
     * @param customerUuid
     * @return
     */
    Page<GainsLossesEntity> findByCustomerUuid( final Pageable pageRequest, final UUID customerUuid );

    /**
     * Find the gains and losses for a customer.
     * @param customerUuid
     * @return
     */
    List<GainsLossesEntity> findByCustomerUuid( UUID customerUuid );

    /**
     * Find by customer uuid and ticker symbol.
     * @param pageRequest
     * @param customerUuid
     * @param tickerSymbol
     * @return
     */
    Page<GainsLossesEntity> findByCustomerUuidAndTickerSymbol( Pageable pageRequest, UUID customerUuid, String tickerSymbol );

    /**
     * Get all of the stocks to buy records for a customer and ticker symbol.
     * @param customerUuid
     * @return
     */
    Page<GainsLossesEntity> findByCustomerUuidAndLinkedAccountUuidAndTickerSymbol( final Pageable pageRequest,
                                                                                   final UUID customerUuid,
                                                                                   final UUID linkedAccountUuid,
                                                                                   final String tickerSymbol );

    /**
     * Get all of the stocks to buy for the customer and the ticker symbol
     *
     * @param customerUuid
     * @param tickerSymbol
     * @return
     */
    GainsLossesEntity findByCustomerUuidAndLinkedAccountUuidAndTickerSymbol( final UUID customerUuid,
                                                                             final UUID linkedAccountUuid,
                                                                             final String tickerSymbol );

    @Override
    @Transactional
    @Modifying
    GainsLossesEntity save( GainsLossesEntity stockToBuyEntity );

    @Override
    @Transactional
    @Modifying
    <S extends GainsLossesEntity> List<S> save( Iterable<S> iterable );

    @Override
    @Transactional
    @Modifying
    <S extends GainsLossesEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( final UUID uuid );

    @Override
    @Transactional
    @Modifying
    void delete( GainsLossesEntity stockToBuyEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<GainsLossesEntity> iterable );

}
