package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.CustomerRepository;
import com.stocktracker.repositorylayer.CustomerStockRepository;
import com.stocktracker.repositorylayer.PortfolioRepository;
import com.stocktracker.repositorylayer.PortfolioStockRepository;
import com.stocktracker.repositorylayer.StockRepository;
import com.stocktracker.repositorylayer.StockSectorRepository;
import com.stocktracker.repositorylayer.StockSubSectorRepository;
import com.stocktracker.repositorylayer.VPortfolioStockRepository;
import com.stocktracker.servicelayer.service.listcopy.ListCopyCustomerEntityToCustomerDE;
import com.stocktracker.servicelayer.service.listcopy.ListCopyPortfolioEntityToPortfolioDomainEntity;
import com.stocktracker.servicelayer.service.listcopy.ListCopyStockEntityToStockDE;
import com.stocktracker.servicelayer.service.listcopy.ListCopyStockSectorEntityToStockSectorDE;
import com.stocktracker.servicelayer.service.listcopy.ListCopyStockSubSectorEntityToStockSubSectorDE;
import com.stocktracker.servicelayer.service.listcopy.ListCopyVPortfolioStockEntityToCustomerStockDE;
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
    protected CustomerStockRepository customerStockRepository;
    protected PortfolioRepository portfolioRepository;
    protected PortfolioStockRepository portfolioStockRepository;
    protected VPortfolioStockRepository vPortfolioStockRepository;
    protected StockRepository stockRepository;
    protected StockSectorRepository stockSectorRepository;
    protected StockSubSectorRepository stockSubSectorRepository;

    /***********************************************
     *  L I S T  B E A N  C O P I E R S
     **********************************************/
    protected ListCopyVPortfolioStockEntityToCustomerStockDE listCopyVPortfolioStockEntityToCustomerStockDE;
    protected ListCopyCustomerEntityToCustomerDE listCopyCustomerEntityToCustomerDE;
    protected ListCopyPortfolioEntityToPortfolioDomainEntity listCopyPortfolioEntityToPortfolioDomainEntity;
    protected ListCopyStockEntityToStockDE listCopyStockEntityToStockDE;
    protected ListCopyStockSectorEntityToStockSectorDE listCopyStockSectorEntityToStockSectorDE;
    protected ListCopyStockSubSectorEntityToStockSubSectorDE listCopyStockSubSectorEntityToStockSubSectorDE;

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
    public void setListCopyStockEntityToStockDE(
        final ListCopyStockEntityToStockDE listCopyStockEntityToStockDE )
    {
        this.listCopyStockEntityToStockDE = listCopyStockEntityToStockDE;
    }

    /**
     * Dependency injection of the StockSectorRepository
     *
     * @param stockSectorRepository
     */
    @Autowired
    public void setStockSectorRepository( final StockSectorRepository stockSectorRepository )
    {
        final String methodName = "setStockSectorRepository";
        logDebug( methodName, "Dependency Injection of: " + stockSectorRepository );
        this.stockSectorRepository = stockSectorRepository;
    }

    @Autowired
    public void setListCopyStockSectorEntityToStockSectorDE(
        final ListCopyStockSectorEntityToStockSectorDE listCopyStockSectorEntityToStockSectorDE )
    {
        this.listCopyStockSectorEntityToStockSectorDE = listCopyStockSectorEntityToStockSectorDE;
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
     * Dependency injection of the CustomerStockRepository
     * @param customerStockRepository
     */
    @Autowired
    public void setCustomerStockRepository( final CustomerStockRepository customerStockRepository )
    {
        final String methodName = "setCustomerStockRepository";
        logDebug( methodName, "Dependency Injection of: " + customerStockRepository );
        this.customerStockRepository = customerStockRepository;
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
     * @param listCopyCustomerEntityToCustomerDE
     */
    @Autowired
    public void setListCopyCustomerEntityToCustomerDE( final ListCopyCustomerEntityToCustomerDE listCopyCustomerEntityToCustomerDE )
    {
        final String methodName = "setListCopyCustomerEntityToCustomerDo";
        logDebug( methodName, "Dependency Injection of: " + listCopyCustomerEntityToCustomerDE );
        this.listCopyCustomerEntityToCustomerDE = listCopyCustomerEntityToCustomerDE;
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
     * Dependency injection of the ListCopyVPortfolioStockEntityToCustomerStockDomainEntity
     * @param
     */
    @Autowired
    public void setListCopyVPortfolioStockEntityToCustomerStockDE(
        ListCopyVPortfolioStockEntityToCustomerStockDE listCopyVPortfolioStockEntityToCustomerStockDE )
    {
        final String methodName = "setListCopyVPortfolioStockEntityToCustomerStockDomainEntity";
        logDebug( methodName, "Dependency Injection of: " + listCopyVPortfolioStockEntityToCustomerStockDE );
        this.listCopyVPortfolioStockEntityToCustomerStockDE = listCopyVPortfolioStockEntityToCustomerStockDE;
    }

    public StockSubSectorRepository getStockSubSectorRepository()
    {
        return stockSubSectorRepository;
    }

    @Autowired
    public void setStockSubSectorRepository( StockSubSectorRepository stockSubSectorRepository )
    {
        this.stockSubSectorRepository = stockSubSectorRepository;
    }

    public ListCopyStockSubSectorEntityToStockSubSectorDE getListCopyStockSubSectorEntityToStockSubSectorDE()
    {
        return listCopyStockSubSectorEntityToStockSubSectorDE;
    }

    @Autowired
    public void setListCopyStockSubSectorEntityToStockSubSectorDE( ListCopyStockSubSectorEntityToStockSubSectorDE listCopyStockSubSectorEntityToStockSubSectorDE )
    {
        this.listCopyStockSubSectorEntityToStockSubSectorDE = listCopyStockSubSectorEntityToStockSubSectorDE;
    }
}
