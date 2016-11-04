package com.stocktracker.weblayer.dto;

import com.stocktracker.servicelayer.entity.CustomerDomainEntity;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * This class defines the data that will be sent to the client when requesting information for a Customer
 * Created by mike on 5/15/2016.
 */
public class CustomerDTO
{
    private int id;
    private String email;
    private List<PortfolioDTO> portfolios;

    /**
     * Creates a new instance from (@code customerEntity)
     * @param customerDomainEntity
     * @return
     */
    public static final CustomerDTO newInstance( final CustomerDomainEntity customerDomainEntity )
    {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties( customerDomainEntity, customerDTO );
        return customerDTO;
    }

    private CustomerDTO()
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

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public List<PortfolioDTO> getPortfolios()
    {
        return portfolios;
    }

    public void setPortfolios( List<PortfolioDTO> portfolios )
    {
        this.portfolios = portfolios;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "CustomerDTO" );
        sb.append( "@" );
        sb.append( hashCode() );
        sb.append( "{" );
        sb.append( "id=" ).append( id );
        sb.append( ", email=" ).append( getEmail() );
        sb.append( ", portfolios=" ).append( portfolios );
        sb.append( '}' );
        return sb.toString();
    }
}
