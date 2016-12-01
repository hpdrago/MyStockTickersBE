package com.stocktracker.repositorylayer.db.entity;

import com.stocktracker.servicelayer.entity.PortfolioStockDE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Created by mike on 9/4/2016.
 */
@Entity
@Table( name = "portfolio_stock", schema = "stocktracker", catalog = "" )
@IdClass( PortfolioStockEntityPK.class )
public class PortfolioStockEntity extends BaseDBEntity<PortfolioStockEntity, PortfolioStockDE>
{
    private int portfolioId;
    private String tickerSymbol;

    public static PortfolioStockEntity newInstance( final int portfolioId, final String tickerSymbol )
    {
        PortfolioStockEntity portfolioStockEntity = new PortfolioStockEntity();
        portfolioStockEntity.setPortfolioId( portfolioId );
        portfolioStockEntity.setTickerSymbol( tickerSymbol );
        return portfolioStockEntity;
    }

    @Id
    @Column( name = "portfolio_id", nullable = false )
    public int getPortfolioId()
    {
        return portfolioId;
    }

    public void setPortfolioId( final int portfolioId )
    {
        this.portfolioId = portfolioId;
    }

    @Id
    @Column( name = "ticker_symbol", nullable = false, length = 5 )
    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
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
        final PortfolioStockEntity that = (PortfolioStockEntity) o;
        return portfolioId == that.portfolioId &&
            Objects.equals( tickerSymbol, that.tickerSymbol );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( portfolioId, tickerSymbol );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "PortfolioStockEntity{" );
        sb.append( "portfolioId=" ).append( portfolioId );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
