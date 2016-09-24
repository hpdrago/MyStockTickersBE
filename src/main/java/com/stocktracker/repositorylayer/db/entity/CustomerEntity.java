package com.stocktracker.repositorylayer.db.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Created by mike on 9/2/2016.
 */
@Entity
@Table( name = "customer", schema = "stocktracker", catalog = "" )
public class CustomerEntity
{
    private int id;
    private String email;
    private String password;

    public CustomerEntity()
    {
    }

    public CustomerEntity( final String email, final String password )
    {
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue
    @Column( name = "id" )
    public int getId()
    {
        return id;
    }

    public void setId( final int id )
    {
        this.id = id;
    }

    @Basic
    @Column( name = "email" )
    public String getEmail()
    {
        return email;
    }

    public void setEmail( final String email )
    {
        this.email = email;
    }

    @Basic
    @Column( name = "password" )
    public String getPassword()
    {
        return password;
    }

    public void setPassword( final String password )
    {
        this.password = password;
    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        final CustomerEntity that = (CustomerEntity) o;
        return id == that.id &&
            Objects.equals( email, that.email ) &&
            Objects.equals( password, that.password );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( id, email, password );
    }
}
