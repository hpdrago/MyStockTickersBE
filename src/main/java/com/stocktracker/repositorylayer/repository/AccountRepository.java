package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Account entity repository
 *
 * Created by mike on 12/4/2017.
 */
public interface AccountRepository extends JpaRepository<AccountEntity, Integer>
{
    /**
     * Finds the customer by the id number.  Id numbers are unique as well
     * @param id
     * @return
     */
    AccountEntity findById( final int id );

    List<AccountEntity> findByCustomerId( final int customerId );

    AccountEntity findByCustomerIdAndName( final int customerId, final String name );
}
