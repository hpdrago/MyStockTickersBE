package com.stocktracker.servicelayer.entity;

/**
 * This class defines a stock that is part of a portfolio.
 * A stock can be part of more than one port folio.
 *
 * Created by mike on 10/30/2016.
 */
public class PortfolioStockDomainEntity
{
    private int portfolioId;
    private StockDomainEntity stock;

    public int getPortfolioId()
    {
        return portfolioId;
    }

    public void setPortfolioId( int portfolioId )
    {
        this.portfolioId = portfolioId;
    }

    public StockDomainEntity getStock()
    {
        return stock;
    }

    public void setStock( StockDomainEntity stock )
    {
        this.stock = stock;
    }
}
