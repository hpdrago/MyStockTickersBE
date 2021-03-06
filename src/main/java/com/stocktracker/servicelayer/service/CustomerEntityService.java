package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.CustomerNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.CustomerEntity;
import com.stocktracker.repositorylayer.entity.PortfolioEntity;
import com.stocktracker.repositorylayer.repository.CustomerRepository;
import com.stocktracker.repositorylayer.repository.PortfolioRepository;
import com.stocktracker.weblayer.dto.CustomerDTO;
import com.stocktracker.weblayer.dto.PortfolioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * This service communicates between the Web layer and Repositories using the Domain Model
 * It is a stateless instance that provides business level services
 *
 * Created by mike on 5/15/2016.
 */
@Service
public class CustomerEntityService extends UuidEntityService<CustomerEntity,
                                                             CustomerDTO,
                                                             CustomerRepository>
{
    private PortfolioEntityService portfolioService;
    private CustomerRepository customerRepository;
    private PortfolioRepository portfolioRepository;

    /**
     * Get the customer by id request
     * @param id
     * @return
     */
    public CustomerDTO getCustomerDTO( final UUID id )
        throws CustomerNotFoundException
    {
        final String methodName = "getCustomerDTO";
        logMethodBegin( methodName, id );
        final CustomerEntity customerEntity = getCustomerEntity( id );
        final CustomerDTO customerDTO = loadCustomerDTO( customerEntity );
        logMethodEnd( methodName, customerDTO );
        return customerDTO;
    }

    /**
     * Gets the customer entity for the {@code customerId}
     * @param customerUuid
     * @return
     * @throws CustomerNotFoundException
     */
    public CustomerEntity getCustomerEntity( final UUID customerUuid )
        throws CustomerNotFoundException
    {
        final String methodName = "getCustomerEntity";
        logMethodBegin( methodName, customerUuid );
        CustomerEntity customerEntity;
        try
        {
            customerEntity = this.getEntity( customerUuid );
        }
        catch( VersionedEntityNotFoundException e )
        {
            throw new CustomerNotFoundException( customerUuid );
        }
        logMethodEnd( methodName, customerEntity );
        return customerEntity;
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
     * This method loads the CustomerDTO including the portfolio information.
     * @param customerEntity
     * @return
     */
    private CustomerDTO loadCustomerDTO( final CustomerEntity customerEntity )
    {
        final String methodName = "loadCustomerDTO";
        logMethodBegin( methodName, customerEntity );
        Objects.requireNonNull( customerEntity );
        final CustomerDTO customerDTO = this.entityToDTO( customerEntity );
        /*
         * Get the portfolios for the customer from the database
         */
        final List<PortfolioEntity> customerPortfolios = this.portfolioRepository.findByCustomerUuid( customerEntity.getId() );
        final List<PortfolioDTO> customerDTOPortfolios = this.portfolioService.entitiesToDTOs( customerPortfolios );
        customerDTO.setPortfolios( customerDTOPortfolios );
        logMethodEnd( methodName, customerEntity );
        return customerDTO;
    }

    /**
     * Get all of the customers
     * @return
     */
    public List<CustomerDTO> getAllCustomers()
        throws CustomerNotFoundException
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
    protected CustomerDTO createDTO()
    {
        return this.context.getBean( CustomerDTO.class );
    }

    @Override
    protected CustomerEntity createEntity()
    {
        return this.context.getBean( CustomerEntity.class );
    }

    @Override
    protected CustomerRepository getRepository()
    {
        return this.customerRepository;
    }

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
    public void setPortfolioService( final PortfolioEntityService portfolioService )
    {
        this.portfolioService = portfolioService;
    }
}
