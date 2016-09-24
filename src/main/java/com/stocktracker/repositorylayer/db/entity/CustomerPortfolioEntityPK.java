package com.stocktracker.repositorylayer.db.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by mike on 9/4/2016.
 */
public class CustomerPortfolioEntityPK implements Serializable
{
    private int customerId;
    private int portfolioId;

    @Column( name = "customer_id" )
    @Id
    public int getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( final int customerId )
    {
        this.customerId = customerId;
    }

    @Column( name = "portfolio_id" )
    @Id
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
        final CustomerPortfolioEntityPK that = (CustomerPortfolioEntityPK) o;
        return customerId == that.customerId &&
            portfolioId == that.portfolioId;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( customerId, portfolioId );
    }
}
