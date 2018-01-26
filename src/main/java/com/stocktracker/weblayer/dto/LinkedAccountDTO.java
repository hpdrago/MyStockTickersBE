package com.stocktracker.weblayer.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stocktracker.common.JSONMoneySerializer;
import com.stocktracker.common.JSONTimestampDateTimeSerializer;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * This is the DTO class for the {@code LinkedAccountEntity} class.
 */
public class LinkedAccountDTO
{
    private Integer id;
    private Integer customerId;
    private Integer parentAccountId;
    private String accountNumber;
    private String accountName;
    private Integer accountIndex;
    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    private Timestamp createDate;
    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    private Timestamp updateDate;
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

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public Integer getParentAccountId()
    {
        return parentAccountId;
    }

    public void setParentAccountId( Integer parentAccountId )
    {
        this.parentAccountId = parentAccountId;
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

    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( final Integer customerId )
    {
        this.customerId = customerId;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "LinkedAccountDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", parentAccountId=" ).append( parentAccountId );
        sb.append( ", accountNumber='" ).append( accountNumber ).append( '\'' );
        sb.append( ", accountName='" ).append( accountName ).append( '\'' );
        sb.append( ", accountIndex=" ).append( accountIndex );
        sb.append( ", createDate=" ).append( createDate );
        sb.append( ", updateDate=" ).append( updateDate );
        sb.append( ", availableCash=" ).append( availableCash );
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
