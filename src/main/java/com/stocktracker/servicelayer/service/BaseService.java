package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.repository.CustomerRepository;
import com.stocktracker.repositorylayer.repository.PortfolioRepository;
import com.stocktracker.repositorylayer.repository.PortfolioStockRepository;
import com.stocktracker.repositorylayer.repository.StockNoteRepository;
import com.stocktracker.repositorylayer.repository.StockNoteSourceRepository;
import com.stocktracker.repositorylayer.repository.StockNoteStockRepository;
import com.stocktracker.repositorylayer.repository.StockRepository;
import com.stocktracker.repositorylayer.repository.StockSectorRepository;
import com.stocktracker.repositorylayer.repository.StockSubSectorRepository;
import com.stocktracker.repositorylayer.repository.VPortfolioStockRepository;
import com.stocktracker.repositorylayer.repository.VStockNoteCountRepository;
import com.stocktracker.servicelayer.service.listcopy.ListCopyCustomerEntityToCustomerDE;
import com.stocktracker.servicelayer.service.listcopy.ListCopyPortfolioEntityToPortfolioDE;
import com.stocktracker.servicelayer.service.listcopy.ListCopyPortfolioStockEntityToPortfolioStockDE;
import com.stocktracker.servicelayer.service.listcopy.ListCopyStockEntityToStockDE;
import com.stocktracker.servicelayer.service.listcopy.ListCopyStockNoteEntityToStockNoteDTO;
import com.stocktracker.servicelayer.service.listcopy.ListCopyStockNoteSourceEntityToStockNoteSourceDTO;
import com.stocktracker.servicelayer.service.listcopy.ListCopyStockNoteStockDTOToStockNoteStockEntity;
import com.stocktracker.servicelayer.service.listcopy.ListCopyStockNoteStockEntityToStockNoteStockDTO;
import com.stocktracker.servicelayer.service.listcopy.ListCopyStockSectorEntityToStockSectorDE;
import com.stocktracker.servicelayer.service.listcopy.ListCopyStockSubSectorEntityToStockSubSectorDE;
import com.stocktracker.servicelayer.service.listcopy.ListCopyVPortfolioStockEntityToCustomerStockDE;
import com.stocktracker.servicelayer.service.listcopy.ListCopyVStockNoteCountEntityToStockNoteCountDTO;
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
    protected StockSectorRepository stockSectorRepository;
    protected StockSubSectorRepository stockSubSectorRepository;
    protected StockNoteRepository stockNoteRepository;
    protected VStockNoteCountRepository vStockNoteCountRepository;
    protected StockNoteSourceRepository stockNoteSourceRepository;
    protected StockNoteStockRepository stockNoteStockRepository;

    /***********************************************
     *  L I S T  B E A N  C O P I E R S
     **********************************************/
    protected ListCopyVPortfolioStockEntityToCustomerStockDE listCopyVPortfolioStockEntityToCustomerStockDE;
    protected ListCopyCustomerEntityToCustomerDE listCopyCustomerEntityToCustomerDE;
    protected ListCopyPortfolioEntityToPortfolioDE listCopyPortfolioEntityToPortfolioDE;
    protected ListCopyStockEntityToStockDE listCopyStockEntityToStockDE;
    protected ListCopyStockSectorEntityToStockSectorDE listCopyStockSectorEntityToStockSectorDE;
    protected ListCopyStockSubSectorEntityToStockSubSectorDE listCopyStockSubSectorEntityToStockSubSectorDE;
    protected ListCopyPortfolioStockEntityToPortfolioStockDE listCopyPortfolioStockEntityToPortfolioStockDE;
    protected ListCopyStockNoteEntityToStockNoteDTO listCopyStockNoteEntityToStockNoteDTO;
    protected ListCopyStockNoteStockEntityToStockNoteStockDTO listCopyStockNoteStockEntityToStockNoteStockDTO;
    protected ListCopyStockNoteStockDTOToStockNoteStockEntity listCopyStockNoteStockDTOToStockNoteStockEntity;
    protected ListCopyVStockNoteCountEntityToStockNoteCountDTO listCopyVStockNoteCountEntityToStockNoteCountDTO;
    protected ListCopyStockNoteSourceEntityToStockNoteSourceDTO listCopyStockNoteSourceEntityToStockNoteSourceDTO;

    /**
     * Dependency injection of the VStockNoteCountRepository
     *
     * @param
     */
    @Autowired
    public void setVStockNoteTickerSymbolCountRepository( final VStockNoteCountRepository VStockNoteCountRepository )
    {
        final String methodName = "setVStockNoteTickerSymbolCountRepository";
        logDebug( methodName, "Dependency Injection of: " + VStockNoteCountRepository );
        this.vStockNoteCountRepository = VStockNoteCountRepository;
    }

    /**
     * Dependency injection of the StockRepository
     *
     * @param stockNoteStockRepository
     */
    @Autowired
    public void setStockNoteStockRepository( final StockNoteStockRepository stockNoteStockRepository )
    {
        final String methodName = "setStockNoteStockRepository";
        logDebug( methodName, "Dependency Injection of: " + stockNoteStockRepository );
        this.stockNoteStockRepository = stockNoteStockRepository;
    }

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

    /**
     * Dependency injection of the StockNoteRepository
     *
     * @param stockNoteRepository
     */
    @Autowired
    public void setStockNoteRepository( final StockNoteRepository stockNoteRepository )
    {
        final String methodName = "setStockNoteRepository";
        logDebug( methodName, "Dependency Injection of: " + stockNoteRepository );
        this.stockNoteRepository = stockNoteRepository;
    }

    /**
     * Dependency injection
     * @param listCopyStockNoteEntityToStockNoteDTO
     */
    @Autowired
    public void setListCopyStockNoteEntityToStockNoteDTO(
        final ListCopyStockNoteEntityToStockNoteDTO listCopyStockNoteEntityToStockNoteDTO )
    {
        this.listCopyStockNoteEntityToStockNoteDTO = listCopyStockNoteEntityToStockNoteDTO;
    }

    /**
     * Dependency injection
     * @param listCopyStockNoteStockEntityToStockNoteStockDTO
     */
    @Autowired
    public void setListCopyStockNoteStockEntityToStockNoteStockDTO(
        final ListCopyStockNoteStockEntityToStockNoteStockDTO listCopyStockNoteStockEntityToStockNoteStockDTO )
    {
        this.listCopyStockNoteStockEntityToStockNoteStockDTO = listCopyStockNoteStockEntityToStockNoteStockDTO;
    }

    /**
     * Dependency injection
     * @param listCopyStockNoteStockDTOToStockNoteStockEntity
     */
    @Autowired
    public void setListCopyStockNoteStockDTOToStockNoteStockEntity(
        final ListCopyStockNoteStockDTOToStockNoteStockEntity listCopyStockNoteStockDTOToStockNoteStockEntity )
    {
        this.listCopyStockNoteStockDTOToStockNoteStockEntity = listCopyStockNoteStockDTOToStockNoteStockEntity;
    }

    /**
     * Dependency injection
     * @param listCopyStockEntityToStockDE
     */
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
     * @param listCopyPortfolioEntityToPortfolioDE
     */
    @Autowired
    public void setListCopyPortfolioEntityToPortfolioDE(
        final ListCopyPortfolioEntityToPortfolioDE listCopyPortfolioEntityToPortfolioDE )
    {
        final String methodName = "setListCopyPortfolioEntityToPortfolioDo";
        logDebug( methodName, "Dependency Injection of: " + listCopyPortfolioEntityToPortfolioDE );
        this.listCopyPortfolioEntityToPortfolioDE = listCopyPortfolioEntityToPortfolioDE;
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

    @Autowired
    public void setStockSubSectorRepository( StockSubSectorRepository stockSubSectorRepository )
    {
        this.stockSubSectorRepository = stockSubSectorRepository;
    }

    @Autowired
    public void setListCopyStockSubSectorEntityToStockSubSectorDE( ListCopyStockSubSectorEntityToStockSubSectorDE listCopyStockSubSectorEntityToStockSubSectorDE )
    {
        this.listCopyStockSubSectorEntityToStockSubSectorDE = listCopyStockSubSectorEntityToStockSubSectorDE;
    }

    @Autowired
    public void setListCopyPortfolioStockEntityToPortfolioStockDE( ListCopyPortfolioStockEntityToPortfolioStockDE
                                                                   listCopyPortfolioStockEntityToPortfolioStockDE )
    {
        this.listCopyPortfolioStockEntityToPortfolioStockDE = listCopyPortfolioStockEntityToPortfolioStockDE;
    }

    @Autowired
    public void setStockNoteSourceRepository( StockNoteSourceRepository stockNoteSourceRepository )
    {
        this.stockNoteSourceRepository = stockNoteSourceRepository;
    }

    @Autowired
    public void setListCopyVStockNoteCountEntityToStockNoteCountDTO( ListCopyVStockNoteCountEntityToStockNoteCountDTO
                                                                         listCopyVStockNoteCountEntityToStockNoteCountDTO )
    {
        this.listCopyVStockNoteCountEntityToStockNoteCountDTO = listCopyVStockNoteCountEntityToStockNoteCountDTO;
    }

    @Autowired
    public void setListCopyStockNoteSourceEntityToStockNoteSourceDTO( ListCopyStockNoteSourceEntityToStockNoteSourceDTO
                                                                     listCopyVStockNoteCountEntityToStockNoteCountDE )
    {
        this.listCopyStockNoteSourceEntityToStockNoteSourceDTO = listCopyVStockNoteCountEntityToStockNoteCountDE;
    }
}
