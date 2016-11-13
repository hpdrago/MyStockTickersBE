package com.stocktracker.servicelayer.entity;

/**
 * This class defines a stock that is part of a portfolio.
 * A stock can be part of more than one port folio.
 *
 * Created by mike on 10/30/2016.
 */
public class PortfolioStockDE
{
    private int portfolioId;
    private StockDE stock;

    public int getPortfolioId()
    {
        return portfolioId;
    }

    public void setPortfolioId( int portfolioId )
    {
        this.portfolioId = portfolioId;
    }

    public StockDE getStock()
    {
        return stock;
    }

    public void setStock( StockDE stock )
    {
        this.stock = stock;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "PortfolioStockDE{" );
        sb.append( "portfolioId=" ).append( portfolioId );
        sb.append( ", stock=" ).append( stock );
        sb.append( '}' );
        return sb.toString();
    }
}
