package com.stocktracker.repositorylayer.entity;

import com.stocktracker.servicelayer.service.stocks.StockPriceWhenCreatedContainer;
import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;
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
import java.util.UUID;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "watch_list_stock", schema = "stocktracker", catalog = "" )
public class WatchListStockEntity extends UUIDEntity
                                  implements TickerSymbolContainer,
                                             StockPriceWhenCreatedContainer
{
    private UUID watchListUuid;
    private String tickerSymbol;
    private String notes;
    private BigDecimal stockPriceWhenCreated;
    private Integer shares;
    private BigDecimal costBasis;
    private WatchListEntity watchListByWatchListUuid;

    @Basic
    @Column( name = "watch_list_uuid", updatable = false, insertable = false)
    public UUID getWatchListUuid()
    {
        return watchListUuid;
    }

    public void setWatchListUuid( final UUID watchListUuid )
    {
        this.watchListUuid = watchListUuid;
    }

    @Basic
    @Column( name = "ticker_symbol", nullable = false, length = 20 )
    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    @Basic
    @Column( name = "notes", nullable = true, length = 30 )
    public String getNotes()
    {
        return notes;
    }

    public void setNotes( final String notes )
    {
        this.notes = notes;
    }

    @ManyToOne
    @JoinColumn( name = "watch_list_uuid", referencedColumnName = "uuid", nullable = false )
    public WatchListEntity getWatchListByWatchListUuid()
    {
        return watchListByWatchListUuid;
    }

    public void setWatchListByWatchListUuid( final WatchListEntity watchListByWatchListUuid )
    {
        this.watchListByWatchListUuid = watchListByWatchListUuid;
    }

    @Basic
    @Column( name = "stock_price_when_created", nullable = false, precision = 2 )
    public BigDecimal getStockPriceWhenCreated()
    {
        return stockPriceWhenCreated;
    }

    public void setStockPriceWhenCreated( final BigDecimal stockPriceWhenCreated )
    {
        this.stockPriceWhenCreated = stockPriceWhenCreated;
    }

    @Basic
    @Column( name = "shares", nullable = true )
    public Integer getShares()
    {
        return shares;
    }

    public void setShares( final Integer shares )
    {
        this.shares = shares;
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

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "WatchListStockEntity{" );
        sb.append( "watchListUuid=" ).append( watchListUuid );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", notes='" ).append( notes ).append( '\'' );
        sb.append( ", stockPriceWhenCreated=" ).append( stockPriceWhenCreated );
        sb.append( ", shares=" ).append( shares );
        sb.append( ", costBasis=" ).append( costBasis );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
