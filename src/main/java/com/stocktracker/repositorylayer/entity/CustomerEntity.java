package com.stocktracker.repositorylayer.entity;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collection;

/**
 * Created by mike on 9/2/2016.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "customer", schema = "stocktracker", catalog = "" )
public class CustomerEntity extends UUIDEntity
{
    private String email;
    private String password;
    //private Collection<TradeItAccountEntity> accountsById;

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

    /*
    @OneToMany( mappedBy = "customerByCustomerUuid" )
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
    */

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "CustomerEntity{" );
        sb.append( "uuid=" ).append( getUuidString() );
        sb.append( ", email='" ).append( email ).append( '\'' );
        sb.append( ", password='" ).append( password ).append( '\'' );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
