package com.stocktracker.repositorylayer.entity;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by mike on 9/2/2016.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "customer", schema = "stocktracker", catalog = "" )
public class CustomerEntity implements VersionedEntity<Integer>
{
    private Integer id;
    private String email;
    private String password;
    private Timestamp createDate;
    private Timestamp updateDate;
    private Integer version;
    private Collection<TradeItAccountEntity> accountsById;

    public CustomerEntity()
    {
    }

    public static CustomerEntity newInstance()
    {
        return new CustomerEntity();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "id", nullable = false )
    public Integer getId()
    {
        return id;
    }

    public void setId( final Integer id )
    {
        this.id = id;
    }

    @Transient
    public int getCustomerId()
    {
        return this.id;
    }

    @Transient
    public void setCustomerId( final int customerId )
    {
        this.id = customerId;
    }

    @Basic
    @Column( name = "email", nullable = false, length = 45 )
    public String getEmail()
    {
        return email;
    }

    public void setEmail( final String email )
    {
        this.email = email;
    }

    @Basic
    @Column( name = "password", nullable = false, length = 45 )
    public String getPassword()
    {
        return password;
    }

    public void setPassword( final String password )
    {
        this.password = password;
    }

    @Basic
    @Column( name = "create_date", nullable = false )
    public Timestamp getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate( final Timestamp createDate )
    {
        this.createDate = createDate;
    }

    @Basic
    @Column( name = "update_date", nullable = true )
    public Timestamp getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate( final Timestamp updateDate )
    {
        this.updateDate = updateDate;
    }

    @OneToMany( mappedBy = "customerByCustomerId" )
    public Collection<TradeItAccountEntity> getAccountsById()
    {
        return accountsById;
    }

    public void setAccountsById( final Collection<TradeItAccountEntity> accountsById )
    {
        this.accountsById = accountsById;
    }

    public void addAccount( final TradeItAccountEntity tradeItAccountEntity )
    {
        this.accountsById.add( tradeItAccountEntity );
    }

    @Override
    @Basic
    @Column( name = "version" )
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
        if ( !(o instanceof CustomerEntity) )
        {
            return false;
        }
        final CustomerEntity that = (CustomerEntity) o;
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
        final StringBuilder sb = new StringBuilder( "CustomerEntity{" );
        sb.append( "id=" ).append( id );
        sb.append( ", email='" ).append( email ).append( '\'' );
        sb.append( ", password='" ).append( password ).append( '\'' );
        sb.append( ", createDate=" ).append( createDate );
        sb.append( ", updateDate=" ).append( updateDate );
        sb.append( ", version=" ).append( updateDate );
        sb.append( '}' );
        return sb.toString();
    }
}
