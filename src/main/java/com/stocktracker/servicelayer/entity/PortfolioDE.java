package com.stocktracker.servicelayer.entity;

import com.stocktracker.repositorylayer.db.entity.PortfolioEntity;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * This class represents a Portfolio for a customer.  A portfolio is a set of stocks identified by the customer.
 *
 * Created by mike on 10/23/2016.
 */
public class PortfolioDE
{
    private int id;
    private int customerId;
    private String name;
    private List<PortfolioDE> portfolioList;

    /**
     * Create a new empty instance
     * @return
     */
    public static PortfolioDE newInstance()
    {
        return new PortfolioDE();
    }

    /**
     * Convert the {@code portfolioEntity} into a {@code PortfolioDE}
     * @param portfolioEntity
     * @return
     */
    public static PortfolioDE newInstance( final PortfolioEntity portfolioEntity )
    {
        PortfolioDE portfolioDE = new PortfolioDE();
        BeanUtils.copyProperties( portfolioEntity, portfolioDE );
        return portfolioDE;
    }

    private PortfolioDE()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public int getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( int customerId )
    {
        this.customerId = customerId;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "PortfolioDE{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", name='" ).append( name ).append( '\'' );
        sb.append( ", portfolioList='" ).append( portfolioList ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
