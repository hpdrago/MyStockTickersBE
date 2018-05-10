package com.stocktracker.repositorylayer.entity;

import com.stocktracker.repositorylayer.common.VersionedEntity;
import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Objects;

@MappedSuperclass
public abstract class TickerSymbolEntity extends BaseEntity<String>
    implements VersionedEntity<String>,
               TickerSymbolContainer
{
    public static final int TICKER_SYMBOL_LEN = 6;
    private String tickerSymbol;

    /**
     * Get the primary key
     * @return
     */
    @Transient
    @Override
    public String getId()
    {
        return this.tickerSymbol;
    }

    /**
     * Set the primary key.
     * @param id
     */
    public void setId( final String id )
    {
        this.setTickerSymbol( id );
    }

    @Id
    @Column( name = "ticker_symbol", nullable = false, length = 25 )
    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        Objects.requireNonNull( "ticker symbol cannot be null" );
        this.tickerSymbol = tickerSymbol;
    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        final TickerSymbolEntity that = (TickerSymbolEntity) o;
        return Objects.equals( tickerSymbol, that.tickerSymbol );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( tickerSymbol );
    }
}
