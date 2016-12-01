package com.stocktracker.servicelayer.entity;

import com.stocktracker.repositorylayer.db.entity.CustomerStockEntity;
import com.stocktracker.weblayer.dto.CustomerStockDTO;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 * This class encapsulates the information for a single stock for a customer.
 *
 * Created by mike on 10/30/2016.
 */
public class CustomerStockDE
{
    private int customerId;
    private String tickerSymbol;
    private String companyName;
    private Integer numberOfShares;
    private BigDecimal costBasis;
    private BigDecimal lastPrice;

    private Integer sectorId;
    private Integer realizedLoss;
    private BigDecimal stopLossPrice;
    private Integer stopLossShares;
    private BigDecimal profitTakingPrice;
    private Integer profitTakingShares;
    private Integer realizedGain;

    /**
     * Create a new instance of {@code CustomerStockDE} from a {@code CustomerStockEntity} instance
     * @param customerStockEntity
     * @return
     */
    public static CustomerStockDE newInstance( final CustomerStockEntity customerStockEntity )
    {
        CustomerStockDE customerStockDE = new CustomerStockDE();
        BeanUtils.copyProperties( customerStockEntity, customerStockDE );
        return customerStockDE;
    }

    /**
     * Creates a new instance of {@code CustomerStockDE} from a {@code CustomerStockDTO}
     * @param customerStockDTO
     * @return
     */
    public static CustomerStockDE newInstance( final CustomerStockDTO customerStockDTO )
    {
        CustomerStockDE customerStockDE = new CustomerStockDE();
        BeanUtils.copyProperties( customerStockDTO, customerStockDE );
        return customerStockDE;
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

    public BigDecimal getCostBasis()
    {
        return costBasis;
    }

    public void setCostBasis( BigDecimal costBasis )
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

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "CustomerStockDE{" );
        sb.append( "customerId=" ).append( customerId );
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
