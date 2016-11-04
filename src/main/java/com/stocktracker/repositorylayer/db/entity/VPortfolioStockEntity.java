package com.stocktracker.repositorylayer.db.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by mike on 10/30/2016.
 */
@Entity
@Table( name = "v_portfolio_stock", schema = "stocktracker", catalog = "" )
public class VPortfolioStockEntity
{
    private String rowId;
    private String tickerSymbol;
    private String companyName;
    private BigDecimal lastPrice;
    private BigDecimal costBasis;
    private Integer numberOfShares;
    private Integer portfolioId;

    @Id
    public String getRowId()
    {
        return rowId;
    }

    public void setRowId( String rowId )
    {
        this.rowId = rowId;
    }

    @Basic
    @Column( name = "portfolio_id", nullable = false )
    public Integer getPortfolioId()
    {
        return portfolioId;
    }

    public void setPortfolioId( final Integer portfolioId )
    {
        this.portfolioId = portfolioId;
    }

    @Basic
    @Column( name = "ticker_symbol", nullable = false, length = 5 )
    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    @Basic
    @Column( name = "company_name", nullable = true, length = 70 )
    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName( final String companyName )
    {
        this.companyName = companyName;
    }

    @Basic
    @Column( name = "last_price", nullable = true, precision = 2 )
    public BigDecimal getLastPrice()
    {
        return lastPrice;
    }

    public void setLastPrice( final BigDecimal lastPrice )
    {
        this.lastPrice = lastPrice;
    }

    @Basic
    @Column( name = "cost_basis", nullable = true, precision = 2 )
    public BigDecimal getCostBasis()
    {
        return costBasis;
    }

    public void setCostBasis( final BigDecimal costBasis )
    {
        this.costBasis = costBasis;
    }

    @Basic
    @Column( name = "number_of_shares", nullable = true )
    public Integer getNumberOfShares()
    {
        return numberOfShares;
    }

    public void setNumberOfShares( final Integer numberOfShares )
    {
        this.numberOfShares = numberOfShares;
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
        final VPortfolioStockEntity that = (VPortfolioStockEntity) o;
        return Objects.equals( tickerSymbol, that.tickerSymbol ) &&
            Objects.equals( companyName, that.companyName ) &&
            Objects.equals( lastPrice, that.lastPrice ) &&
            Objects.equals( costBasis, that.costBasis ) &&
            Objects.equals( numberOfShares, that.numberOfShares );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( tickerSymbol, companyName, lastPrice, costBasis, numberOfShares );
    }
}
