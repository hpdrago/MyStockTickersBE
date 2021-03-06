package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Customer entity repositorylayer
 * <p>
 * Created by mike on 5/14/2016.
 */
@Transactional( readOnly = true )
public interface CustomerRepository extends JpaRepository<CustomerEntity,UUID>
{
    /**
     * Finds the customer for the email. Email address are unique
     *
     * @param email
     * @return
     */
    CustomerEntity findByEmail( final String email );

    @Override
    @Transactional
    @Modifying
    CustomerEntity save( CustomerEntity customerEntity );

    @Override
    @Transactional
    @Modifying
    <S extends CustomerEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<CustomerEntity> iterable );
}
