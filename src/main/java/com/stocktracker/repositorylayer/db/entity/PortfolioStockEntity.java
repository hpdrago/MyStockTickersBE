package com.stocktracker.repositorylayer.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Created by mike on 9/4/2016.
 */
@Entity
@Table( name = "portfolio_stock", schema = "stocktracker", catalog = "" )
@IdClass( PortfolioStockEntityPK.class )
public class PortfolioStockEntity
{
    private int portfolioId;
    private String stockTicker;

    @Id
    @Column( name = "portfolio_id" )
    public int getPortfolioId()
    {
        return portfolioId;
    }

    public void setPortfolioId( final int portfolioId )
    {
        this.portfolioId = portfolioId;
    }

    @Id
    @Column( name = "stock_ticker" )
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
        final PortfolioStockEntity that = (PortfolioStockEntity) o;
        return portfolioId == that.portfolioId &&
            Objects.equals( stockTicker, that.stockTicker );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( portfolioId, stockTicker );
    }
}
