package com.stocktracker.weblayer.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stocktracker.common.JSONMoneySerializer;
import com.stocktracker.common.JSONTimestampDateTimeSerializer;
import com.stocktracker.repositorylayer.entity.VersionedEntity;
import com.stocktracker.servicelayer.service.StockQuoteService;
import com.stocktracker.servicelayer.stockinformationprovider.StockQuoteState;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class LinkedAccountPositionDTO implements StockQuoteService.StockQuoteContainer,
                                                 VersionedEntity<Integer>
{
    private Integer id;
    private String tickerSymbol;
    private String symbolClass;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal costBasis;
    private String holdingType;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal lastPrice;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal quantity;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal todayGainLossDollar;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal todayGainLossPercentage;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal totalGainLossDollar;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal totalGainLossPercentage;
    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    private Timestamp lastPriceChange;
    private int customerId;
    private String companyName;
    private String exchange;
    private StockQuoteState stockQuoteState;
    private BigDecimal avgAnalystPriceTarget;
    private int version;

    @Override
    public Integer getId()
    {
        return null;
    }

    public void setId( final Integer id )
    {
        this.id = id;
    }

    @Override
    public String getTickerSymbol()
    {
        return this.tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    @Override
    public void setLastPrice( final BigDecimal stockPrice )
    {
        this.lastPrice = stockPrice;
    }

    @Override
    public BigDecimal getLastPrice()
    {
        return this.lastPrice;
    }

    @Override
    public void setLastPriceChange( final Timestamp lastPriceChange )
    {
        this.lastPriceChange = lastPriceChange;
    }

    @Override
    public Timestamp getLastPriceChange()
    {
        return lastPriceChange;
    }

    @Override
    public void setCompanyName( final String companyName )
    {
        this.companyName = companyName;
    }

    @Override
    public String getCompanyName()
    {
        return this.companyName;
    }

    @Override
    public void setStockExchange( final String exchange )
    {
        this.exchange = exchange;
    }

    @Override
    public String getStockExchange()
    {
        return this.exchange;
    }

    @Override
    public void setStockQuoteState( final StockQuoteState stockQuoteState )
    {
        this.stockQuoteState = stockQuoteState;
    }

    @Override
    public StockQuoteState getStockQuoteState()
    {
        return this.stockQuoteState;
    }

    @Override
    public BigDecimal getAvgAnalystPriceTarget()
    {
        return null;
    }

    @Override
    public void setAvgAnalystPriceTarget( final BigDecimal avgAnalystPriceTarget )
    {
        this.avgAnalystPriceTarget = avgAnalystPriceTarget;
    }

    public void setCustomerId( final int customerId )
    {
        this.customerId = customerId;
    }

    @Override
    public Integer getCustomerId()
    {
        return this.customerId;
    }

    public String getSymbolClass()
    {
        return symbolClass;
    }

    public void setSymbolClass( final String symbolClass )
    {
        this.symbolClass = symbolClass;
    }

    public BigDecimal getCostBasis()
    {
        return costBasis;
    }

    public void setCostBasis( final BigDecimal costBasis )
    {
        this.costBasis = costBasis;
    }

    public String getHoldingType()
    {
        return holdingType;
    }

    public void setHoldingType( final String holdingType )
    {
        this.holdingType = holdingType;
    }

    public BigDecimal getQuantity()
    {
        return quantity;
    }

    public void setQuantity( final BigDecimal quantity )
    {
        this.quantity = quantity;
    }

    public BigDecimal getTodayGainLossDollar()
    {
        return todayGainLossDollar;
    }

    public void setTodayGainLossDollar( final BigDecimal todayGainLossDollar )
    {
        this.todayGainLossDollar = todayGainLossDollar;
    }

    public BigDecimal getTodayGainLossPercentage()
    {
        return todayGainLossPercentage;
    }

    public void setTodayGainLossPercentage( final BigDecimal todayGainLossPercentage )
    {
        this.todayGainLossPercentage = todayGainLossPercentage;
    }

    public BigDecimal getTotalGainLossDollar()
    {
        return totalGainLossDollar;
    }

    public void setTotalGainLossDollar( final BigDecimal totalGainLossDollar )
    {
        this.totalGainLossDollar = totalGainLossDollar;
    }

    public BigDecimal getTotalGainLossPercentage()
    {
        return totalGainLossPercentage;
    }

    public void setTotalGainLossPercentage( final BigDecimal totalGainLossPercentage )
    {
        this.totalGainLossPercentage = totalGainLossPercentage;
    }

    public String getExchange()
    {
        return exchange;
    }

    public void setExchange( final String exchange )
    {
        this.exchange = exchange;
    }

    @Override
    public Integer getVersion()
    {
        return version;
    }

    public void setVersion( final int version )
    {
        this.version = version;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "LinkedAccountPositionDTO{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", id='" ).append( id ).append( '\'' );
        sb.append( ", symbolClass='" ).append( symbolClass ).append( '\'' );
        sb.append( ", costBasis=" ).append( costBasis );
        sb.append( ", holdingType='" ).append( holdingType ).append( '\'' );
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", quantity=" ).append( quantity );
        sb.append( ", todayGainLossDollar=" ).append( todayGainLossDollar );
        sb.append( ", todayGainLossPercentage=" ).append( todayGainLossPercentage );
        sb.append( ", totalGainLossDollar=" ).append( totalGainLossDollar );
        sb.append( ", totalGainLossPercentage=" ).append( totalGainLossPercentage );
        sb.append( ", lastPriceChange=" ).append( lastPriceChange );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( ", exchange='" ).append( exchange ).append( '\'' );
        sb.append( ", stockQuoteState=" ).append( stockQuoteState );
        sb.append( ", avgAnalystPriceTarget=" ).append( avgAnalystPriceTarget );
        sb.append( '}' );
        return sb.toString();
    }
}
