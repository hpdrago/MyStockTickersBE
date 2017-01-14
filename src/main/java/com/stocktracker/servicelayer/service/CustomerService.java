package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.CustomerNotFoundException;
import com.stocktracker.repositorylayer.db.entity.CustomerEntity;
import com.stocktracker.repositorylayer.db.entity.PortfolioEntity;
import com.stocktracker.servicelayer.entity.CustomerDE;
import com.stocktracker.servicelayer.entity.PortfolioDE;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * This service communicates between the Web layer and Repositories using the Domain Model
 * It is a stateless instance that provides business level services
 *
 * Created by mike on 5/15/2016.
 */
@Service
public class CustomerService extends BaseService implements MyLogger
{
    /**
     * Get the customer by id request
     * @param id
     * @return
     */
    public CustomerDE getCustomerById( final int id )
    {
        final String methodName = "getCustomerById";
        logMethodBegin( methodName, id );
        /*
         * Get the customer entity
         */
        CustomerEntity customerEntity = customerRepository.findOne( id );
        if ( customerEntity == null )
        {
            throw new CustomerNotFoundException( id );
        }
        CustomerDE customerDE = loadCustomerDE( customerEntity );
        logMethodEnd( methodName, customerDE );
        return customerDE;
    }

    /**
     * Get the customer by email request
     * @param email
     * @return
     * @throws CustomerNotFoundException
     */
    public CustomerDE getCustomerByEmail( final String email )
        throws CustomerNotFoundException
    {
        final String methodName = "getCustomerByEmail";
        logMethodBegin( methodName, email );
        Objects.requireNonNull( email, "Email cannot be null" );
        CustomerEntity customerEntity = customerRepository.findByEmail( email );
        if ( customerEntity == null )
        {
            throw new CustomerNotFoundException( email );
        }
        CustomerDE customerDE = loadCustomerDE( customerEntity );
        logMethodEnd( methodName, customerDE );
        return customerDE;
    }

    /**
     * This method loads the CustomerDTO
     * @param customerEntity
     * @return
     */
    private CustomerDE loadCustomerDE( final CustomerEntity customerEntity )
    {
        Objects.requireNonNull( customerEntity );
        CustomerDE customerDE = CustomerDE.newInstance( customerEntity );
        /*
         * Get the portfolios for the customer from the database
         */
        List<PortfolioEntity> customerPortfolios = portfolioRepository.findByCustomerId( customerEntity.getId() );
        List<PortfolioDE> customerDEPortfolios = listCopyPortfolioEntityToPortfolioDE.copy( customerPortfolios );
        customerDE.setPortfolios( customerDEPortfolios );
        return customerDE;
    }

    /**
     * Get all of the customers
     * @return
     */
    public List<CustomerDE> getAllCustomers()
    {
        final String methodName = "getAllCustomers";
        logMethodBegin( methodName );
        List<CustomerEntity> customerEntities = customerRepository.findAll();
        if ( customerEntities == null )
        {
            throw new CustomerNotFoundException( "There are no customers" );
        }
        List<CustomerDE> customerDEs = listCopyCustomerEntityToCustomerDE.copy( customerEntities );
        logMethodEnd( methodName, customerDEs );
        return customerDEs;
    }
}
