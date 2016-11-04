package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.db.entity.CustomerEntity;
import com.stocktracker.repositorylayer.db.entity.PortfolioEntity;
import com.stocktracker.repositorylayer.exceptions.CustomerNotFoundException;
import com.stocktracker.servicelayer.entity.CustomerDomainEntity;
import com.stocktracker.servicelayer.entity.PortfolioDomainEntity;
import com.stocktracker.weblayer.dto.CustomerDTO;
import com.stocktracker.weblayer.dto.PortfolioDTO;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public CustomerDTO getCustomerById( final int id )
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
        CustomerDTO customerDTO = loadCustomerDTO( customerEntity );
        logMethodEnd( methodName, customerDTO );
        return customerDTO;
    }

    /**
     * Get the customer by email request
     * @param email
     * @return
     * @throws CustomerNotFoundException
     */
    public CustomerDTO getCustomerByEmail( final String email )
        throws CustomerNotFoundException
    {
        final String methodName = "getCustomerByEmail";
        logMethodBegin( methodName, email );
        CustomerEntity customerEntity = customerRepository.findByEmail( email );
        if ( customerEntity == null )
        {
            throw new CustomerNotFoundException( email );
        }
        CustomerDTO customerDTO = loadCustomerDTO( customerEntity );
        logMethodEnd( methodName, customerDTO );
        return customerDTO;
    }

    /**
     * This method loads the CustomerDTO
     * @param customerEntity
     * @return
     */
    private CustomerDTO loadCustomerDTO( final CustomerEntity customerEntity )
    {
        CustomerDomainEntity customerDomainEntity = CustomerDomainEntity.newInstance( customerEntity );
        CustomerDTO customerDTO = CustomerDTO.newInstance( customerDomainEntity );
        /*
         * Get the portfolios for the customer from the database
         */
        List<PortfolioEntity> customerPortfolios = portfolioRepository.findByCustomerId( customerEntity.getId() );
        List<PortfolioDomainEntity> customerDomainPortfolios = listCopyPortfolioEntityToPortfolioDomainEntity.copy( customerPortfolios );
        List<PortfolioDTO> customerPortfolioDTOs = listCopyPortfolioDomainEntityToPortfolioDTO.copy( customerDomainPortfolios );
        customerDTO.setPortfolios( customerPortfolioDTOs );
        return customerDTO;
    }

    /**
     * Get all of the customers
     * @return
     */
    public List<CustomerDTO> getAllCustomers()
    {
        final String methodName = "getAllCustomers";
        logMethodBegin( methodName );
        List<CustomerEntity> customerEntities = customerRepository.findAll();
        if ( customerEntities == null )
        {
            throw new CustomerNotFoundException( "There are no customers" );
        }
        List<CustomerDomainEntity> customerDomainEntities = listCopyCustomerEntityToCustomerDomainEntity.copy( customerEntities );
        List<CustomerDTO> customerDTOs = listCopyCustomerDomainEntityToCustomerDTO.copy( customerDomainEntities );
        logMethodEnd( methodName, customerDTOs );
        return customerDTOs;
    }
}
