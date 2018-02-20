package com.stocktracker.weblayer.dto;

import java.util.Objects;

/**
 * Created by mike on 10/23/2016.
 */
public class PortfolioDTO implements VersionedDTO<Integer>
{
    /**
     * The portfolio id
     */
    private Integer id;
    private Integer customerId;

    /**
     * The portfolio name
     */
    private String name;

    private Integer realizedGL;
    private Integer marketValue;
    private Integer costBasis;
    private Integer unrealizedGL;
    private Integer version;

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

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
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
    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( Integer customerId )
    {
        this.customerId = customerId;
    }

    public Integer getRealizedGL()
    {
        return realizedGL;
    }

    public void setRealizedGL( Integer realizedGL )
    {
        this.realizedGL = realizedGL;
    }

    public Integer getMarketValue()
    {
        return marketValue;
    }

    public void setMarketValue( Integer marketValue )
    {
        this.marketValue = marketValue;
    }

    public Integer getCostBasis()
    {
        return costBasis;
    }

    public void setCostBasis( Integer costBasis )
    {
        this.costBasis = costBasis;
    }

    public Integer getUnrealizedGL()
    {
        return unrealizedGL;
    }

    public void setUnrealizedGL( Integer unrealizedGL )
    {
        this.unrealizedGL = unrealizedGL;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion( final Integer version )
    {
        this.version = version;
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
        sb.append( ", version=" ).append( version );
        sb.append( '}' );
        return sb.toString();
    }

}
