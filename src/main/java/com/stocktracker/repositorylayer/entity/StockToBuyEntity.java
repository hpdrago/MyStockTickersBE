package com.stocktracker.repositorylayer.entity;

import com.stocktracker.repositorylayer.common.CustomerUuidContainer;
import com.stocktracker.repositorylayer.common.NotesSourceUuidContainer;
import com.stocktracker.servicelayer.service.StockNoteSourceEntityService;
import com.stocktracker.servicelayer.service.stocks.StockPriceWhenCreatedContainer;
import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "stock_to_buy", schema = "stocktracker", catalog = "" )
public class StockToBuyEntity extends UUIDEntity
                              implements CustomerUuidContainer,
                                         NotesSourceUuidContainer,
                                         TickerSymbolContainer,
                                         StockPriceWhenCreatedContainer
{
    private static final int COMMENTS_LEN = 4096;
    private UUID customerUuid;
    private String tickerSymbol;
    private String comments;
    private BigDecimal buySharesUpToPrice;
    private BigDecimal stockPriceWhenCreated;
    private String completed;
    //private UUID notesSourceUuid;
    private StockNoteSourceEntity stockNoteSourceByNotesSourceUuid;
    private Timestamp buyAfterDate;

    @Basic
    @Column( name = "customer_uuid" )
    public UUID getCustomerUuid()
    {
        return customerUuid;
    }

    public void setCustomerUuid( final UUID customerId )
    {
        this.customerUuid = customerId;
    }

    @Basic
    @Column( name = "ticker_symbol" )
    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol == null ? null : StringUtils.truncate( tickerSymbol, StockCompanyEntity.TICKER_SYMBOL_LEN );
    }

    @Basic
    @Column( name = "comments", nullable = true, length = 4096 )
    public String getComments()
    {
        return comments;
    }

    public void setComments( final String comments )
    {
        this.comments = comments == null ? null : StringUtils.truncate( comments, COMMENTS_LEN );
    }

    @Basic
    @Column( name = "buy_shares_up_to_price", nullable = true, precision = 2 )
    public BigDecimal getBuySharesUpToPrice()
    {
        return buySharesUpToPrice;
    }

    public void setBuySharesUpToPrice( final BigDecimal buySharesBelow )
    {
        this.buySharesUpToPrice = buySharesBelow;
    }

    @Basic
    @Column( name = "stock_price_when_created", nullable = true, precision = 2 )
    public BigDecimal getStockPriceWhenCreated()
    {
        return stockPriceWhenCreated;
    }

    public void setStockPriceWhenCreated( final BigDecimal stockPrice )
    {
        this.stockPriceWhenCreated = stockPrice;
    }

    @Basic
    @Column( name = "completed", nullable = false, length = 1 )
    public String getCompleted()
    {
        return completed;
    }

    public void setCompleted( final String completed )
    {
        this.completed = completed;
    }

    @Basic
    @Column( name = "buy_after_date" )
    public Timestamp getBuyAfterDate()
    {
        return buyAfterDate;
    }

    public void setBuyAfterDate( final Timestamp buyAfterDate )
    {
        this.buyAfterDate = buyAfterDate;
    }

    /*
    @Basic
    @Column( name = "notes_source_uuid" )
    public UUID getNotesSourceUuid()
    {
        return notesSourceUuid;
    }

    public void setNotesSourceUuid( final UUID notesSourceUuid )
    {
        this.notesSourceUuid = notesSourceUuid;
    }
    */

    @ManyToOne
    @JoinColumn( name = "notes_source_uuid", referencedColumnName = "uuid" )
    public StockNoteSourceEntity getStockNoteSourceByNotesSourceUuid()
    {
        return stockNoteSourceByNotesSourceUuid;
    }

    public void setStockNoteSourceByNotesSourceUuid( final StockNoteSourceEntity stockNoteSourceByNotesSourceUuid )
    {
        this.stockNoteSourceByNotesSourceUuid = stockNoteSourceByNotesSourceUuid;
    }

    @Transient
    @Override
    public Optional<StockNoteSourceEntity> getNotesSourceEntity()
    {
        return Optional.ofNullable( this.stockNoteSourceByNotesSourceUuid );
    }

    @Transient
    @Override
    public void setNotesSourceEntity( final StockNoteSourceEntity stockNoteSourceEntity )
    {
        this.stockNoteSourceByNotesSourceUuid = stockNoteSourceEntity;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockToBuyEntity{" );
        sb.append( "uuid=" ).append( getUuidString() );
        sb.append( ", customerUuid=" ).append( customerUuid );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", comments='" ).append( comments ).append( '\'' );
        //sb.append( ", notesSourceId=" ).append( notesSourceUuid );
        sb.append( ", buySharesUpToPrice=" ).append( buySharesUpToPrice );
        sb.append( ", stockPriceWhenCreated=" ).append( stockPriceWhenCreated );
        sb.append( ", completed='" ).append( completed ).append( '\'' );
        sb.append( ", buyAfterDate=" ).append( buyAfterDate );
        sb.append( ", stockNoteSourceByNotesSourceId=" ).append( stockNoteSourceByNotesSourceUuid );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
