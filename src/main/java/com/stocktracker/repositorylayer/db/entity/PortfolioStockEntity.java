package com.stocktracker.repositorylayer.db.entity;

import com.stocktracker.servicelayer.entity.PortfolioStockDE;
import org.springframework.beans.BeanUtils;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * CUSTOMER_STOCK Entity
 * Created by mike on 10/30/2016.
 */
@Entity
@Table( name = "portfolio_stock", schema = "stocktracker", catalog = "" )
public class PortfolioStockEntity extends BaseDBEntity<PortfolioStockEntity, PortfolioStockDE>
{
    private Integer id;
    private Integer customerId;
    private Integer portfolioId;
    private String tickerSymbol;
    private Integer numberOfShares;
    private Integer sectorId;
    private Integer subSectorId;
    private Integer costBasis;
    private Integer realizedLoss;
    private BigDecimal stopLossPrice;
    private Integer stopLossShares;
    private BigDecimal profitTakingPrice;
    private Integer profitTakingShares;
    private Integer realizedGain;

    /**
     * Default constructor
     */
    public PortfolioStockEntity()
    {
    }

    /**
     * Creates a new {@code CustomerStockEntity} instance from {@code PortfolioStockDE} instance
     * @param portfolioStockDE
     * @return
     */
    public static PortfolioStockEntity newInstance( final PortfolioStockDE portfolioStockDE )
    {
        PortfolioStockEntity portfolioStockEntity = new PortfolioStockEntity();
        BeanUtils.copyProperties( portfolioStockDE, portfolioStockEntity );
        return portfolioStockEntity;
    }

    @Id
    @GeneratedValue
    @Column( name = "id", nullable = false )
    public Integer getId()
    {
        return id;
    }

    public void setId( final Integer id )
    {
        this.id = id;
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
    @Column( name = "customer_id", nullable = false )
    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( final Integer customerId )
    {
        this.customerId = customerId;
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
    @Column( name = "cost_basis", nullable = true )
    public Integer getCostBasis()
    {
        return costBasis;
    }

    public void setCostBasis( final Integer costBasis )
    {
        this.costBasis = costBasis;
    }

    @Basic
    @Column( name = "realized_loss", nullable = true )
    public Integer getRealizedLoss()
    {
        return realizedLoss;
    }

    public void setRealizedLoss( final Integer realizedLoss )
    {
        this.realizedLoss = realizedLoss;
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

    public void setRealizedGain( final Integer realizedGain )
    {
        this.realizedGain = realizedGain;
    }

    @Basic
    @Column( name = "sector_id", nullable = true )
    public Integer getSectorId()
    {
        return sectorId;
    }

    public void setSectorId( final Integer sectorId )
    {
        this.sectorId = sectorId;
    }

    @Basic
    @Column( name = "sub_sector_id", nullable = true )
    public Integer getSubSectorId()
    {
        return sectorId;
    }

    public void setSubSectorId( final Integer subSectorId )
    {
        this.subSectorId = subSectorId;
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
        return Objects.equals( id, that.id );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( id );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "PortfolioStockEntity{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", portfolioId=" ).append( portfolioId );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", numberOfShares=" ).append( numberOfShares );
        sb.append( ", sectorId=" ).append( sectorId );
        sb.append( ", subSectorId=" ).append( subSectorId );
        sb.append( ", costBasis=" ).append( costBasis );
        sb.append( ", realizedLoss=" ).append( realizedLoss );
        sb.append( ", stopLossPrice=" ).append( stopLossPrice );
        sb.append( ", stopLossShares=" ).append( stopLossShares );
        sb.append( ", profitTakingPrice=" ).append( profitTakingPrice );
        sb.append( ", profitTakingShares=" ).append( profitTakingShares );
        sb.append( ", realizedGain=" ).append( realizedGain );
        sb.append( '}' );
        return sb.toString();
    }
}
