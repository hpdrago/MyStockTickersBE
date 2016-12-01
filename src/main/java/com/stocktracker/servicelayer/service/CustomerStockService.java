package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.db.entity.CustomerStockEntity;
import com.stocktracker.repositorylayer.db.entity.CustomerStockEntityPK;
import com.stocktracker.repositorylayer.exceptions.CustomerStockNotFound;
import com.stocktracker.servicelayer.entity.CustomerStockDE;
import org.springframework.stereotype.Service;

/**
 * This service communicates between the Web layer and Repositories using the Domain Model
 * It is a stateless instance that provides business level services.
 *
 * It provides services for the CustomerStockEntity and CustomerStockDE
 *
 * Created by mike on 10/23/2016.
 */
@Service
public class CustomerStockService extends BaseService implements MyLogger
{
    /**
     * Get the customer stock entry for the customer id and the ticker symbol
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    public CustomerStockDE getCustomerStock( final int customerId, final String tickerSymbol )
        throws CustomerStockNotFound
    {
        final String methodName = "getCustomerSTock";
        logMethodBegin( methodName, customerId, tickerSymbol );
        CustomerStockEntityPK customerStockEntityPK = new CustomerStockEntityPK( customerId, tickerSymbol );
        CustomerStockEntity customerStockEntity = customerStockRepository.findOne( customerStockEntityPK );
        if ( customerStockEntity == null )
        {
            throw new CustomerStockNotFound( customerStockEntityPK );
        }
        CustomerStockDE customerStockDE = CustomerStockDE.newInstance( customerStockEntity );
        logMethodEnd( methodName, customerStockDE );
        return customerStockDE;
    }

    /**
     * Determines if the customer stock entry is in the database
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    public boolean isStockExists( final int customerId, final String tickerSymbol )
    {
        final String methodName = "isStockExists";
        logMethodBegin( methodName, customerId, tickerSymbol );
        /*
         * Get the stock from the database
         */
        CustomerStockEntityPK customerStockEntityPK = new CustomerStockEntityPK( customerId, tickerSymbol );
        boolean exists = customerStockRepository.exists( customerStockEntityPK );
        logMethodEnd( methodName, exists );
        return exists;
    }

    /**
     * Add a new customer stock to the database
     * @param customerStockDE
     * @return
     */
    public CustomerStockDE addCustomerStock( final CustomerStockDE customerStockDE )
    {
        final String methodName = "addCustomerStock";
        logMethodBegin( methodName, customerStockDE );
        CustomerStockEntity customerStockEntity = CustomerStockEntity.newInstance( customerStockDE );
        CustomerStockEntity returnCustomerStockEntity = customerStockRepository.save( customerStockEntity );
        CustomerStockDE returnCustomerStockDE = CustomerStockDE.newInstance( returnCustomerStockEntity );
        logMethodEnd( methodName, returnCustomerStockDE );
        return returnCustomerStockDE;
    }
}
