package com.stocktracker.servicelayer.entity;

import com.stocktracker.repositorylayer.entity.CustomerEntity;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;

/**
 * Created by mike on 9/10/2016.
 */
public class CustomerDE
{
    private int id;
    private String email;

    /**
     * Create a new empty instance
     * @return
     */
    public static CustomerDE newInstance()
    {
        return new CustomerDE();
    }

    /**
     * Convert the {@code customerEntity} into a {@code CustomerDE}
     * @param customerEntity
     * @return
     */
    public static CustomerDE newInstance( final CustomerEntity customerEntity )
    {
        Objects.requireNonNull( customerEntity );
        CustomerDE customerDE = new CustomerDE();
        BeanUtils.copyProperties( customerEntity, customerDE );
        return customerDE;
    }

    public void setPortfolios( final List<PortfolioDE> customerDEPortfolios )
    {
    }

    private CustomerDE()
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
        final StringBuilder sb = new StringBuilder( "CustomerDE" );
        sb.append( "{" );
        sb.append( "id=" ).append( id );
        sb.append( ", email=" ).append( getEmail() );
        sb.append( '}' );
        return sb.toString();
    }
}
