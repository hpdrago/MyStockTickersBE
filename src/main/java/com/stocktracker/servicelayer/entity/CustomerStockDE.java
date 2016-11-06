package com.stocktracker.servicelayer.entity;

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
}
