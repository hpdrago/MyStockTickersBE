package com.stocktracker.weblayer.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stocktracker.common.JSONMoneySerializer;
import com.stocktracker.weblayer.dto.common.CustomerIdContainer;
import com.stocktracker.weblayer.dto.common.UuidDTO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by mike on 10/30/2016.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class PortfolioStockDTO extends StockQuoteDTO
                               implements UuidDTO,
                                          CustomerIdContainer
{
    private String customerId;
    private String portfolioId;
    private Integer numberOfShares;
    private BigDecimal averageUnitCost;
    private BigDecimal realizedGains;
    private BigDecimal realizedLosses;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal stopLossPrice;
    private Integer stopLossShares;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal profitTakingPrice;
    private Integer profitTakingShares;
    private Integer sectorId;

    /***** Calculated fields *****/
    private Integer marketValue;
    private Integer costBasis;

    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( String customerId )
    {
        this.customerId = customerId;
    }

    public Integer getNumberOfShares()
    {
        return numberOfShares;
    }

    public void setNumberOfShares( Integer numberOfShares )
    {
        this.numberOfShares = numberOfShares;
        this.calculateCostBasis();
        this.calculateMarketValue();
    }

    public BigDecimal getAverageUnitCost()
    {
        return averageUnitCost;
    }

    public void setAverageUnitCost( BigDecimal averageUnitCost )
    {
        this.averageUnitCost = averageUnitCost;
        this.calculateCostBasis();
    }

    public BigDecimal getRealizedLosses()
    {
        return realizedLosses == null ? new BigDecimal( 0 ) : realizedLosses;
    }

    public void setRealizedLosses( BigDecimal realizedLosses )
    {
        this.realizedLosses = realizedLosses;
    }

    public BigDecimal getStopLossPrice()
    {
        return stopLossPrice;
    }

    public void setStopLossPrice( BigDecimal stopLossPrice )
    {
        this.stopLossPrice = stopLossPrice;
    }

    public Integer getStopLossShares()
    {
        return stopLossShares;
    }

    public void setStopLossShares( Integer stopLossShares )
    {
        this.stopLossShares = stopLossShares;
    }

    public BigDecimal getProfitTakingPrice()
    {
        return profitTakingPrice;
    }

    public void setProfitTakingPrice( BigDecimal profitTakingPrice )
    {
        this.profitTakingPrice = profitTakingPrice;
    }

    public Integer getProfitTakingShares()
    {
        return profitTakingShares;
    }

    public void setProfitTakingShares( Integer profitTakingShares )
    {
        this.profitTakingShares = profitTakingShares;
    }

    public BigDecimal getRealizedGains()
    {
        return realizedGains == null ? new BigDecimal(0 ) : realizedGains;
    }

    public void setRealizedGains( BigDecimal realizedGains )
    {
        this.realizedGains = realizedGains;
    }

    public Integer getSectorId()
    {
        return sectorId;
    }

    public void setSectorId( Integer sectorId )
    {
        this.sectorId = sectorId;
    }

    public String getPortfolioId()
    {
        return portfolioId;
    }

    public void setPortfolioId( String portfolioId )
    {
        this.portfolioId = portfolioId;
    }

    private void calculateMarketValue()
    {
        if ( this.numberOfShares != null && this.getLastPrice() != null )
        {
            this.marketValue = (int) (this.numberOfShares.floatValue() *
                                      this.getLastPrice().floatValue());
        }
        else
        {
            this.marketValue = 0;
        }
    }

    private void calculateCostBasis()
    {
        if ( this.numberOfShares != null && this.averageUnitCost != null )
        {
            this.costBasis = (int) (this.numberOfShares.floatValue() *
                                    this.averageUnitCost.floatValue());
        }
        else
        {
            this.costBasis = 0;
        }
    }

    public Integer getMarketValue()
    {
        return marketValue;
    }

    public void setMarketValue( Integer marketValue )
    {
        this.marketValue = marketValue;
    }

    public Integer getCostBasis()
    {
        return costBasis;
    }

    public void setCostBasis( Integer costBasis )
    {
        this.costBasis = costBasis;
    }


    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "PortfolioStockDTO{" );
        sb.append( "id=" ).append( super.getId() );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", portfolioId=" ).append( portfolioId );
        sb.append( ", tickerSymbol='" ).append( super.getTickerSymbol() ).append( '\'' );
        sb.append( ", numberOfShares=" ).append( numberOfShares );
        sb.append( ", averageUnitCost=" ).append( averageUnitCost );
        sb.append( ", realizedGains=" ).append( realizedGains );
        sb.append( ", realizedLosses=" ).append( realizedLosses );
        sb.append( ", stopLossPrice=" ).append( stopLossPrice );
        sb.append( ", stopLossShares=" ).append( stopLossShares );
        sb.append( ", profitTakingPrice=" ).append( profitTakingPrice );
        sb.append( ", profitTakingShares=" ).append( profitTakingShares );
        sb.append( ", sectorId=" ).append( sectorId );
        sb.append( ", marketValue=" ).append( marketValue );
        sb.append( ", costBasis=" ).append( costBasis );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
