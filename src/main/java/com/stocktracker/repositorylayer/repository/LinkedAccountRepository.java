package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
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
public interface LinkedAccountRepository extends JpaRepository<LinkedAccountEntity, Integer>
{
    /**
     * Get all of the linked accounts by customer id and parent account id.  It is not really necessary to include the
     * customer id, but to be safe, let's make sure we only look at the customer's accounts.
     *
     * @param customerId
     * @param tradeItAccountId
     * @return
     */
    List<LinkedAccountEntity> findAllByCustomerIdAndTradeItAccountId( final int customerId, final int tradeItAccountId );

    @Override
    @Transactional
    @Modifying
    LinkedAccountEntity save( LinkedAccountEntity linkedAccountEntity );

    @Override
    @Transactional
    @Modifying
    <S extends LinkedAccountEntity> List<S> save( Iterable<S> iterable );

    @Override
    @Transactional
    @Modifying
    <S extends LinkedAccountEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( LinkedAccountEntity linkedAccountEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<LinkedAccountEntity> iterable );

    @Override
    @Transactional
    @Modifying
    void delete( Integer integer );
}
