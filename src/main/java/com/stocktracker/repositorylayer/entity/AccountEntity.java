package com.stocktracker.repositorylayer.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 *
 * Created by Mike on December 4th, 2017
 */
@Entity
@Table( name = "account", schema = "stocktracker", catalog = "" )
public class AccountEntity
{
    private Integer id;
    private Integer customerId;
    private String name;
    private String userId;
    private String userToken;
    private String sessionToken;
    private String brokerage;
    private Timestamp createDate;
    private Timestamp updateDate;
    private CustomerEntity customerByCustomerId;

    public static AccountEntity newInstance()
    {
        return new AccountEntity();
    }

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column( name = "id" )
    public Integer getId()
    {
        return id;
    }

    public void setId( final Integer id )
    {
        this.id = id;
    }

    @Column( name = "customer_id", insertable = false, updatable = false )
    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( final Integer customerId )
    {
        this.customerId = customerId;
    }

    @Basic
    @Column( name = "name" )
    public String getName()
    {
        return name;
    }

    public void setName( final String name )
    {
        this.name = name;
    }

    @Basic
    @Column( name = "user_token" )
    public String getUserToken()
    {
        return userToken;
    }

    public void setUserToken( final String loginToken )
    {
        this.userToken = loginToken;
    }

    @Basic
    @Column( name = "brokerage" )
    public String getBrokerage()
    {
        return brokerage;
    }

    public void setBrokerage( final String brokerage )
    {
        this.brokerage = brokerage;
    }

    @Basic
    @Column( name = "create_date" )
    public Timestamp getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate( final Timestamp createDate )
    {
        this.createDate = createDate;
    }

    @Basic
    @Column( name = "update_date" )
    public Timestamp getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate( final Timestamp updateDate )
    {
        this.updateDate = updateDate;
    }

    @ManyToOne
    @JoinColumn( name = "customer_id", referencedColumnName = "id", nullable = false )
    public CustomerEntity getCustomerByCustomerId()
    {
        return customerByCustomerId;
    }

    public void setCustomerByCustomerId( final CustomerEntity customerByCustomerId )
    {
        this.customerByCustomerId = customerByCustomerId;
    }

    @Basic
    @Column( name = "user_id" )
    public String getUserId()
    {
        return userId;
    }

    public void setUserId( final String userId )
    {
        this.userId = userId;
    }

    @Basic
    @Column( name = "session_token" )
    public String getSessionToken()
    {
        return sessionToken;
    }

    public void setSessionToken( final String sessionToken )
    {
        this.sessionToken = sessionToken;
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !(o instanceof AccountEntity) )
        {
            return false;
        }

        final AccountEntity that = (AccountEntity) o;

        return getId().equals( that.getId() );
    }

    @Override
    public int hashCode()
    {
        return getId().hashCode();
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "AccountEntity{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", name='" ).append( name ).append( '\'' );
        sb.append( ", userId='" ).append( userToken ).append( '\'' );
        sb.append( ", userToken='" ).append( userToken ).append( '\'' );
        sb.append( ", sessionToken='" ).append( userToken ).append( '\'' );
        sb.append( ", brokerage='" ).append( brokerage ).append( '\'' );
        sb.append( ", createDate=" ).append( createDate );
        sb.append( ", updateDate=" ).append( updateDate );
        sb.append( ", customerByCustomerId=" ).append( customerByCustomerId );
        sb.append( '}' );
        return sb.toString();
    }
}
