package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * TradeItAccount entity repository
 *
 * Created by mike on 12/4/2017.
 */
public interface LinkedAccountRepository extends JpaRepository<LinkedAccountEntity,Integer>
{
    /**
     * Get all of the linked accounts by customer id and parent account id.  It is not really necessary to include the
     * customer id, but to be safe, let's make sure we only look at the customer's accounts.
     * @param customerId
     * @param tradeItAccountId
     * @return
     */
    List<LinkedAccountEntity> findAllByCustomerIdAndTradeItAccountId( final int customerId, final int tradeItAccountId );
}
