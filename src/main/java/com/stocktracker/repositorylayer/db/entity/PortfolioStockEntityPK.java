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
    private String tickerSymbol;

    @Column( name = "portfolio_id", nullable = false )
    @Id
    public int getPortfolioId()
    {
        return portfolioId;
    }

    public void setPortfolioId( final int portfolioId )
    {
        this.portfolioId = portfolioId;
    }

    @Column( name = "ticker_symbol", nullable = false, length = 5 )
    @Id
    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
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
            Objects.equals( tickerSymbol, that.tickerSymbol );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( portfolioId, tickerSymbol );
    }
}
