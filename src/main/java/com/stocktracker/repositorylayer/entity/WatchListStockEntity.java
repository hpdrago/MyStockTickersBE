package com.stocktracker.repositorylayer.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.UUID;

@Entity
@Table( name = "watch_list_stock", schema = "stocktracker", catalog = "" )
public class WatchListStockEntity extends UUIDEntity
{
    private UUID watchListUuid;
    private String tickerSymbol;
    private String notes;
    private WatchListEntity watchListByWatchListUuid;

    @Basic
    @Column( name = "watch_list_uuid" )
    public UUID getWatchListUuid()
    {
        return watchListUuid;
    }

    public void setWatchListUuid( final UUID watchListUuid )
    {
        this.watchListUuid = watchListUuid;
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
    @Column( name = "notes" )
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

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "WatchListStockEntity{" );
        sb.append( "watchListUuid=" ).append( watchListUuid );
        sb.append( ", watchListEntity="  ).append( watchListByWatchListUuid ).append( '\'' );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", notes='" ).append( notes ).append( '\'' );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
