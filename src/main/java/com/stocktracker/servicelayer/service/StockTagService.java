package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.CustomerTagEntity;
import com.stocktracker.repositorylayer.entity.StockTagEntity;
import com.stocktracker.repositorylayer.entity.VStockTagEntity;
import com.stocktracker.repositorylayer.repository.CustomerTagRepository;
import com.stocktracker.repositorylayer.repository.StockTagRepository;
import com.stocktracker.repositorylayer.repository.VStockTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

/**
 * This service class implements the methods to save and retrieve stock tags to and from the database.
 */
@Service
public class StockTagService implements MyLogger
{
    private StockTagRepository stockTagRepository;
    private CustomerTagRepository customerTagRepository;
    private VStockTagRepository vStockTagRepository;

    /**
     * Saves any new tags into the customer tag and stock tag tables
     * @param referenceType
     * @param customerUuid
     * @param entityUuid
     * @param tags
     */
    public void saveStockTags( final UUID customerUuid,
                               final String tickerSymbol,
                               final StockTagEntity.StockTagReferenceType referenceType,
                               final UUID entityUuid,
                               final String[] tags )
    {
        final String methodName = "saveStockTags";
        /*
         * Remove any duplicates from the tags
         */
        logMethodBegin( methodName, customerUuid, tickerSymbol, referenceType, entityUuid, tags );
        Objects.requireNonNull( customerUuid, "customerUuid cannot be null" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Objects.requireNonNull( referenceType, "referenceType cannot be null" );
        Objects.requireNonNull( entityUuid, "entityUuid cannot be null" );
        Objects.requireNonNull( tags, "tags cannot be null" );
        if ( tags != null )
        {
            Set<String> uniqueTags = new TreeSet<>();
            for ( String tag : tags )
            {
                uniqueTags.add( tag );
            }

            for ( String tag : uniqueTags )
            {
                /*
                 * Set the unique key values to see if the tag exists
                 */
                StockTagEntity stockTagEntity = new StockTagEntity();
                stockTagEntity.setReferenceUuid( entityUuid );
                stockTagEntity.setReferenceType( referenceType.getReferenceType() );
                Example<StockTagEntity> stockTagEntityExample = Example.of( stockTagEntity );
                /*
                 * Check if the stock tag exists or not, if it exists, then we assume the
                 * customer tag exists as well.
                 */
                if ( !this.stockTagRepository.exists( stockTagEntityExample ) )
                {
                    logDebug( methodName, "stockTagEntity does not exist: {0}", stockTagEntity );
                    /*
                     * Check to see if the customer tag needs to be created
                     */
                    CustomerTagEntity customerTagEntity = new CustomerTagEntity();
                    customerTagEntity.setCustomerUuid( customerUuid );
                    customerTagEntity.setTagName( tag );
                    Example<CustomerTagEntity> customerTagEntityExample = Example.of( customerTagEntity );
                    customerTagEntity = this.customerTagRepository.findOne( customerTagEntityExample );
                    if ( customerTagEntity == null )
                    {
                        customerTagEntity = new CustomerTagEntity();
                        customerTagEntity.setCustomerUuid( customerUuid );
                        customerTagEntity.setTagName( tag );
                        logDebug( methodName, "adding customer tag: {0}", customerTagEntity );
                        customerTagEntity = this.customerTagRepository.save( customerTagEntity );
                        logDebug( methodName, "added customer tag: {0}", customerTagEntity );
                    }
                    stockTagEntity.setTickerSymbol( tickerSymbol );
                    stockTagEntity.setCustomerTagUuid( customerTagEntity.getUuid() );
                    logDebug( methodName, "adding stock tag: {0}", stockTagEntity );
                    this.stockTagRepository.save( stockTagEntity );
                }
            }
        }
        logMethodEnd( methodName );
    }

    /**
     * Retrieves stock tags for a customer
     * @param referenceType
     * @param customerUuid
     * @param entityId
     * @return
     */
    public List<String> findStockTags( final UUID customerUuid,
                                       final StockTagEntity.StockTagReferenceType referenceType,
                                       final UUID entityId )
    {
        List<String> tags = new ArrayList<>();
        VStockTagEntity stockTagEntity = new VStockTagEntity();
        stockTagEntity.setCustomerUuid( customerUuid );
        stockTagEntity.setReferenceUuid( entityId );
        stockTagEntity.setReferenceType( referenceType.getReferenceType() );
        Example<VStockTagEntity> stockTagEntityExample = Example.of( stockTagEntity );
        List<VStockTagEntity> stockTagEntities = this.vStockTagRepository
                                                     .findAll( stockTagEntityExample );
        for ( VStockTagEntity stockTag: stockTagEntities )
        {
            tags.add( stockTag.getTagName() );
        }
        return tags;
    }

    /**
     * Inject the repository
     * @param stockTagRepository
     */
    @Autowired
    public void setStockTagRepository( final StockTagRepository stockTagRepository )
    {
        this.stockTagRepository = stockTagRepository;
    }

    @Autowired
    public void setCustomerTagRepository( final CustomerTagRepository customerTagRepository )
    {
        this.customerTagRepository = customerTagRepository;
    }

    @Autowired
    public void setvStockTagRepository( final VStockTagRepository vStockTagRepository )
    {
        this.vStockTagRepository = vStockTagRepository;
    }

}
