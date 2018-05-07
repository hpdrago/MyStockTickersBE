package com.stocktracker.weblayer.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stocktracker.common.JSONMoneySerializer;
import com.stocktracker.common.JSONTimestampDateTimeSerializer;
import com.stocktracker.servicelayer.service.stocks.StockPriceContainer;
import com.stocktracker.servicelayer.stockinformationprovider.StockPriceCacheState;
import com.stocktracker.servicelayer.tradeit.types.TradeItPosition;
import com.stocktracker.weblayer.dto.common.CustomerIdContainer;
import com.stocktracker.weblayer.dto.common.UuidDTO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * This DTO contains a combination of information.  The positions are obtained from TradeIt and stored in the linked account
 * position table.  The DTO also contains other stock information obtained from Yahoo or IExtrading
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockPositionDTO implements StockPriceContainer,
                                         UuidDTO,
                                         CustomerIdContainer
{
    private String id;
    private String tradeItAccountId;
    private String linkedAccountId;
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
    private BigDecimal todayGainLossAbsolute;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal todayGainLossPercentage;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal totalGainLossAbsolute;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal totalGainLossPercentage;
    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    private Timestamp expirationTime;
    private String customerId;
    private StockPriceCacheState stockPriceCacheState;
    private Integer version;

    /**
     * Copies the TradeIt position information.
     * @param tradeItPosition
     */
    public void setResults( final TradeItPosition tradeItPosition )
    {
        this.costBasis = new BigDecimal( tradeItPosition.getCostbasis() );
        this.holdingType = tradeItPosition.getHoldingType();
        this.lastPrice = new BigDecimal( tradeItPosition.getLastPrice() );
        this.quantity = new BigDecimal( tradeItPosition.getQuantity() );
        this.tickerSymbol = tradeItPosition.getSymbol();
        this.symbolClass = tradeItPosition.getSymbolClass();
        this.todayGainLossAbsolute = new BigDecimal( tradeItPosition.getTodayGainLossAbsolute() );
        this.todayGainLossPercentage = new BigDecimal( tradeItPosition.getTodayGainLossPercentage() );
        this.totalGainLossAbsolute = new BigDecimal( tradeItPosition.getTodayGainLossAbsolute() );
        this.totalGainLossPercentage = new BigDecimal( tradeItPosition.getTodayGainLossPercentage() );
    }

    @Override
    public String getId()
    {
        return null;
    }

    public void setId( final String id )
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
    public BigDecimal getLastPrice()
    {
        return this.lastPrice;
    }

    public void setLastPrice( final BigDecimal lastPrice )
    {
        this.lastPrice = lastPrice;
    }

    @Override
    public void setStockPriceCacheState( final StockPriceCacheState stockPriceCacheState )
    {
        this.stockPriceCacheState = stockPriceCacheState;
    }

    @Override
    public Timestamp getExpirationTime()
    {
        return this.expirationTime;
    }

    @Override
    public void setExpirationTime( final Timestamp expirationTime )
    {
        this.expirationTime = expirationTime;
    }

    @Override
    public StockPriceCacheState getStockPriceCacheState()
    {
        return this.stockPriceCacheState;
    }

    public void setCustomerId( final String customerId )
    {
        this.customerId = customerId;
    }

    public void setCustomerUuid( final UUID customerUuid )
    {
        this.customerId = customerUuid.toString();
    }

    public String getCustomerId()
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

    public BigDecimal getTodayGainLossAbsolute()
    {
        return todayGainLossAbsolute;
    }

    public void setTodayGainLossAbsolute( final BigDecimal todayGainLossAbsolute )
    {
        this.todayGainLossAbsolute = todayGainLossAbsolute;
    }

    public BigDecimal getTodayGainLossPercentage()
    {
        return todayGainLossPercentage;
    }

    public void setTodayGainLossPercentage( final BigDecimal todayGainLossPercentage )
    {
        this.todayGainLossPercentage = todayGainLossPercentage;
    }

    public BigDecimal getTotalGainLossAbsolute()
    {
        return totalGainLossAbsolute;
    }

    public void setTotalGainLossAbsolute( final BigDecimal totalGainLossAbsolute )
    {
        this.totalGainLossAbsolute = totalGainLossAbsolute;
    }

    public BigDecimal getTotalGainLossPercentage()
    {
        return totalGainLossPercentage;
    }

    public void setTotalGainLossPercentage( final BigDecimal totalGainLossPercentage )
    {
        this.totalGainLossPercentage = totalGainLossPercentage;
    }

    @Override
    public Integer getVersion()
    {
        return version;
    }

    public void setVersion( final Integer version )
    {
        this.version = version;
    }

    public String getTradeItAccountId()
    {
        return tradeItAccountId;
    }

    public void setTradeItAccountId( String tradeItAccountId )
    {
        this.tradeItAccountId = tradeItAccountId;
    }

    public void setTradeItAccountId( final UUID tradeItAccountUuid )
    {
        this.tradeItAccountId = tradeItAccountUuid.toString();
    }

    public void setLinkedAccountId( final UUID linkedAccountUuid )
    {
        this.tradeItAccountId = linkedAccountUuid.toString();
    }

    public String getLinkedAccountId()
    {
        return linkedAccountId;
    }

    public void setLinkedAccountId( String linkedAccountId )
    {
        this.linkedAccountId = linkedAccountId;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockPositionDTO{" );
        sb.append( "tradeItAccountId=" ).append( tradeItAccountId );
        sb.append( ", linkedAccountId=" ).append( linkedAccountId );
        sb.append( ", id=" ).append( id );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", symbolClass='" ).append( symbolClass ).append( '\'' );
        sb.append( ", costBasis=" ).append( costBasis );
        sb.append( ", holdingType='" ).append( holdingType ).append( '\'' );
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", quantity=" ).append( quantity );
        sb.append( ", todayGainLossAbsolute=" ).append( todayGainLossAbsolute );
        sb.append( ", todayGainLossPercentage=" ).append( todayGainLossPercentage );
        sb.append( ", totalGainLossAbsolute=" ).append( totalGainLossAbsolute );
        sb.append( ", totalGainLossPercentage=" ).append( totalGainLossPercentage );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", stockPriceCacheState=" ).append( stockPriceCacheState );
        sb.append( ", version=" ).append( version );
        sb.append( '}' );
        return sb.toString();
    }
}
