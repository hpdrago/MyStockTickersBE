package com.stocktracker.weblayer.dto;

import java.util.Objects;

/**
 * Created by mike on 10/23/2016.
 */
public class PortfolioDTO
{
    /**
     * The portfolio id
     */
    private int id;

    private int customerId;

    /**
     * The portfolio name
     */
    private String name;

    private int realizedGL;
    private int marketValue;
    private int costBasis;
    private int unrealizedGL;

    /**
     * Creates a new instance from (@code portfolioEntity)
     * @return
     */
    public static final PortfolioDTO newInstance()
    {
        PortfolioDTO portfolioDTO = new PortfolioDTO();
        return portfolioDTO;
    }

    private PortfolioDTO()
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

    /**
     * The customer id
     */ /**
     * The customer id
     */
    public int getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( int customerId )
    {
        this.customerId = customerId;
    }

    public int getRealizedGL()
    {
        return realizedGL;
    }

    public void setRealizedGL( int realizedGL )
    {
        this.realizedGL = realizedGL;
    }

    public int getMarketValue()
    {
        return marketValue;
    }

    public void setMarketValue( int marketValue )
    {
        this.marketValue = marketValue;
    }

    public int getCostBasis()
    {
        return costBasis;
    }

    public void setCostBasis( int costBasis )
    {
        this.costBasis = costBasis;
    }

    public int getUnrealizedGL()
    {
        return unrealizedGL;
    }

    public void setUnrealizedGL( int unrealizedGL )
    {
        this.unrealizedGL = unrealizedGL;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "PortfolioDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", name='" ).append( name ).append( '\'' );
        sb.append( ", realizedGL=" ).append( realizedGL );
        sb.append( ", marketValue=" ).append( marketValue );
        sb.append( ", costBasis=" ).append( costBasis );
        sb.append( ", unrealizedGL=" ).append( unrealizedGL );
        sb.append( '}' );
        return sb.toString();
    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !(o instanceof PortfolioDTO) )
        {
            return false;
        }
        final PortfolioDTO that = (PortfolioDTO) o;
        return id == that.id;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( id );
    }
}
