package com.stocktracker.repositorylayer.db.entity;

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
@Table( name = "customer_portfolio", schema = "stocktracker", catalog = "" )
@IdClass( CustomerPortfolioEntityPK.class )
public class CustomerPortfolioEntity
{
    private int customerId;
    private int portfolioId;

    @Id
    @Column( name = "customer_id" )
    public int getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( final int customerId )
    {
        this.customerId = customerId;
    }

    @Id
    @Column( name = "portfolio_id" )
    public int getPortfolioId()
    {
        return portfolioId;
    }

    public void setPortfolioId( final int portfolioId )
    {
        this.portfolioId = portfolioId;
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
        final CustomerPortfolioEntity that = (CustomerPortfolioEntity) o;
        return customerId == that.customerId &&
            portfolioId == that.portfolioId;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( customerId, portfolioId );
    }
}
