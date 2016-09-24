package com.stocktracker.repositorylayer.db.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by mike on 9/4/2016.
 */
public class PortfolioStockEntityPK implements Serializable
{
    private int portfolioId;
    private String stockTicker;

    @Column( name = "portfolio_id" )
    @Id
    public int getPortfolioId()
    {
        return portfolioId;
    }

    public void setPortfolioId( final int portfolioId )
    {
        this.portfolioId = portfolioId;
    }

    @Column( name = "stock_ticker" )
    @Id
    public String getStockTicker()
    {
        return stockTicker;
    }

    public void setStockTicker( final String stockTicker )
    {
        this.stockTicker = stockTicker;
    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        final PortfolioStockEntityPK that = (PortfolioStockEntityPK) o;
        return portfolioId == that.portfolioId &&
            Objects.equals( stockTicker, that.stockTicker );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( portfolioId, stockTicker );
    }
}
