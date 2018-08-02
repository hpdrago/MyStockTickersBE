package com.stocktracker.repositorylayer.entity;

import com.stocktracker.repositorylayer.common.CustomerUuidContainer;
import com.stocktracker.servicelayer.tradeit.types.TradeItAccount;
import com.stocktracker.weblayer.dto.tradeit.GetAccountOverviewDTO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "linked_account", schema = "stocktracker", catalog = "" )
public class LinkedAccountEntity extends UUIDEntity
                                 implements CustomerUuidContainer
{
    private UUID customerUuid;
    private UUID tradeItAccountUuid;
    private String accountNumber;
    private String accountName;
    private String accountIndex;
    private TradeItAccountEntity accountByTradeItAccountUuid;
    private String getAccountOverviewStatus;
    private BigDecimal availableCash;
    private BigDecimal buyingPower;
    private BigDecimal totalValue;
    private BigDecimal dayAbsoluteReturn;
    private BigDecimal dayPercentReturn;
    private BigDecimal totalAbsoluteReturn;
    private BigDecimal totalPercentReturn;
    private BigDecimal marginCash;
    private Collection<StockPositionEntity> linkedAccountPositionsByUuid;

    /**
     * Creates a new instance from the information contained within {@code TradeItAccountDTO}.
     * @param tradeItAccount
     * @return
     */
    public static LinkedAccountEntity newInstance( final TradeItAccount tradeItAccount )
    {
        LinkedAccountEntity linkedAccountEntity = new LinkedAccountEntity();
        linkedAccountEntity.accountName = tradeItAccount.getName();
        linkedAccountEntity.accountIndex = tradeItAccount.getAccountIndex();
        linkedAccountEntity.accountNumber = tradeItAccount.getAccountNumber();
        return linkedAccountEntity;
    }

    @Basic
    @Column( name = "customer_uuid", nullable = false )
    public UUID getCustomerUuid()
    {
        return customerUuid;
    }

    public void setCustomerUuid( final UUID customerUuid )
    {
        this.customerUuid = customerUuid;
    }

    @Basic
    @Column( name = "tradeit_account_uuid", nullable = false, insertable = false, updatable = false)
    public UUID getTradeItAccountUuid()
    {
        return tradeItAccountUuid;
    }

    public void setTradeItAccountUuid( final UUID tradeitAccountUuid )
    {
        this.tradeItAccountUuid = tradeitAccountUuid;
    }

    @Basic
    @Column( name = "account_number", nullable = false, length = 20 )
    public String getAccountNumber()
    {
        return accountNumber;
    }

    public void setAccountNumber( final String accountNumber )
    {
        this.accountNumber = accountNumber;
    }

    @Basic
    @Column( name = "account_name", nullable = false, length = 40 )
    public String getAccountName()
    {
        return accountName;
    }

    public void setAccountName( final String accountName )
    {
        this.accountName = accountName;
    }

    @Basic
    @Column( name = "account_index", nullable = false, length = 30 )
    public String getAccountIndex()
    {
        return accountIndex;
    }

    public void setAccountIndex( final String accountIndex )
    {
        this.accountIndex = accountIndex;
    }

    public void setAccountByTradeItAccountId( final TradeItAccountEntity tradeItAccountEntity )
    {
        this.accountByTradeItAccountUuid = tradeItAccountEntity;
        this.tradeItAccountUuid = tradeItAccountEntity.getUuid();
    }

    /**
     * Determines if the the account name and account number are the same
     * @param accountName
     * @param accountNumber
     * @return
     */
    public boolean isAccountDetailsEqual( final String accountName, final String accountNumber )
    {
        return Objects.equals( this.accountName, accountName ) &&
               Objects.equals( this.accountNumber, accountNumber );
    }

    @Basic
    @Column( name = "available_cash", nullable = true, precision = 2 )
    public BigDecimal getAvailableCash()
    {
        return availableCash;
    }

    public void setAvailableCash( final BigDecimal availableCache )
    {
        this.availableCash = availableCache;
    }

    @Basic
    @Column( name = "buying_power", nullable = true, precision = 2 )
    public BigDecimal getBuyingPower()
    {
        return buyingPower;
    }

    public void setBuyingPower( final BigDecimal buyingPower )
    {
        this.buyingPower = buyingPower;
    }

    @Basic
    @Column( name = "total_value", nullable = true, precision = 2 )
    public BigDecimal getTotalValue()
    {
        return totalValue;
    }

    public void setTotalValue( final BigDecimal totalValue )
    {
        this.totalValue = totalValue;
    }

    @Basic
    @Column( name = "day_absolute_return", nullable = true, precision = 2 )
    public BigDecimal getDayAbsoluteReturn()
    {
        return dayAbsoluteReturn;
    }

    public void setDayAbsoluteReturn( final BigDecimal dayAbsoluteReturn )
    {
        this.dayAbsoluteReturn = dayAbsoluteReturn;
    }

    @Basic
    @Column( name = "day_percent_return", nullable = true, precision = 2 )
    public BigDecimal getDayPercentReturn()
    {
        return dayPercentReturn;
    }

    public void setDayPercentReturn( final BigDecimal dayPercentReturn )
    {
        this.dayPercentReturn = dayPercentReturn;
    }

    @Basic
    @Column( name = "total_absolute_return", nullable = true, precision = 2 )
    public BigDecimal getTotalAbsoluteReturn()
    {
        return totalAbsoluteReturn;
    }

    public void setTotalAbsoluteReturn( final BigDecimal totalAbsoluteReturn )
    {
        this.totalAbsoluteReturn = totalAbsoluteReturn;
    }

    @Basic
    @Column( name = "total_percent_return", nullable = true, precision = 2 )
    public BigDecimal getTotalPercentReturn()
    {
        return totalPercentReturn;
    }

    public void setTotalPercentReturn( final BigDecimal totalPercentReturn )
    {
        this.totalPercentReturn = totalPercentReturn;
    }

    @Basic
    @Column( name = "margin_cash", nullable = true, precision = 2 )
    public BigDecimal getMarginCash()
    {
        return marginCash;
    }

    public void setMarginCash( final BigDecimal marginCash )
    {
        this.marginCash = marginCash;
    }

    @OneToMany( mappedBy = "linkedAccountByLinkedAccountUuid" )
    public Collection<StockPositionEntity> getLinkedAccountPositionsByUuid()
    {
        return linkedAccountPositionsByUuid;
    }

    public void setLinkedAccountPositionsByUuid( final Collection<StockPositionEntity> linkedAccountPositions )
    {
        this.linkedAccountPositionsByUuid = linkedAccountPositions;
    }

    @ManyToOne
    @JoinColumn( name = "tradeit_account_uuid", referencedColumnName = "uuid" )
    public TradeItAccountEntity getAccountByTradeItAccountUuid()
    {
        return accountByTradeItAccountUuid;
    }

    public void setAccountByTradeItAccountUuid( final TradeItAccountEntity accountByTradeItAccountUuid )
    {
        this.accountByTradeItAccountUuid = accountByTradeItAccountUuid;
    }

    /**
     * Copy the values form the result of the TradeIt getAccountOverview call.
     * @param accountOverviewDTO
     */
    @Transient
    public void setGetAccountOverviewValues( final GetAccountOverviewDTO accountOverviewDTO )
    {
        this.availableCash = new BigDecimal( accountOverviewDTO.getAvailableCash() );
        this.buyingPower = new BigDecimal( accountOverviewDTO.getBuyingPower() );
        this.totalValue = new BigDecimal( accountOverviewDTO.getTotalValue() );
        this.dayAbsoluteReturn = new BigDecimal( accountOverviewDTO.getDayAbsoluteReturn() );
        this.dayPercentReturn = new BigDecimal( accountOverviewDTO.getDayPercentReturn() );
        this.totalAbsoluteReturn = new BigDecimal( accountOverviewDTO.getTotalAbsoluteReturn() );
        this.totalPercentReturn = new BigDecimal( accountOverviewDTO.getTotalPercentReturn() );
        this.marginCash = new BigDecimal( accountOverviewDTO.getMarginCash() );
    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !(o instanceof LinkedAccountEntity) )
        {
            return false;
        }

        final LinkedAccountEntity that = (LinkedAccountEntity) o;

        return accountNumber != null
               ? accountNumber.equals( that.accountNumber )
               : that.accountNumber == null;
    }

    @Override
    public int hashCode()
    {
        return accountNumber != null
               ? accountNumber.hashCode()
               : 0;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "LinkedAccountEntity{" );
        sb.append( "uuid=" ).append( getUuidString() );
        sb.append( ", customerUuid=" ).append( customerUuid );
        sb.append( ", tradeItAccountUuid=" ).append( tradeItAccountUuid );
        sb.append( ", accountNumber='" ).append( accountNumber ).append( '\'' );
        sb.append( ", accountName='" ).append( accountName ).append( '\'' );
        sb.append( ", accountIndex=" ).append( accountIndex );
        sb.append( ", availableCache=" ).append( availableCash );
        sb.append( ", buyingPower=" ).append( buyingPower );
        sb.append( ", totalValue=" ).append( totalValue );
        sb.append( ", dayAbsoluteReturn=" ).append( dayAbsoluteReturn );
        sb.append( ", dayPercentReturn=" ).append( dayPercentReturn );
        sb.append( ", totalAbsoluteReturn=" ).append( totalAbsoluteReturn );
        sb.append( ", totalPercentReturn=" ).append( totalPercentReturn );
        sb.append( ", marginCash=" ).append( marginCash );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
