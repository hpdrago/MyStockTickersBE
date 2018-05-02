package com.stocktracker.weblayer.dto;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class defines the data that will be sent to the client when requesting information for a Customer
 * Created by mike on 5/15/2016.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class CustomerDTO implements UuidDTO
{
    private String id;
    private String email;
    private Integer version;
    private List<PortfolioDTO> portfolios;

    /**
     * Creates a new instance from (@code customerEntity)
     * @return
     */
    public static final CustomerDTO newInstance()
    {
        CustomerDTO customerDTO = new CustomerDTO();
        return customerDTO;
    }

    private CustomerDTO()
    {
    }

    public String getId()
    {
        return id;
    }

    public void setId( final String id )
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
    public Integer getVersion()
    {
        return version;
    }

    public void setVersion( final Integer version )
    {
        this.version = version;
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
        sb.append( ", version=" ).append( version );
        sb.append( '}' );
        return sb.toString();
    }
}
