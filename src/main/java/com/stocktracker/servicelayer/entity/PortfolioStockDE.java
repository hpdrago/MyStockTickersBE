package com.stocktracker.servicelayer.entity;

import com.stocktracker.repositorylayer.entity.PortfolioStockEntity;
import com.stocktracker.weblayer.dto.PortfolioStockDTO;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 * This class encapsulates the information for a single stock for a customer.
 *
 * Created by mike on 10/30/2016.
 */
public class PortfolioStockDE
{
    private Integer id;
    private Integer portfolioId;
    private Integer customerId;
    private String tickerSymbol;
    private String companyName;
    private Integer numberOfShares;
    private Integer costBasis;
    private BigDecimal lastPrice;
    private Integer sectorId;
    private Integer realizedLoss;
    private BigDecimal stopLossPrice;
    private Integer stopLossShares;
    private BigDecimal profitTakingPrice;
    private Integer profitTakingShares;
    private Integer realizedGain;

    public static PortfolioStockDE newInstance()
    {
        return new PortfolioStockDE();
    }

    /**
     * Create a new instance of {@code PortfolioStockDE} from a {@code CustomerStockEntity} instance
     * @param portfolioStockEntity
     * @return
     */
    public static PortfolioStockDE newInstance( final PortfolioStockEntity portfolioStockEntity )
    {
        PortfolioStockDE portfolioStockDE = new PortfolioStockDE();
        BeanUtils.copyProperties( portfolioStockEntity, portfolioStockDE );
        return portfolioStockDE;
    }

    /**
     * Creates a new instance of {@code PortfolioStockDE} from a {@code PortfolioStockDTO}
     * @param portfolioStockDTO
     * @return
     */
    public static PortfolioStockDE newInstance( final PortfolioStockDTO portfolioStockDTO )
    {
        PortfolioStockDE portfolioStockDE = new PortfolioStockDE();
        BeanUtils.copyProperties( portfolioStockDTO, portfolioStockDE );
        return portfolioStockDE;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public Integer getPortfolioId()
    {
        return portfolioId;
    }

    public void setPortfolioId( Integer portfolioId )
    {
        this.portfolioId = portfolioId;
    }

    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( Integer customerId )
    {
        this.customerId = customerId;
    }

    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName( String companyName )
    {
        this.companyName = companyName;
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

    public Integer getSectorId()
    {
        return sectorId;
    }

    public void setSectorId( Integer sectorId )
    {
        this.sectorId = sectorId;
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

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "PortfolioStockDE{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", portfolioId=" ).append( portfolioId );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( ", numberOfShares=" ).append( numberOfShares );
        sb.append( ", costBasis=" ).append( costBasis );
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", sectorId=" ).append( sectorId );
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
