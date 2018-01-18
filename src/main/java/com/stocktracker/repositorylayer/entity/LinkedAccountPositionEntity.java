package com.stocktracker.repositorylayer.entity;

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
public class LinkedAccountPositionEntity
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
    private LinkedAccountEntity linkedAccountByLinkedAccountId;

    @Id
    @Column( name = "id" )
    public Integer getId()
    {
        return id;
    }

    public void setId( final Integer id )
    {
        this.id = id;
    }


    @Basic
    @Column( name = "linked_account_id", updatable = false, insertable = false )
    public Integer getLinkedAccountId()
    {
        return linkedAccountId;
    }

    public void setLinkedAccountId( final Integer linkedAccountId )
    {
        this.linkedAccountId = linkedAccountId;
    }

    @Basic
    @Column( name = "cost_basis" )
    public BigDecimal getCostBasis()
    {
        return costBasis;
    }

    public void setCostBasis( final BigDecimal costBasis )
    {
        this.costBasis = costBasis;
    }

    @Basic
    @Column( name = "holding_type" )
    public String getHoldingType()
    {
        return holdingType;
    }

    public void setHoldingType( final String holdingType )
    {
        this.holdingType = holdingType;
    }

    @Basic
    @Column( name = "last_price" )
    public BigDecimal getLastPrice()
    {
        return lastPrice;
    }

    public void setLastPrice( final BigDecimal lastPrice )
    {
        this.lastPrice = lastPrice;
    }

    @Basic
    @Column( name = "quantity" )
    public BigDecimal getQuantity()
    {
        return quantity;
    }

    public void setQuantity( final BigDecimal quantity )
    {
        this.quantity = quantity;
    }

    @Basic
    @Column( name = "ticker_symbol" )
    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    @Basic
    @Column( name = "symbol_class" )
    public String getSymbolClass()
    {
        return symbolClass;
    }

    public void setSymbolClass( final String symbolClass )
    {
        this.symbolClass = symbolClass;
    }

    @Basic
    @Column( name = "today_gain_loss_dollar" )
    public BigDecimal getTodayGainLossDollar()
    {
        return todayGainLossDollar;
    }

    public void setTodayGainLossDollar( final BigDecimal todayGainLossDollar )
    {
        this.todayGainLossDollar = todayGainLossDollar;
    }

    @Basic
    @Column( name = "today_gain_loss_percentage" )
    public BigDecimal getTodayGainLossPercentage()
    {
        return todayGainLossPercentage;
    }

    public void setTodayGainLossPercentage( final BigDecimal todayGainLossPercentage )
    {
        this.todayGainLossPercentage = todayGainLossPercentage;
    }

    @Basic
    @Column( name = "total_gain_loss_dollar" )
    public BigDecimal getTotalGainLossDollar()
    {
        return totalGainLossDollar;
    }

    public void setTotalGainLossDollar( final BigDecimal totalGainLossDollar )
    {
        this.totalGainLossDollar = totalGainLossDollar;
    }

    @Basic
    @Column( name = "total_gain_loss_percentage" )
    public BigDecimal getTotalGainLossPercentage()
    {
        return totalGainLossPercentage;
    }

    public void setTotalGainLossPercentage( final BigDecimal totalGainLossPercentage )
    {
        this.totalGainLossPercentage = totalGainLossPercentage;
    }

    @Basic
    @Column( name = "create_date" )
    public Timestamp getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate( final Timestamp createDate )
    {
        this.createDate = createDate;
    }

    @Basic
    @Column( name = "update_date" )
    public Timestamp getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate( final Timestamp updateDate )
    {
        this.updateDate = updateDate;
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
        if ( !(o instanceof LinkedAccountPositionEntity) )
        {
            return false;
        }

        final LinkedAccountPositionEntity that = (LinkedAccountPositionEntity) o;

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
        sb.append( '}' );
        return sb.toString();
    }
}
