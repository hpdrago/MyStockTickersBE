package com.stocktracker.weblayer.dto;

import java.util.List;

/**
 * Created by mike on 10/30/2016.
 */
public class PortfolioStockDTO
{
    private PortfolioDTO portfolio;
    private List<StockDTO> stocks;

    public PortfolioDTO getPortfolio()
    {
        return portfolio;
    }

    public void setPortfolio( PortfolioDTO portfolio )
    {
        this.portfolio = portfolio;
    }

    public List<StockDTO> getStocks()
    {
        return stocks;
    }

    public void setStocks( List<StockDTO> stocks )
    {
        this.stocks = stocks;
    }
}
