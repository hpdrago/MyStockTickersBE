package com.stocktracker.servicelayer.service;

import com.stocktracker.servicelayer.service.listcopy.ListCopyCustomerDomainEntityToCustomerDTO;
import com.stocktracker.servicelayer.service.listcopy.ListCopyCustomerEntityToCustomerDomainEntity;
import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.db.entity.CustomerEntity;
import com.stocktracker.servicelayer.entity.CustomerDomainEntity;
import com.stocktracker.weblayer.dto.CustomerDTO;
import com.stocktracker.repositorylayer.exceptions.CustomerNotFoundException;
import com.stocktracker.repositorylayer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This service communicates between the Web layer and Repositories using the Domain Model
 * It is a stateless instance that provides business level services
 *
 * Created by mike on 5/15/2016.
 */
@Service
public class CustomerService implements MyLogger
{
    private CustomerRepository customerRepository;
    private ListCopyCustomerEntityToCustomerDomainEntity listCopyCustomerEntityToCustomerDomainEntity;
    private ListCopyCustomerDomainEntityToCustomerDTO listCopyCustomerDomainEntityToCustomerDTO;

    /**
     * Dependency injection of the CustomerRepository
     * @param customerRepository
     */
    @Autowired
    public void setCustomerRepository( final CustomerRepository customerRepository )
    {
        final String methodName = "setCustomerRepository";
        logDebug( methodName, "Dependency Injection of: " + customerRepository );
        this.customerRepository = customerRepository;
    }

    /**
     * Dependency injection of the ListCopyCustomerEntityToCustomerDo
     * @param listCopyCustomerEntityToCustomerDomainEntity
     */
    @Autowired
    public void setListCopyCustomerEntityToCustomerDomainEntity( final ListCopyCustomerEntityToCustomerDomainEntity listCopyCustomerEntityToCustomerDomainEntity )
    {
        final String methodName = "setListCopyCustomerEntityToCustomerDo";
        logDebug( methodName, "Dependency Injection of: " + listCopyCustomerEntityToCustomerDomainEntity );
        this.listCopyCustomerEntityToCustomerDomainEntity = listCopyCustomerEntityToCustomerDomainEntity;
    }

    /**
     * Dependency injection of the ListCopyCustomerDoToCustomerDo
     * @param
     */
    @Autowired
    public void setListCopyCustomerDoToCustomerDo( final ListCopyCustomerDomainEntityToCustomerDTO listCopyCustomerDomainEntityToCustomerDTO )
    {
        final String methodName = "setListCopyCustomerDoToCustomerDo";
        logDebug( methodName, "Dependency Injection of: " + listCopyCustomerDomainEntityToCustomerDTO );
        this.listCopyCustomerDomainEntityToCustomerDTO = listCopyCustomerDomainEntityToCustomerDTO;
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
        CustomerEntity customerEntity = customerRepository.findOne( id );
        if ( customerEntity == null )
        {
            throw new CustomerNotFoundException( id );
        }
        CustomerDomainEntity customerDomainEntity = CustomerDomainEntity.newInstance( customerEntity );
        CustomerDTO customerDTO = CustomerDTO.newInstance( customerDomainEntity );
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
        CustomerDomainEntity customerDomainEntity = CustomerDomainEntity.newInstance( customerEntity );
        CustomerDTO customerDTO = CustomerDTO.newInstance( customerDomainEntity );
        logMethodEnd( methodName, customerDTO );
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
