package com.stocktracker.repositorylayer.entity;

import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.repositorylayer.common.CustomerUuidContainer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * This entity contains the information for the account that is registered with TradeIt for a brokerage.  The actual
 * brokerage account(s) are stored in the LINKED_ACCOUNT table as brokerages offer the ability to link multiple accounts
 * to a single login and thus there is a 1 to many relationship.
 * Created by Mike on December 4th, 2017
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "tradeit_account", schema = "stocktracker", catalog = "" )
public class TradeItAccountEntity extends UUIDEntity
                                  implements CustomerUuidContainer
{
    private String name;
    private String userId;
    private String userToken;
    private String brokerage;
    private String authToken;
    private String authUuid;
    private UUID customerUuid;
    private Timestamp authTimestamp;
    private boolean tradeItAccountFlag;
    //private CustomerEntity customerByCustomerUuid;
    private Collection<LinkedAccountEntity> linkedAccountsByUuid;

    public TradeItAccountEntity()
    {
    }

    public static TradeItAccountEntity newInstance()
    {
        return new TradeItAccountEntity();
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
    @Column( name = "user_token", updatable = false )
    public String getUserToken()
    {
        return userToken;
    }

    public void setUserToken( final String userToken )
    {
        this.userToken = userToken;
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

    @Override
    @Basic
    @Column( name = "customer_uuid" )
    public UUID getCustomerUuid()
    {
        return this.customerUuid;
    }

    @Override
    public void setCustomerUuid( final UUID customerUuid )
    {
        this.customerUuid = customerUuid;
    }

    /*
    @ManyToOne
    @JoinColumn( name = "customer_uuid", referencedColumnName = "uuid", nullable = false )
    public CustomerEntity getCustomerByCustomerUuid()
    {
        return customerByCustomerUuid;
    }

    public void setCustomerByCustomerUuid( final CustomerEntity customerEntity )
    {
        if ( customerEntity != null )
        {
            this.customerUuid = customerEntity.getUuid();
        }
        this.customerByCustomerUuid = customerEntity;
    }
    */

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

    @OneToMany( mappedBy = "accountByTradeItAccountUuid" )
    public Collection<LinkedAccountEntity> getLinkedAccountsByUuid()
    {
        return linkedAccountsByUuid;
    }

    @Transient
    public void setLinkedAccountsByUuid( final Collection<LinkedAccountEntity> linkedAccountsByUuid )
    {
        this.linkedAccountsByUuid = linkedAccountsByUuid;
    }

    @Transient
    public void addLinkedAccount( final LinkedAccountEntity linkedAccountEntity )
    {
        if ( this.linkedAccountsByUuid == null )
        {
            this.linkedAccountsByUuid = new ArrayList<>();
        }
        linkedAccountEntity.setCustomerUuid( this.getCustomerUuid() );
        this.linkedAccountsByUuid.add( linkedAccountEntity );
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
        if ( this.linkedAccountsByUuid == null )
        {
            return Optional.empty();
        }
        return this.linkedAccountsByUuid.stream()
                                        .filter( linkedAccountEntity -> linkedAccountEntity.getAccountNumber()
                                                                                           .equals( accountNumber ) )
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

    @Transient
    public boolean isTradeItAccount()
    {
        return tradeItAccountFlag;
    }

    @Basic
    @Column( name = "tradeit_account_flag" )
    public boolean getTradeItAccountFlag()
    {
        return tradeItAccountFlag;
    }

    public void setTradeItAccountFlag( final boolean tradeItAccountFlag )
    {
        this.tradeItAccountFlag = tradeItAccountFlag;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "TradeItAccountEntity{" );
        sb.append( "uuid=" ).append( this.getUuid() );
        sb.append( ", customerUuid=" ).append( customerUuid );
        sb.append( ", name='" ).append( name ).append( '\'' );
        sb.append( ", userId='" ).append( userId ).append( '\'' );
        sb.append( ", userToken='" ).append( userToken ).append( '\'' );
        sb.append( ", authUuid='" ).append( authUuid ).append( '\'' );
        sb.append( ", authToken='" ).append( authToken ).append( '\'' );
        sb.append( ", authTimestamp='" ).append( authTimestamp ).append( '\'' );
        sb.append( ", brokerage='" ).append( brokerage ).append( '\'' );
        //sb.append( ", linkedAccountsByUuid=" ).append( linkedAccountsByUuid );
        sb.append( ", tradeItAccountFlag=" ).append( tradeItAccountFlag );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
