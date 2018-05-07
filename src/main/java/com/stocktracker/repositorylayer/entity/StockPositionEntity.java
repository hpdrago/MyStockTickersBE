package com.stocktracker.repositorylayer.entity;

import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;
import com.stocktracker.servicelayer.tradeit.types.TradeItPosition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "stock_position", schema = "stocktracker", catalog = "" )
public class StockPositionEntity extends UUIDEntity
                                 implements TickerSymbolContainer
{
    private UUID linkedAccountUuid;
    private BigDecimal costBasis;
    private String holdingType;
    private BigDecimal lastPrice;
    private BigDecimal quantity;
    private String tickerSymbol;
    private String symbolClass;
    private BigDecimal todayGainLossAbsolute;
    private BigDecimal todayGainLossPercentage;
    private BigDecimal totalGainLossAbsolute;
    private BigDecimal totalGainLossPercentage;
    private LinkedAccountEntity linkedAccountByLinkedAccountUuid;

    public StockPositionEntity()
    {
    }

    /**
     * Copy constructor.
     * @param stockPositionEntity
     */
    public StockPositionEntity( final StockPositionEntity stockPositionEntity )
    {
        this.setUuid( stockPositionEntity.getUuid() );
        this.costBasis = stockPositionEntity.costBasis;
        this.holdingType = stockPositionEntity.holdingType;
        this.lastPrice = stockPositionEntity.lastPrice;
        this.quantity = stockPositionEntity.quantity;
        this.tickerSymbol = stockPositionEntity.tickerSymbol;
        this.symbolClass = stockPositionEntity.symbolClass;
        this.todayGainLossAbsolute = stockPositionEntity.todayGainLossAbsolute;
        this.todayGainLossPercentage = stockPositionEntity.todayGainLossPercentage;
        this.totalGainLossAbsolute = stockPositionEntity.totalGainLossAbsolute;
        this.totalGainLossPercentage = stockPositionEntity.totalGainLossPercentage;
        this.setCreateDate( stockPositionEntity.getCreateDate() );
        this.setUpdateDate( stockPositionEntity.getUpdateDate() );
        this.setVersion( stockPositionEntity.getVersion() );
        this.linkedAccountByLinkedAccountUuid = stockPositionEntity.linkedAccountByLinkedAccountUuid;
    }

    /**
     * Creates a new entity instance from a TradeItPosition.
     * @param tradeItPosition
     */
    public StockPositionEntity( final TradeItPosition tradeItPosition )
    {
        this.setValues( tradeItPosition );
    }

    /**
     * Create a new instance from a TradeItPosition instance.
     * @param tradeItPosition
     * @return
     */
    public static StockPositionEntity newInstance( final TradeItPosition tradeItPosition )
    {
        return new StockPositionEntity( tradeItPosition );
    }

    /**
     * Copy creator.
     * @param stockPositionEntity
     * @return
     */
    public static StockPositionEntity newInstance( final StockPositionEntity stockPositionEntity )
    {
        return new StockPositionEntity( stockPositionEntity );
    }

    /**
     * Set the TradeItPosition values.
     * @param tradeItPosition
     */
    public void setValues( final TradeItPosition tradeItPosition )
    {
        this.costBasis = this.truncate( tradeItPosition.getCostbasis() );
        this.holdingType = tradeItPosition.getHoldingType();
        this.lastPrice = this.truncate( tradeItPosition.getLastPrice() );
        this.quantity = this.truncate( tradeItPosition.getQuantity() );
        this.tickerSymbol = tradeItPosition.getSymbol();
        this.symbolClass = tradeItPosition.getSymbolClass();
        this.todayGainLossAbsolute = this.truncate( tradeItPosition.getTodayGainLossAbsolute() );
        this.todayGainLossPercentage = this.truncate( tradeItPosition.getTodayGainLossPercentage() );
        this.totalGainLossAbsolute = this.truncate( tradeItPosition.getTotalGainLossAbsolute() );
        this.totalGainLossPercentage = this.truncate( tradeItPosition.getTotalGainLossPercentage() );
    }

    @Basic
    @Column( name = "linked_account_uuid", updatable = false, insertable = false, nullable = false )
    public UUID getLinkedAccountUuid()
    {
        return linkedAccountUuid;
    }

    public void setLinkedAccountUuid( final UUID linkedAccountUuid )
    {
        this.linkedAccountUuid = linkedAccountUuid;
    }

    @Basic
    @Column( name = "cost_basis", nullable = true, precision = 2 )
    public BigDecimal getCostBasis()
    {
        return costBasis;
    }

    public void setCostBasis( final BigDecimal costBasis )
    {
        this.costBasis = this.truncate( costBasis );
    }

    @Basic
    @Column( name = "holding_type", nullable = true, length = 5 )
    public String getHoldingType()
    {
        return holdingType;
    }

    public void setHoldingType( final String holdingType )
    {
        this.holdingType = holdingType;
    }

    @Basic
    @Column( name = "last_price", nullable = true, precision = 2 )
    public BigDecimal getLastPrice()
    {
        return lastPrice;
    }

    public void setLastPrice( final BigDecimal lastPrice )
    {
        this.lastPrice = this.truncate( lastPrice );
    }

    @Basic
    @Column( name = "quantity", nullable = true, precision = 2 )
    public BigDecimal getQuantity()
    {
        return quantity;
    }

    public void setQuantity( final BigDecimal quantity )
    {
        this.quantity = this.truncate( quantity );
    }

    @Basic
    @Column( name = "ticker_symbol", nullable = true, length = 25 )
    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    @Basic
    @Column( name = "symbol_class", nullable = true, length = 20 )
    public String getSymbolClass()
    {
        return symbolClass;
    }

    public void setSymbolClass( final String symbolClass )
    {
        this.symbolClass = symbolClass;
    }

    @Basic
    @Column( name = "today_gain_loss_absolute", nullable = true, precision = 2 )
    public BigDecimal getTodayGainLossAbsolute()
    {
        return todayGainLossAbsolute;
    }

    public void setTodayGainLossAbsolute( final BigDecimal todayGainLossAbsolute )
    {
        this.todayGainLossAbsolute = this.truncate( todayGainLossAbsolute );
    }

    @Basic
    @Column( name = "today_gain_loss_percentage", nullable = true, precision = 2 )
    public BigDecimal getTodayGainLossPercentage()
    {
        return todayGainLossPercentage;
    }

    public void setTodayGainLossPercentage( final BigDecimal todayGainLossPercentage )
    {
        this.todayGainLossPercentage = this.truncate( todayGainLossPercentage );
    }

    @Basic
    @Column( name = "total_gain_loss_absolute", nullable = true, precision = 2 )
    public BigDecimal getTotalGainLossAbsolute()
    {
        return totalGainLossAbsolute;
    }

    public void setTotalGainLossAbsolute( final BigDecimal totalGainLossAbsolute )
    {
        this.totalGainLossAbsolute = this.truncate( totalGainLossAbsolute );
    }

    /**
     * Truncates to two decimal places.  Returns null if value is null.
     */
    private BigDecimal truncate( final BigDecimal value )
    {
        return value == null ? null : value.setScale( 2, RoundingMode.DOWN );
    }

    /**
     * Truncates to two decimal places.  Returns null if value is null.
     */
    private BigDecimal truncate( final double value )
    {
        return this.truncate( new BigDecimal( value ) );
    }

    @Basic
    @Column( name = "total_gain_loss_percentage", nullable = true, precision = 2 )
    public BigDecimal getTotalGainLossPercentage()
    {
        return totalGainLossPercentage;
    }

    public void setTotalGainLossPercentage( final BigDecimal totalGainLossPercentage )
    {
        this.totalGainLossPercentage = totalGainLossPercentage;
    }

    @ManyToOne
    @JoinColumn( name = "linked_account_uuid", referencedColumnName = "uuid" )
    public LinkedAccountEntity getLinkedAccountByLinkedAccountUuid()
    {
        return linkedAccountByLinkedAccountUuid;
    }

    public void setLinkedAccountByLinkedAccountUuid( final LinkedAccountEntity linkedAccount )
    {
        this.linkedAccountByLinkedAccountUuid = linkedAccount;
        this.linkedAccountUuid = linkedAccount.getId();
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "LinkedAccountPositionEntity{" );
        sb.append( "uuid=" ).append( getUuidString() );
        sb.append( ", linkedAccountUuid=" ).append( linkedAccountUuid );
        sb.append( ", costBasis=" ).append( costBasis );
        sb.append( ", holdingType='" ).append( holdingType ).append( '\'' );
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", quantity=" ).append( quantity );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", symbolClass='" ).append( symbolClass ).append( '\'' );
        sb.append( ", todayGainLossAbsolute=" ).append( todayGainLossAbsolute );
        sb.append( ", todayGainLossPercentage=" ).append( todayGainLossPercentage );
        sb.append( ", totalGainLossAbsolute=" ).append( totalGainLossAbsolute );
        sb.append( ", totalGainLossPercentage=" ).append( totalGainLossPercentage );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
