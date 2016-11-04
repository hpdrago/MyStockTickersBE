package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.CustomerRepository;
import com.stocktracker.repositorylayer.PortfolioRepository;
import com.stocktracker.repositorylayer.PortfolioStockRepository;
import com.stocktracker.repositorylayer.StockRepository;
import com.stocktracker.repositorylayer.VPortfolioStockRepository;
import com.stocktracker.servicelayer.service.listcopy.ListCopyCustomerDomainEntityToCustomerDTO;
import com.stocktracker.servicelayer.service.listcopy.ListCopyCustomerEntityToCustomerDomainEntity;
import com.stocktracker.servicelayer.service.listcopy.ListCopyCustomerStockDomainEntityToCustomerStockDTO;
import com.stocktracker.servicelayer.service.listcopy.ListCopyPortfolioDomainEntityToPortfolioDTO;
import com.stocktracker.servicelayer.service.listcopy.ListCopyPortfolioEntityToPortfolioDomainEntity;
import com.stocktracker.servicelayer.service.listcopy.ListCopyStockDomainEntityToStockDTO;
import com.stocktracker.servicelayer.service.listcopy.ListCopyStockEntityToStockDomainEntity;
import com.stocktracker.servicelayer.service.listcopy.ListCopyVPortfolioStockEntityToCustomerStockDomainEntity;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by mike on 11/1/2016.
 */
public class BaseService implements MyLogger
{
    /***********************************************
     *  R E P O S I T O R I E S
     **********************************************/
    protected CustomerRepository customerRepository;
    protected PortfolioRepository portfolioRepository;
    protected PortfolioStockRepository portfolioStockRepository;
    protected VPortfolioStockRepository vPortfolioStockRepository;
    protected StockRepository stockRepository;

    /***********************************************
     *  L I S T  B E A N  C O P I E R S
     **********************************************/
    protected ListCopyVPortfolioStockEntityToCustomerStockDomainEntity listCopyVPortfolioStockEntityToCustomerStockDomainEntity;
    protected ListCopyCustomerStockDomainEntityToCustomerStockDTO listCopyCustomerStockDomainEntityToCustomerStockDTO;
    protected ListCopyCustomerEntityToCustomerDomainEntity listCopyCustomerEntityToCustomerDomainEntity;
    protected ListCopyCustomerDomainEntityToCustomerDTO listCopyCustomerDomainEntityToCustomerDTO;
    protected ListCopyPortfolioEntityToPortfolioDomainEntity listCopyPortfolioEntityToPortfolioDomainEntity;
    protected ListCopyPortfolioDomainEntityToPortfolioDTO listCopyPortfolioDomainEntityToPortfolioDTO;
    protected ListCopyStockEntityToStockDomainEntity listCopyStockEntityToStockDomainEntity;
    protected ListCopyStockDomainEntityToStockDTO listCopyStockDomainEntityToStockDTO;

    /**
     * Dependency injection of the StockRepository
     *
     * @param stockRepository
     */
    @Autowired
    public void setStockRepository( final StockRepository stockRepository )
    {
        final String methodName = "setStockRepository";
        logDebug( methodName, "Dependency Injection of: " + stockRepository );
        this.stockRepository = stockRepository;
    }

    @Autowired
    public void setListCopyStockEntityToStockDomainEntity(
        final ListCopyStockEntityToStockDomainEntity listCopyStockEntityToStockDomainEntity )
    {
        this.listCopyStockEntityToStockDomainEntity = listCopyStockEntityToStockDomainEntity;
    }

