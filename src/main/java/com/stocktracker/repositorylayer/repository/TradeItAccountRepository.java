package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * TradeItAccount entity repository
 *
 * Created by mike on 12/4/2017.
 */
public interface TradeItAccountRepository extends JpaRepository<TradeItAccountEntity,Integer>
{
    /**
     * Get the list of accounts by the customerId.
     * @param customerId
     * @return
     */
    List<TradeItAccountEntity> findByCustomerId( final int customerId );

    /**
     * Get the customer's account by the name of the account.
     * @param customerId
     * @param name
     * @return
     */
    TradeItAccountEntity findByCustomerIdAndName( final int customerId, final String name );
}
