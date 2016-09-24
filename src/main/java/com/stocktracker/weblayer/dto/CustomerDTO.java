package com.stocktracker.weblayer.dto;

import com.stocktracker.servicelayer.entity.CustomerDomainEntity;
import org.springframework.beans.BeanUtils;

/**
 * Created by mike on 5/15/2016.
 */
public class CustomerDTO
{
    private int id;
    private String email;

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

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "CustomerDTO" );
        sb.append( "@" );
        sb.append( hashCode() );
        sb.append( "{" );
        sb.append( "id=" ).append( id );
        sb.append( ", email=" ).append( getEmail() );
        sb.append( '}' );
        return sb.toString();
    }
}
