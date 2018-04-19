package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Customer entity repositorylayer
 * <p>
 * Created by mike on 5/14/2016.
 */
@Transactional( readOnly = true )
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer>
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
    <S extends CustomerEntity> List<S> save( Iterable<S> iterable );

    @Override
    @Transactional
    @Modifying
    <S extends CustomerEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( Integer integer );

    @Override
    @Transactional
    @Modifying
    void delete( Iterable<? extends CustomerEntity> iterable );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<CustomerEntity> iterable );
}
