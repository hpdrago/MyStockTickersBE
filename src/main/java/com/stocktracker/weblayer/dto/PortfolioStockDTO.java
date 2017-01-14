package com.stocktracker.weblayer.dto;

import com.stocktracker.servicelayer.entity.PortfolioStockDE;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by mike on 10/30/2016.
 */
public class PortfolioStockDTO
{
    private Integer id;
    private Integer customerId;
    private Integer portfolioId;
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
     * Creates a new {@code PortfolioStockDTO} from a {@code PortfolioStockDE} instance
     * @param portfolioStockDE
     * @return
     */
    public static PortfolioStockDTO newInstance( final PortfolioStockDE portfolioStockDE )
    {
        Objects.requireNonNull( portfolioStockDE );
        PortfolioStockDTO portfolioStockDTO = new PortfolioStockDTO();
        BeanUtils.copyProperties( portfolioStockDE, portfolioStockDTO );
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

    public Integer getPortfolioId()
    {
        return portfolioId;
    }

    public void setPortfolioId( Integer portfolioId )
    {
        this.portfolioId = portfolioId;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "PortfolioStockDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId='" ).append( customerId ).append( '\'' );
        sb.append( ", portfolioId='" ).append( portfolioId ).append( '\'' );
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
