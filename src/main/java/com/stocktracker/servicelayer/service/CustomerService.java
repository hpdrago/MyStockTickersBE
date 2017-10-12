package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.CustomerNotFoundException;
import com.stocktracker.repositorylayer.entity.CustomerEntity;
import com.stocktracker.repositorylayer.entity.PortfolioEntity;
import com.stocktracker.repositorylayer.repository.CustomerRepository;
import com.stocktracker.repositorylayer.repository.PortfolioRepository;
import com.stocktracker.weblayer.dto.CustomerDTO;
import com.stocktracker.weblayer.dto.PortfolioDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * This service communicates between the Web layer and Repositories using the Domain Model
 * It is a stateless instance that provides business level services
 *
 * Created by mike on 5/15/2016.
 */
@Service
@Transactional
public class CustomerService extends BaseService<CustomerEntity, CustomerDTO> implements MyLogger
{
    private PortfolioService portfolioService;
    private CustomerRepository customerRepository;
    private PortfolioRepository portfolioRepository;

    @Autowired
    public void setPortfolioRepository( final PortfolioRepository portfolioRepository )
    {
        this.portfolioRepository = portfolioRepository;
    }

    @Autowired
    public void setCustomerRepository( final CustomerRepository customerRepository )
    {
        this.customerRepository = customerRepository;
    }

    @Autowired
    public void setPortfolioService( final PortfolioService portfolioService )
    {
        this.portfolioService = portfolioService;
    }

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
        CustomerDTO customerDE = loadCustomerDTO( customerEntity );
        logMethodEnd( methodName, customerDE );
        return customerDE;
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
        Objects.requireNonNull( email, "Email cannot be null" );
        CustomerEntity customerEntity = customerRepository.findByEmail( email );
        if ( customerEntity == null )
        {
            throw new CustomerNotFoundException( email );
        }
        CustomerDTO customerDE = loadCustomerDTO( customerEntity );
        logMethodEnd( methodName, customerDE );
        return customerDE;
    }

    /**
     * This method loads the CustomerDTO
     * @param customerEntity
     * @return
     */
    private CustomerDTO loadCustomerDTO( final CustomerEntity customerEntity )
    {
        final String methodName = "loadCustomerDTO";
        logMethodBegin( methodName, customerEntity );
        Objects.requireNonNull( customerEntity );
        CustomerDTO customerDTO = this.entityToDTO( customerEntity );
        /*
         * Get the portfolios for the customer from the database
         */
        List<PortfolioEntity> customerPortfolios = portfolioRepository.findByCustomerId( customerEntity.getId() );
        List<PortfolioDTO> customerDTOPortfolios = this.portfolioService.entitiesToDTOs( customerPortfolios );
        customerDTO.setPortfolios( customerDTOPortfolios );
        logMethodEnd( methodName, customerEntity );
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
        List<CustomerDTO> customerDTOs = this.entitiesToDTOs( customerEntities );
        logMethodEnd( methodName, customerDTOs );
        return customerDTOs;
    }

    @Override
    protected CustomerDTO entityToDTO( final CustomerEntity entity )
    {
        Objects.requireNonNull( entity );
        CustomerDTO customerDTO = CustomerDTO.newInstance();
        BeanUtils.copyProperties( entity, customerDTO );
        return customerDTO;
    }

    @Override
    protected CustomerEntity dtoToEntity( final CustomerDTO dto )
    {
        Objects.requireNonNull( dto );
        CustomerEntity customerEntity = CustomerEntity.newInstance();
        BeanUtils.copyProperties( dto, customerEntity );
        return customerEntity;
    }

}
