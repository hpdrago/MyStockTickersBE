package com.stocktracker.weblayer.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stocktracker.common.EntityLoadingStatus;
import com.stocktracker.common.JSONMoneySerializer;
import com.stocktracker.common.JSONTimestampDateTimeSerializer;
import com.stocktracker.weblayer.dto.tradeit.GetAccountOverviewDTO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * This is the DTO class for the {@code LinkedAccountEntity} class.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class LinkedAccountDTO implements UuidDTO,
                                         CustomerIdContainer
{
    private String id;
    private String customerId;
    private String tradeItAccountId;
    private String accountNumber;
    private String accountName;
    private Integer accountIndex;
    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    private Timestamp createDate;
    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    private Timestamp updateDate;
    private Integer version;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal availableCash;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal buyingPower;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal totalValue;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal dayAbsoluteReturn;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal dayPercentReturn;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal totalAbsoluteReturn;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal totalPercentReturn;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal marginCash;
    private String loadingStatus;

    public String getId()
    {
        return id;
    }

    public void setId( final String id )
    {
        this.id = id;
    }

    public String getTradeItAccountId()
    {
        return tradeItAccountId;
    }

    public void setTradeItAccountId( String tradeItAccountId )
    {
        this.tradeItAccountId = tradeItAccountId;
    }

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public void setAccountNumber( String accountNumber )
    {
        this.accountNumber = accountNumber;
    }

    public String getAccountName()
    {
        return accountName;
    }

    public void setAccountName( String accountName )
    {
        this.accountName = accountName;
    }

    public Integer getAccountIndex()
    {
        return accountIndex;
    }

    public void setAccountIndex( Integer accountIndex )
    {
        this.accountIndex = accountIndex;
    }

    public Timestamp getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate( Timestamp createDate )
    {
        this.createDate = createDate;
    }

    public Timestamp getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate( Timestamp updateDate )
    {
        this.updateDate = updateDate;
    }

    public BigDecimal getAvailableCash()
    {
        return availableCash;
    }

    public void setAvailableCash( BigDecimal availableCash )
    {
        this.availableCash = availableCash;
    }

    public BigDecimal getBuyingPower()
    {
        return buyingPower;
    }

    public void setBuyingPower( BigDecimal buyingPower )
    {
        this.buyingPower = buyingPower;
    }

    public BigDecimal getTotalValue()
    {
        return totalValue;
    }

    public void setTotalValue( BigDecimal totalValue )
    {
        this.totalValue = totalValue;
    }

    public BigDecimal getDayAbsoluteReturn()
    {
        return dayAbsoluteReturn;
    }

    public void setDayAbsoluteReturn( BigDecimal dayAbsoluteReturn )
    {
        this.dayAbsoluteReturn = dayAbsoluteReturn;
    }

    public BigDecimal getDayPercentReturn()
    {
        return dayPercentReturn;
    }

    public void setDayPercentReturn( BigDecimal dayPercentReturn )
    {
        this.dayPercentReturn = dayPercentReturn;
    }

    public BigDecimal getTotalAbsoluteReturn()
    {
        return totalAbsoluteReturn;
    }

    public void setTotalAbsoluteReturn( BigDecimal totalAbsoluteReturn )
    {
        this.totalAbsoluteReturn = totalAbsoluteReturn;
    }

    public BigDecimal getTotalPercentReturn()
    {
        return totalPercentReturn;
    }

    public void setTotalPercentReturn( BigDecimal totalPercentReturn )
    {
        this.totalPercentReturn = totalPercentReturn;
    }

    public BigDecimal getMarginCash()
    {
        return marginCash;
    }

    public void setMarginCash( BigDecimal marginCash )
    {
        this.marginCash = marginCash;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( final String customerId )
    {
        this.customerId = customerId;
    }


    public Integer getVersion()
    {
        return version;
    }

    public void setVersion( final Integer version )
    {
        this.version = version;
    }

    public void setLoadingStatus( final EntityLoadingStatus entityLoadingStatus )
    {
        this.loadingStatus = entityLoadingStatus.name();
    }

    public String getLoadingStatus()
    {
        return loadingStatus;
    }

    public void setLoadingStatus( final String loadingStatus )
    {
        this.loadingStatus = loadingStatus;
    }

    /**
     * Copy account summary info.
     * @param getAccountOverviewDTO
     */
    public void copyAccountSummary( final GetAccountOverviewDTO getAccountOverviewDTO )
    {
        this.availableCash = new BigDecimal( getAccountOverviewDTO.getAvailableCash() );
        this.marginCash = new BigDecimal( getAccountOverviewDTO.getMarginCash() );
        this.buyingPower = new BigDecimal( getAccountOverviewDTO.getBuyingPower() );
        this.totalValue = new BigDecimal( getAccountOverviewDTO.getTotalValue() );
        this.dayAbsoluteReturn = new BigDecimal( getAccountOverviewDTO.getDayAbsoluteReturn() );
        this.dayPercentReturn = new BigDecimal( getAccountOverviewDTO.getDayPercentReturn() );
        this.totalAbsoluteReturn = new BigDecimal( getAccountOverviewDTO.getTotalAbsoluteReturn() );
        this.totalPercentReturn = new BigDecimal( getAccountOverviewDTO.getTotalPercentReturn() );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "LinkedAccountDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", tradeItAccountId=" ).append( tradeItAccountId );
        sb.append( ", accountNumber='" ).append( accountNumber ).append( '\'' );
        sb.append( ", accountName='" ).append( accountName ).append( '\'' );
        sb.append( ", accountIndex=" ).append( accountIndex );
        sb.append( ", createDate=" ).append( createDate );
        sb.append( ", updateDate=" ).append( updateDate );
        sb.append( ", version=" ).append( version );
        sb.append( ", availableCash=" ).append( availableCash );
        sb.append( ", buyingPower=" ).append( buyingPower );
        sb.append( ", totalValue=" ).append( totalValue );
        sb.append( ", dayAbsoluteReturn=" ).append( dayAbsoluteReturn );
        sb.append( ", dayPercentReturn=" ).append( dayPercentReturn );
        sb.append( ", totalAbsoluteReturn=" ).append( totalAbsoluteReturn );
        sb.append( ", totalPercentReturn=" ).append( totalPercentReturn );
        sb.append( ", marginCash=" ).append( marginCash );
        sb.append( ", loadingStatus=" ).append( loadingStatus );
        sb.append( '}' );
        return sb.toString();
    }
}
