package com.stocktracker.servicelayer.entity;

import com.stocktracker.repositorylayer.db.entity.CustomerEntity;
import org.springframework.beans.BeanUtils;

/**
 * Created by mike on 9/10/2016.
 */
public class CustomerDomainEntity
{
    private int id;
    private String email;

    /**
     * Create a new empty instance
     * @return
     */
    public static CustomerDomainEntity newInstance()
    {
        return new CustomerDomainEntity();
    }

    /**
     * Convert the {@code customerEntity} into a {@code CustomerDomainEntity}
     * @param customerEntity
     * @return
     */
    public static CustomerDomainEntity newInstance( final CustomerEntity customerEntity )
    {
        CustomerDomainEntity customerDomainEntity = new CustomerDomainEntity();
        BeanUtils.copyProperties( customerEntity, customerDomainEntity );
        return customerDomainEntity;
    }

    private CustomerDomainEntity()
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
        final StringBuilder sb = new StringBuilder( "CustomerDomainEntity" );
        sb.append( "@" );
        sb.append( hashCode() );
        sb.append( "{" );
        sb.append( "id=" ).append( id );
        sb.append( ", email=" ).append( getEmail() );
        sb.append( '}' );
        return sb.toString();
    }
}
