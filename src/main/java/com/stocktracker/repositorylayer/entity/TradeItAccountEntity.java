package com.stocktracker.repositorylayer.entity;

import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * This entity contains the information for the account that is registered with TradeIt for a brokerage.  The actual
 * brokerage account(s) are stored in the LINKED_ACCOUNT table as brokerages offer the ability to link multiple accounts
 * to a single login and thus there is a 1 to many relationship.
 * Created by Mike on December 4th, 2017
 */
@Entity
@Table( name = "tradeit_account", schema = "stocktracker", catalog = "" )
public class TradeItAccountEntity
{
    private Integer id;
    private Integer customerId;
    private String name;
    private String userId;
    private String userToken;
    private String brokerage;
    private String authToken;
    private String authUuid;
    private Timestamp authTimestamp;
    private Timestamp createDate;
    private Timestamp updateDate;
    private CustomerEntity customerByCustomerId;
    private Collection<LinkedAccountEntity> linkedAccountsById;

    public static TradeItAccountEntity newInstance()
    {
        return new TradeItAccountEntity();
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
    @Column( name = "auth_uuid" )
    public String getAuthUuid()
    {
        return authUuid;
    }

    public void setAuthUuid( final String authUuid )
    {
        this.authUuid = authUuid;
    }

    @OneToMany( mappedBy = "accountByParentAccountId" )
    public Collection<LinkedAccountEntity> getLinkedAccountsById()
    {
        return linkedAccountsById;
    }

    @Transient
    public void setLinkedAccountsById( final Collection<LinkedAccountEntity> linkedAccountsById )
    {
        this.linkedAccountsById = linkedAccountsById;
    }

    @Transient
    public void addLinkedAccount( final LinkedAccountEntity linkedAccountEntity )
    {
        if ( this.linkedAccountsById == null )
        {
            this.linkedAccountsById = new ArrayList<>();
        }
        this.linkedAccountsById.add( linkedAccountEntity );
    }

    /**
     * Gets the linked account for the {@code accountNumber}.  If the account is not found, then the
     * {@code LinkedAccountNotFoundException} is thrown. This is the same call as {@code getLinkedAccount} without the
     * optional and throws the exception;
     * @param accountNumber The account number to search for.
     * @param tradeItAccountEntity The parent account information used for detailed exception information.
     * @return
     * @throws LinkedAccountNotFoundException
     */
    @Transient
    public LinkedAccountEntity getLinkedAccount( final String accountNumber, final TradeItAccountEntity tradeItAccountEntity )
        throws LinkedAccountNotFoundException
    {
        final Optional<LinkedAccountEntity> linkedAccountEntity = this.getLinkedAccount( accountNumber );
        if ( !linkedAccountEntity.isPresent() )
        {
            throw new LinkedAccountNotFoundException( accountNumber, tradeItAccountEntity );
        }
        return linkedAccountEntity.get();
    }

    /**
     * Searches the linked accounts for the account number matching {@code accountNumber}.
     * @param accountNumber
     * @return
     */
    @Transient
    public Optional<LinkedAccountEntity> getLinkedAccount( final String accountNumber )
    {
        if ( this.linkedAccountsById == null )
        {
            return Optional.empty();
        }
        return this.linkedAccountsById.stream()
                                      .filter( linkedAccountEntity -> linkedAccountEntity.getAccountNumber().equals( accountNumber ) )
                                      .findFirst();
    }

    @Basic
    @Column( name = "auth_token" )
    public String getAuthToken()
    {
        return authToken;
    }

    public void setAuthToken( final String authToken )
    {
        this.authToken = authToken;
    }

    @Basic
    @Column( name = "auth_timestamp" )
    public Timestamp getAuthTimestamp()
    {
        return authTimestamp;
    }

    public void setAuthTimestamp( final Timestamp authTimestamp )
    {
        this.authTimestamp = authTimestamp;
    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !(o instanceof TradeItAccountEntity) )
        {
            return false;
        }

        final TradeItAccountEntity that = (TradeItAccountEntity) o;

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
        final StringBuilder sb = new StringBuilder( "TradeItAccountEntity{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", name='" ).append( name ).append( '\'' );
        sb.append( ", userId='" ).append( userToken ).append( '\'' );
        sb.append( ", userToken='" ).append( userToken ).append( '\'' );
        sb.append( ", authUuid='" ).append( authUuid ).append( '\'' );
        sb.append( ", authToken='" ).append( authToken ).append( '\'' );
        sb.append( ", authTimestamp='" ).append( authTimestamp ).append( '\'' );
        sb.append( ", brokerage='" ).append( brokerage ).append( '\'' );
        sb.append( ", createDate=" ).append( createDate );
        sb.append( ", updateDate=" ).append( updateDate );
        sb.append( ", customerByCustomerId=" ).append( customerByCustomerId );
        sb.append( ", linkedAccountsById=" ).append( linkedAccountsById );
        sb.append( '}' );
        return sb.toString();
    }
}
