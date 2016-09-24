package com.stocktracker.repositorylayer;

import com.stocktracker.repositorylayer.db.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Customer entity repositorylayer
 *
 * Created by mike on 5/14/2016.
 */
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer>
{
    /**
     * Finds the customer for the email. Email address are unique
     * @param email
     * @return
     */
    CustomerEntity findByEmail( final String email );
}
