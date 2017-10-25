package com.stocktracker.weblayer.dto;

import com.stocktracker.common.JSONDateConverter;
import com.stocktracker.servicelayer.service.StockService;
import com.stocktracker.servicelayer.service.YahooStockService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;

/**
 * Created by mike on 10/30/2016.
 */
public class PortfolioLastStockDTO implements StockService.StockCompanyNameContainer,
                                              StockService.LastPriceContainer,
                                              YahooStockService.YahooStockContainer
{
    private Integer id;
    private Integer customerId;
    private Integer portfolioId;
    private String tickerSymbol;
    private String companyName;
    private Integer numberOfShares;
    private BigDecimal averageUnitCost;
    private BigDecimal lastPrice;
    private String lastPriceChange;
    private BigDecimal realizedGains;
    private BigDecimal realizedLosses;
    private BigDecimal stopLossPrice;
    private Integer stopLossShares;
    private BigDecimal profitTakingPrice;
    private Integer profitTakingShares;
    private Integer sectorId;

    /***** Calculated fields *****/
    private Integer marketValue;
    private Integer costBasis;

    /**
     * Creates a new {@code PortfolioLastStockDTO}
     * @return
     */
    public static PortfolioLastStockDTO newInstance()
    {
        PortfolioLastStockDTO portfolioStockDTO = new PortfolioLastStockDTO();
        return portfolioStockDTO;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( Integer customerId )
    {
        this.customerId = customerId;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName( String companyName )
    {
        this.companyName = companyName;
    }

    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
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

    public BigDecimal getLastPrice()
    {
        return lastPrice;
    }

    public void setLastPrice( BigDecimal stockPrice )
    {
        this.lastPrice = stockPrice;
        this.calculateMarketValue();
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

    public Integer getPortfolioId()
    {
        return portfolioId;
    }

    public void setPortfolioId( Integer portfolioId )
    {
        this.portfolioId = portfolioId;
    }

    public String getLastPriceChange()
    {
        return lastPriceChange;
    }

    public void setLastPriceChange( final String lastPriceChange )
    {
        this.lastPriceChange = lastPriceChange;
    }

    @Override
    public Timestamp getLastPriceChangeTimestamp()
    {
        Timestamp returnValue = null;
        if ( this.lastPriceChange != null )
        {
            try
            {
                returnValue = JSONDateConverter.toTimestamp( this.lastPriceChange );
            }
            catch ( ParseException e )
            {
                e.printStackTrace();
            }
        }
        return returnValue;
    }

    @Override
    public void setLastPriceChangeTimestamp( final Timestamp lastPriceChange )
    {
        if ( lastPriceChange != null )
        {
            try
            {
                JSONDateConverter.toString( lastPriceChange );
            }
            catch ( ParseException e )
            {
                e.printStackTrace();
            }
        }
        else
        {
            this.lastPriceChange = null;
        }
    }

    private void calculateMarketValue()
    {
        if ( this.numberOfShares != null && this.lastPrice != null )
        {
            this.marketValue = (int) (this.numberOfShares.floatValue() *
                                      this.lastPrice.floatValue());
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
        final StringBuilder sb = new StringBuilder( "PortfolioLastStockDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId='" ).append( customerId ).append( '\'' );
        sb.append( ", portfolioId='" ).append( portfolioId ).append( '\'' );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( ", numberOfShares=" ).append( numberOfShares );
        sb.append( ", averageUnitCost=" ).append( averageUnitCost );
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", lastPriceChange=" ).append( lastPriceChange );
        sb.append( ", costBasis=" ).append( costBasis );
        sb.append( ", marketValue=" ).append( marketValue );
        sb.append( ", realizedGains=" ).append( realizedGains );
        sb.append( ", realizedLosses=" ).append( realizedLosses );
        sb.append( ", stopLossPrice=" ).append( stopLossPrice );
        sb.append( ", stopLossShares=" ).append( stopLossShares );
        sb.append( ", profitTakingPrice=" ).append( profitTakingPrice );
        sb.append( ", profitTakingShares=" ).append( profitTakingShares );
        sb.append( ", sectorId=" ).append( sectorId );
        sb.append( '}' );
        return sb.toString();
    }
}