    @Autowired
    public void setListCopyStockDomainEntityToStockDTO(
        final ListCopyStockDomainEntityToStockDTO listCopyStockDomainEntityToStockDTO )
    {
        this.listCopyStockDomainEntityToStockDTO = listCopyStockDomainEntityToStockDTO;
    }

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
     * Dependency injection of the PortfolioRepository
     * @param portfolioRepository
     */
    @Autowired
    public void setPortfolioRepository( final PortfolioRepository portfolioRepository )
    {
        final String methodName = "setPortfolioRepository";
        logDebug( methodName, "Dependency Injection of: " + portfolioRepository );
        this.portfolioRepository = portfolioRepository;
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
     * Dependency injection of the ListCopyPortfolioDoToPortfolioDo
     * @param
     */
    @Autowired
    public void setListCopyPortfolioDoToPortfolioDo( final ListCopyPortfolioDomainEntityToPortfolioDTO listCopyPortfolioDomainEntityToPortfolioDTO )
    {
        final String methodName = "setListCopyPortfolioDoToPortfolioDo";
        logDebug( methodName, "Dependency Injection of: " + listCopyPortfolioDomainEntityToPortfolioDTO );
        this.listCopyPortfolioDomainEntityToPortfolioDTO = listCopyPortfolioDomainEntityToPortfolioDTO;
    }

    /**
     * Dependency injection of the VPortfolioStockRepository
     * @param vPortfolioStockRepository
     */
    @Autowired
    public void setVPortfolioStockRepository( final VPortfolioStockRepository vPortfolioStockRepository )
    {
        final String methodName = "setPortfolioStockRepository";
        logDebug( methodName, "Dependency Injection of: " + vPortfolioStockRepository );
        this.vPortfolioStockRepository = vPortfolioStockRepository;
    }

    /**
     * Dependency injection of the PortfolioStockRepository
     * @param portfolioStockRepository
     */
    @Autowired
    public void setPortfolioStockRepository( final PortfolioStockRepository portfolioStockRepository )
    {
        final String methodName = "setPortfolioStockRepository";
        logDebug( methodName, "Dependency Injection of: " + portfolioStockRepository );
        this.portfolioStockRepository = portfolioStockRepository;
    }

    /**
     * Dependency injection of the PortfolioRepository
     * @param portfolioStockRepository
     */
    @Autowired
    public void setPortfolioRepository( final PortfolioStockRepository portfolioStockRepository )
    {
        final String methodName = "setPortfolioRepository";
        logDebug( methodName, "Dependency Injection of: " + portfolioStockRepository );
        this.portfolioStockRepository = portfolioStockRepository;
    }

    /**
     * Dependency injection of the ListCopyPortfolioEntityToPortfolioDo
     * @param listCopyPortfolioEntityToPortfolioDomainEntity
     */
    @Autowired
    public void setListCopyPortfolioEntityToPortfolioDomainEntity(
        final ListCopyPortfolioEntityToPortfolioDomainEntity listCopyPortfolioEntityToPortfolioDomainEntity )
    {
        final String methodName = "setListCopyPortfolioEntityToPortfolioDo";
        logDebug( methodName, "Dependency Injection of: " + listCopyPortfolioEntityToPortfolioDomainEntity );
        this.listCopyPortfolioEntityToPortfolioDomainEntity = listCopyPortfolioEntityToPortfolioDomainEntity;
    }

    /**
     * Dependency injection of the ListCopyPortfolioDoToPortfolioDo
     * @param
     */
    @Autowired
    public void setListCopyPortfolioDomainEntityToPortfolioDTO (
        final ListCopyPortfolioDomainEntityToPortfolioDTO listCopyPortfolioDomainEntityToPortfolioDTO )
    {
        final String methodName = "setListCopyPortfolioDomainEntityToPortfolioDTO";
        logDebug( methodName, "Dependency Injection of: " + listCopyPortfolioDomainEntityToPortfolioDTO );
        this.listCopyPortfolioDomainEntityToPortfolioDTO = listCopyPortfolioDomainEntityToPortfolioDTO;
    }

    /**
     * Dependency injection of the ListCopyVPortfolioStockEntityToCustomerStockDomainEntity
     * @param
     */
    @Autowired
    public void setListCopyVPortfolioStockEntityToCustomerStockDomainEntity(
        ListCopyVPortfolioStockEntityToCustomerStockDomainEntity listCopyVPortfolioStockEntityToCustomerStockDomainEntity )
    {
        final String methodName = "setListCopyVPortfolioStockEntityToCustomerStockDomainEntity";
        logDebug( methodName, "Dependency Injection of: " + listCopyVPortfolioStockEntityToCustomerStockDomainEntity );
        this.listCopyVPortfolioStockEntityToCustomerStockDomainEntity = listCopyVPortfolioStockEntityToCustomerStockDomainEntity;
    }

    /**
     * Dependency injection of the ListCopyCustomerStockDomainEntityToCustomerStockDTO
     * @param
     */
    @Autowired
    public void setListCopyCustomerStockDomainEntityToCustomerStockDTO(
        ListCopyCustomerStockDomainEntityToCustomerStockDTO listCopyCustomerStockDomainEntityToCustomerStockDTO )
    {
        final String methodName = "setListCopyCustomerStockDomainEntityToCustomerStockDTO";
        logDebug( methodName, "Dependency Injection of: " + listCopyCustomerStockDomainEntityToCustomerStockDTO );
        this.listCopyCustomerStockDomainEntityToCustomerStockDTO = listCopyCustomerStockDomainEntityToCustomerStockDTO;
    }
}
