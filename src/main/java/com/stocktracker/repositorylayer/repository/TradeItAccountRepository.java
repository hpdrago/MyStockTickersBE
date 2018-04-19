package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TradeItAccount entity repository
 * <p>
 * Created by mike on 12/4/2017.
 */
@Transactional( readOnly = true )
public interface TradeItAccountRepository extends JpaRepository<TradeItAccountEntity, Integer>
{
    /**
     * Get the list of accounts by the customerId.
     *
     * @param customerId
     * @return
     */
    List<TradeItAccountEntity> findByCustomerId( final int customerId );

    /**
     * Get the customer's account by the name of the account.
     *
     * @param customerId
     * @param name
     * @return
     */
    TradeItAccountEntity findByCustomerIdAndName( final int customerId, final String name );

    @Override
    @Transactional
    @Modifying
    TradeItAccountEntity save( TradeItAccountEntity tradeItAccountEntity );

    @Override
    @Transactional
    @Modifying
    <S extends TradeItAccountEntity> List<S> save( Iterable<S> iterable );

    @Override
    @Transactional
    @Modifying
    <S extends TradeItAccountEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( Integer integer );

    @Override
    @Transactional
    @Modifying
    void delete( TradeItAccountEntity tradeItAccountEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<TradeItAccountEntity> iterable );
}
