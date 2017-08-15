package com.stocktracker.repositorylayer.db.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by mike on 5/7/2017.
 */
@Entity
@Table( name = "customer_stock", schema = "stocktracker", catalog = "" )
@IdClass( CustomerStockEntityPK.class )
public class CustomerStockEntity
{
    private Integer customerId;
    private String tickerSymbol;
    private Integer numberOfShares;
    private Integer costBasis;
    private Integer realizedGain;
    private Integer realizedLoss;
    private BigDecimal stopLossPrice;
    private Integer stopLossShares;
    private BigDecimal profitTakingPrice;
    private Integer profitTakingShares;
    private Integer sectorId;
    private Integer subSectorId;

    @Id
    @Column( name = "customer_id", nullable = false )
    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( final Integer customerId )
    {
        this.customerId = customerId;
    }

    @Id
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
        return subSectorId;
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
        final CustomerStockEntity that = (CustomerStockEntity) o;
        return Objects.equals( customerId, that.customerId ) &&
               Objects.equals( tickerSymbol, that.tickerSymbol ) &&
               Objects.equals( numberOfShares, that.numberOfShares ) &&
               Objects.equals( costBasis, that.costBasis ) &&
               Objects.equals( realizedGain, that.realizedGain ) &&
               Objects.equals( realizedLoss, that.realizedLoss ) &&
               Objects.equals( stopLossPrice, that.stopLossPrice ) &&
               Objects.equals( stopLossShares, that.stopLossShares ) &&
               Objects.equals( profitTakingPrice, that.profitTakingPrice ) &&
               Objects.equals( profitTakingShares, that.profitTakingShares ) &&
               Objects.equals( sectorId, that.sectorId ) &&
               Objects.equals( subSectorId, that.subSectorId );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( customerId, tickerSymbol, numberOfShares, costBasis, realizedGain, realizedLoss, stopLossPrice, stopLossShares, profitTakingPrice, profitTakingShares, sectorId, subSectorId );
    }
}
