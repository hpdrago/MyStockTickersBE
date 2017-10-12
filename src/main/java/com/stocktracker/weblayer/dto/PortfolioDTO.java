package com.stocktracker.weblayer.dto;

/**
 * Created by mike on 10/23/2016.
 */
public class PortfolioDTO
{
    /**
     * The portfolio id
     */
    private int id;

    /**
     * The customer id
     */
    private int customerId;

    /**
     * The portfolio name
     */
    private String name;

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
     */
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
        final StringBuilder sb = new StringBuilder( "PortfolioDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId='" ).append( customerId ).append( '\'' );
        sb.append( ", name='" ).append( name ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
