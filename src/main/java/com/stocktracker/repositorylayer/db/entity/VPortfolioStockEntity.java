package com.stocktracker.repositorylayer.db.entity;

import com.stocktracker.servicelayer.entity.StockDE;
import org.hibernate.annotations.Immutable;

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
@Immutable
@Table( name = "v_portfolio_stock", schema = "stocktracker", catalog = "" )
public class VPortfolioStockEntity extends BaseDBEntity<VPortfolioStockEntity, StockDE>
{
    private Integer portfolioId;
    private String tickerSymbol;
    private String companyName;
    private Integer numberOfShares;
    private BigDecimal lastPrice;
    private Integer sectorId;
    private Integer subSectorId;
    private Integer costBasis;
    private Integer realizedGain;
    private Integer realizedLoss;
    private BigDecimal stopLossPrice;
    private Integer stopLossShares;
    private Integer profitTakingShares;
    private BigDecimal profitTakingPrice;
    private Integer id;

    @Id
    @Column( name = "portfolio_id", nullable = true )
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
    public Integer getCostBasis()
    {
        return costBasis;
    }

    public void setCostBasis( final Integer costBasis )
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

    @Basic
    @Column( name = "stop_loss_price", nullable = true, precision = 2 )
    public BigDecimal getStopLossPrice()
    {
        return stopLossPrice;
    }

    public void setStopLossPrice( final BigDecimal stopLossPrice )
    {
        this.stopLossPrice = stopLossPrice;
    }

    @Basic
    @Column( name = "stop_loss_shares", nullable = true )
    public Integer getStopLossShares()
    {
        return stopLossShares;
    }

    public void setStopLossShares( final Integer stopLossShares )
    {
        this.stopLossShares = stopLossShares;
    }

    @Basic
    @Column( name = "profit_taking_price", nullable = true, precision = 2 )
    public BigDecimal getProfitTakingPrice()
    {
        return profitTakingPrice;
    }

    public void setProfitTakingPrice( final BigDecimal profitTakingPrice )
    {
        this.profitTakingPrice = profitTakingPrice;
    }

    @Basic
    @Column( name = "profit_taking_shares", nullable = true )
    public Integer getProfitTakingShares()
    {
        return profitTakingShares;
    }

    public void setProfitTakingShares( final Integer profitTakingShares )
    {
        this.profitTakingShares = profitTakingShares;
    }

    @Basic
    @Column( name = "realized_gain", nullable = true )
    public Integer getRealizedGain()
    {
        return realizedGain;
    }

    public void setRealizedGain( Integer realizedGain )
    {
        this.realizedGain = realizedGain;
    }

    @Basic
    @Column( name = "realized_loss", nullable = true )
    public Integer getRealizedLoss()
    {
        return realizedLoss;
    }

    public void setRealizedLoss( Integer realizedLoss )
    {
        this.realizedLoss = realizedLoss;
    }

    @Basic
    @Column( name = "sector_id", nullable = true, length = 5 )
    public Integer getSectorId()
    {
        return sectorId;
    }

    public void setSectorId( Integer sectorId )
    {
        this.sectorId = sectorId;
    }

    @Basic
    @Column( name = "sub_sector_id", nullable = true, length = 5 )
    public Integer getSubSectorId()
    {
        return subSectorId;
    }

    public void setSubSectorId( Integer subSectorId )
    {
        this.subSectorId = subSectorId;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "VPortfolioStockEntity{" );
        sb.append( "portfolioId=" ).append( portfolioId );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( ", numberOfShares=" ).append( numberOfShares );
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", sectorId=" ).append( sectorId );
        sb.append( ", subSectorId=" ).append( subSectorId );
        sb.append( ", costBasis=" ).append( costBasis );
        sb.append( ", realizedGain=" ).append( realizedGain );
        sb.append( ", realizedLoss=" ).append( realizedLoss );
        sb.append( ", stopLossPrice=" ).append( stopLossPrice );
        sb.append( ", stopLossShares=" ).append( stopLossShares );
        sb.append( ", profitTakingShares=" ).append( profitTakingShares );
        sb.append( ", profitTakingPrice=" ).append( profitTakingPrice );
        sb.append( '}' );
        return sb.toString();
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
        return Objects.equals( portfolioId, that.portfolioId ) &&
            Objects.equals( tickerSymbol, that.tickerSymbol ) &&
            Objects.equals( companyName, that.companyName ) &&
            Objects.equals( numberOfShares, that.numberOfShares ) &&
            Objects.equals( lastPrice, that.lastPrice ) &&
            Objects.equals( costBasis, that.costBasis ) &&
            Objects.equals( realizedGain, that.realizedGain ) &&
            Objects.equals( realizedLoss, that.realizedLoss ) &&
            Objects.equals( stopLossPrice, that.stopLossPrice ) &&
            Objects.equals( stopLossShares, that.stopLossShares ) &&
            Objects.equals( profitTakingShares, that.profitTakingShares ) &&
            Objects.equals( profitTakingPrice, that.profitTakingPrice );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( portfolioId, tickerSymbol, companyName, lastPrice, costBasis, numberOfShares, realizedGain, realizedLoss, stopLossPrice, stopLossShares, profitTakingPrice, profitTakingShares );
    }

    @Basic
    @Column( name = "id", nullable = false )
    public Integer getId()
    {
        return id;
    }

    public void setId( final Integer id )
    {
        this.id = id;
    }
}
