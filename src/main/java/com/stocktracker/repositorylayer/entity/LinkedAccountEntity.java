package com.stocktracker.repositorylayer.entity;

import com.stocktracker.servicelayer.tradeit.types.TradeItAccount;
import com.stocktracker.weblayer.dto.tradeit.GetAccountOverviewDTO;

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
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table( name = "linked_account", schema = "stocktracker", catalog = "" )
public class LinkedAccountEntity implements VersionedEntity<Integer>
{
    private Integer id;
    private Integer customerId;
    private Integer tradeItAccountId;
    private String accountNumber;
    private String accountName;
    private String accountIndex;
    private TradeItAccountEntity accountByTradeItAccountId;
    private BigDecimal availableCash;
    private BigDecimal buyingPower;
    private BigDecimal totalValue;
    private BigDecimal dayAbsoluteReturn;
    private BigDecimal dayPercentReturn;
    private BigDecimal totalAbsoluteReturn;
    private BigDecimal totalPercentReturn;
    private BigDecimal marginCash;
    private Collection<StockPositionEntity> linkedAccountPositionsById;
    private Timestamp createDate;
    private Timestamp updateDate;
    private Integer version;

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

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "id", nullable = false )
    public Integer getId()
    {
        return id;
    }

    public void setId( final Integer id )
    {
        this.id = id;
    }

    @Basic
    @Column( name = "tradeit_account_id", updatable = false, insertable = false, nullable = false )
    public Integer getTradeItAccountId()
    {
        return tradeItAccountId;
    }

    public void setTradeItAccountId( final Integer tradeItAccountId )
    {
        this.tradeItAccountId = tradeItAccountId;
    }

    @Basic
    @Column( name = "customer_id", nullable = false )
    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( final Integer customerId )
    {
        this.customerId = customerId;
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
    @Column( name = "update_date" )
    public Timestamp getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate( final Timestamp updateDate )
    {
        this.updateDate = updateDate;
    }

    @Override
    @Basic
    @Column( name = "version" )
    public Integer getVersion()
    {
        return this.version;
    }

    public void setVersion( final Integer version )
    {
        this.version = version;
    }


    @ManyToOne
    @JoinColumn( name = "tradeit_account_id", referencedColumnName = "id", nullable = false )
    public TradeItAccountEntity getAccountByTradeItAccountId()
    {
        return accountByTradeItAccountId;
    }

    public void setAccountByTradeItAccountId( final TradeItAccountEntity accountByTradeItAccountId )
    {
        this.accountByTradeItAccountId = accountByTradeItAccountId;
        this.tradeItAccountId = accountByTradeItAccountId.getId();
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

    @OneToMany( mappedBy = "linkedAccountByLinkedAccountId" )
    public Collection<StockPositionEntity> getLinkedAccountPositionsById()
    {
        return linkedAccountPositionsById;
    }

    public void setLinkedAccountPositionsById( final Collection<StockPositionEntity> linkedAccountPositionsById )
    {
        this.linkedAccountPositionsById = linkedAccountPositionsById;
    }

    /**
     * Copy the values form the result of the TradeIt getAccountOverview call.
     * @param accountOverviewDTO
     */
    @Transient
    public void setAccountOverviewValues( final GetAccountOverviewDTO accountOverviewDTO )
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
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", tradeItAccountId=" ).append( tradeItAccountId );
        sb.append( ", accountNumber='" ).append( accountNumber ).append( '\'' );
        sb.append( ", accountName='" ).append( accountName ).append( '\'' );
        sb.append( ", accountIndex=" ).append( accountIndex );
        sb.append( ", createDate=" ).append( createDate );
        sb.append( ", updateDate=" ).append( updateDate );
        sb.append( ", availableCache=" ).append( availableCash );
        sb.append( ", buyingPower=" ).append( buyingPower );
        sb.append( ", totalValue=" ).append( totalValue );
        sb.append( ", dayAbsoluteReturn=" ).append( dayAbsoluteReturn );
        sb.append( ", dayPercentReturn=" ).append( dayPercentReturn );
        sb.append( ", totalAbsoluteReturn=" ).append( totalAbsoluteReturn );
        sb.append( ", totalPercentReturn=" ).append( totalPercentReturn );
        sb.append( ", marginCash=" ).append( marginCash );
        sb.append( '}' );
        return sb.toString();
    }
}
