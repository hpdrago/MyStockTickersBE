package com.stocktracker.weblayer.dto;

import com.stocktracker.servicelayer.entity.CustomerStockDE;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 * Created by mike on 10/30/2016.
 */
public class CustomerStockDTO
{
    private int customerId;
    private String tickerSymbol;
    private String companyName;
    private Integer numberOfShares;
    private Integer sectorId;
    private Integer costBasis;
    private BigDecimal lastPrice;
    private Integer realizedGain;
    private Integer realizedLoss;
    private BigDecimal stopLossPrice;
    private Integer stopLossShares;
    private BigDecimal profitTakingPrice;
    private Integer profitTakingShares;

    /**
     * Creates a new {@code CustomerStockDTO} from a {@code CustomerStockDE} instance
     * @param customerStockDE
     * @return
     */
    public static CustomerStockDTO newInstance( final CustomerStockDE customerStockDE )
    {
        CustomerStockDTO customerStockDTO = new CustomerStockDTO();
        BeanUtils.copyProperties( customerStockDE, customerStockDTO );
        return customerStockDTO;
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
    }

    public Integer getCostBasis()
    {
        return costBasis;
    }

    public void setCostBasis( Integer costBasis )
    {
        this.costBasis = costBasis;
    }

    public BigDecimal getLastPrice()
    {
        return lastPrice;
    }

    public void setLastPrice( BigDecimal lastPrice )
    {
        this.lastPrice = lastPrice;
    }

    public int getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( int customerId )
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

    public Integer getRealizedLoss()
    {
        return realizedLoss;
    }

    public void setRealizedLoss( Integer realizedLoss )
    {
        this.realizedLoss = realizedLoss;
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

    public Integer getRealizedGain()
    {
        return realizedGain;
    }

    public void setRealizedGain( Integer realizedGain )
    {
        this.realizedGain = realizedGain;
    }

    public Integer getSectorId()
    {
        return sectorId;
    }

    public void setSectorId( Integer sectorId )
    {
        this.sectorId = sectorId;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "CustomerStockDTO{" );
        sb.append( "customerId=" ).append( customerId );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( ", numberOfShares=" ).append( numberOfShares );
        sb.append( ", sectorId=" ).append( sectorId );
        sb.append( ", costBasis=" ).append( costBasis );
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", realizedGain=" ).append( realizedGain );
        sb.append( ", realizedLoss=" ).append( realizedLoss );
        sb.append( ", stopLossPrice=" ).append( stopLossPrice );
        sb.append( ", stopLossShares=" ).append( stopLossShares );
        sb.append( ", profitTakingPrice=" ).append( profitTakingPrice );
        sb.append( ", profitTakingShares=" ).append( profitTakingShares );
        sb.append( '}' );
        return sb.toString();
    }
}
