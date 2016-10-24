package com.stocktracker.weblayer.dto;

import com.stocktracker.servicelayer.entity.PortfolioDomainEntity;
import org.springframework.beans.BeanUtils;

/**
 * Created by mike on 10/23/2016.
 */
public class PortfolioDTO
{
    private int id;
    private String name;
    private int customerId;

    /**
     * Creates a new instance from (@code portfolioEntity)
     * @param portfolioDomainEntity
     * @return
     */
    public static final PortfolioDTO newInstance( final PortfolioDomainEntity portfolioDomainEntity )
    {
        PortfolioDTO portfolioDTO = new PortfolioDTO();
        BeanUtils.copyProperties( portfolioDomainEntity, portfolioDTO );
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

    public int getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( int customerId )
    {
        this.customerId = customerId;
    }
}
