package com.stocktracker.repositorylayer.entity;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table( name = "linked_account_position", schema = "stocktracker", catalog = "" )
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockPositionEntity implements VersionedEntity<Integer>
{
    private Integer id;
    private Integer linkedAccountId;
    private BigDecimal costBasis;
    private String holdingType;
    private BigDecimal lastPrice;
    private BigDecimal quantity;
    private String tickerSymbol;
    private String symbolClass;
    private BigDecimal todayGainLossDollar;
    private BigDecimal todayGainLossPercentage;
    private BigDecimal totalGainLossDollar;
    private BigDecimal totalGainLossPercentage;
    private Timestamp createDate;
    private Timestamp updateDate;
    private Integer version;
    private LinkedAccountEntity linkedAccountByLinkedAccountId;

    @Id
    @Column( name = "id", nullable = false )
    public Integer getId()
    {
        return id;
    }

    public void setId( final Integer id )
    {
        this.id = id;
    }


    @Basic
    @Column( name = "linked_account_id", updatable = false, insertable = false, nullable = true )
    public Integer getLinkedAccountId()
    {
        return linkedAccountId;
    }

    public void setLinkedAccountId( final Integer linkedAccountId )
    {
        this.linkedAccountId = linkedAccountId;
    }

    @Basic
    @Column( name = "cost_basis", nullable = true, precision = 2 )
    public BigDecimal getCostBasis()
    {
        return costBasis;
    }

    public void setCostBasis( final BigDecimal costBasis )
    {
        this.costBasis = costBasis;
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
        this.lastPrice = lastPrice;
    }

    @Basic
    @Column( name = "quantity", nullable = true, precision = 2 )
    public BigDecimal getQuantity()
    {
        return quantity;
    }

    public void setQuantity( final BigDecimal quantity )
    {
        this.quantity = quantity;
    }

    @Basic
    @Column( name = "ticker_symbol", nullable = true, length = 5 )
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
    @Column( name = "today_gain_loss_dollar", nullable = true, precision = 2 )
    public BigDecimal getTodayGainLossDollar()
    {
        return todayGainLossDollar;
    }

    public void setTodayGainLossDollar( final BigDecimal todayGainLossDollar )
    {
        this.todayGainLossDollar = todayGainLossDollar;
    }

    @Basic
    @Column( name = "today_gain_loss_percentage", nullable = true, precision = 2 )
    public BigDecimal getTodayGainLossPercentage()
    {
        return todayGainLossPercentage;
    }

    public void setTodayGainLossPercentage( final BigDecimal todayGainLossPercentage )
    {
        this.todayGainLossPercentage = todayGainLossPercentage;
    }

    @Basic
    @Column( name = "total_gain_loss_dollar", nullable = true, precision = 2 )
    public BigDecimal getTotalGainLossDollar()
    {
        return totalGainLossDollar;
    }

    public void setTotalGainLossDollar( final BigDecimal totalGainLossDollar )
    {
        this.totalGainLossDollar = totalGainLossDollar;
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

    @Basic
    @Column( name = "create_date", nullable = true )
    public Timestamp getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate( final Timestamp createDate )
    {
        this.createDate = createDate;
    }

    @Basic
    @Column( name = "update_date", nullable = true )
    public Timestamp getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate( final Timestamp updateDate )
    {
        this.updateDate = updateDate;
    }

    @Override
    @Basic
    @Column( name = "version", nullable = false )
    public Integer getVersion()
    {
        return null;
    }

    public void setVersion( final Integer version )
    {
        this.version = version;
    }

    @ManyToOne
    @JoinColumn( name = "linked_account_id", referencedColumnName = "id" )
    public LinkedAccountEntity getLinkedAccountByLinkedAccountId()
    {
        return linkedAccountByLinkedAccountId;
    }

    public void setLinkedAccountByLinkedAccountId( final LinkedAccountEntity linkedAccountByLinkedAccountId )
    {
        this.linkedAccountByLinkedAccountId = linkedAccountByLinkedAccountId;
    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !(o instanceof StockPositionEntity) )
        {
            return false;
        }

        final StockPositionEntity that = (StockPositionEntity) o;

        if ( !id.equals( that.id ) )
        {
            return false;
        }
        return tickerSymbol.equals( that.tickerSymbol );
    }

    @Override
    public int hashCode()
    {
        int result = id.hashCode();
        result = 31 * result + tickerSymbol.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "LinkedAccountPositionEntity{" );
        sb.append( "id=" ).append( id );
        sb.append( ", linkedAccountId=" ).append( linkedAccountId );
        sb.append( ", costBasis=" ).append( costBasis );
        sb.append( ", holdingType='" ).append( holdingType ).append( '\'' );
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", quantity=" ).append( quantity );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", symbolClass='" ).append( symbolClass ).append( '\'' );
        sb.append( ", todayGainLossDollar=" ).append( todayGainLossDollar );
        sb.append( ", todayGainLossPercentage=" ).append( todayGainLossPercentage );
        sb.append( ", totalGainLossDollar=" ).append( totalGainLossDollar );
        sb.append( ", totalGainLossPercentage=" ).append( totalGainLossPercentage );
        sb.append( ", createDate=" ).append( createDate );
        sb.append( ", updateDate=" ).append( updateDate );
        sb.append( ", version=" ).append( version );
        sb.append( '}' );
        return sb.toString();
    }
}