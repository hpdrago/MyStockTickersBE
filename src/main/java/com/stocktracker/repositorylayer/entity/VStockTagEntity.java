package com.stocktracker.repositorylayer.entity;

import com.stocktracker.repositorylayer.CustomerUuidContainer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "v_stock_tag", schema = "stocktracker", catalog = "" )
public class VStockTagEntity implements CustomerUuidContainer
{
    private UUID uuid;
    private UUID customerUuid;
    private Integer referenceType;
    private UUID referenceUuid;
    private UUID customerTagUuid;
    private String tagName;
    private String tickerSymbol;
    private Timestamp createDate;
    private Timestamp updateDate;

    @Id
    @Column( name = "uuid" )
    public UUID getUuid()
    {
        return uuid;
    }

    public void setUuid( final UUID uuid )
    {
        this.uuid = uuid;
    }

    @Basic
    @Column( name = "customer_uuid" )
    public UUID getCustomerUuid()
    {
        return customerUuid;
    }

    public void setCustomerUuid( final UUID customerId )
    {
        this.customerUuid = customerId;
    }

    @Basic
    @Column( name = "reference_type" )
    public Integer getReferenceType()
    {
        return referenceType;
    }

    public void setReferenceType( final Integer referenceType )
    {
        this.referenceType = referenceType;
    }

    @Basic
    @Column( name = "reference_uuid" )
    public UUID getReferenceUuid()
    {
        return referenceUuid;
    }

    public void setReferenceUuid( final UUID referenceId )
    {
        this.referenceUuid = referenceId;
    }

    @Basic
    @Column( name = "customer_tag_uuid" )
    public UUID getCustomerTagUuid()
    {
        return customerTagUuid;
    }

    public void setCustomerTagUuid( final UUID tagId )
    {
        this.customerTagUuid = tagId;
    }

    @Basic
    @Column( name = "tag_name" )
    public String getTagName()
    {
        return tagName;
    }

    public void setTagName( final String tagName )
    {
        this.tagName = tagName;
    }

    @Basic
    @Column( name = "ticker_symbol" )
    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
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

        final VStockTagEntity that = (VStockTagEntity) o;

        if ( uuid != null
             ? !uuid.equals( that.uuid )
             : that.uuid != null )
        {
            return false;
        }
        if ( customerUuid != null
             ? !customerUuid.equals( that.customerUuid )
             : that.customerUuid != null )
        {
            return false;
        }
        if ( referenceType != null
             ? !referenceType.equals( that.referenceType )
             : that.referenceType != null )
        {
            return false;
        }
        if ( referenceUuid != null
             ? !referenceUuid.equals( that.referenceUuid )
             : that.referenceUuid != null )
        {
            return false;
        }
        if ( customerTagUuid != null
             ? !customerTagUuid.equals( that.customerTagUuid )
             : that.customerTagUuid != null )
        {
            return false;
        }
        if ( tagName != null
             ? !tagName.equals( that.tagName )
             : that.tagName != null )
        {
            return false;
        }
        if ( tickerSymbol != null
             ? !tickerSymbol.equals( that.tickerSymbol )
             : that.tickerSymbol != null )
        {
            return false;
        }
        if ( createDate != null
             ? !createDate.equals( that.createDate )
             : that.createDate != null )
        {
            return false;
        }
        if ( updateDate != null
             ? !updateDate.equals( that.updateDate )
             : that.updateDate != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = uuid != null
                     ? uuid.hashCode()
                     : 0;
        result = 31 * result + (customerUuid != null
                                ? customerUuid.hashCode()
                                : 0);
        result = 31 * result + (referenceType != null
                                ? referenceType.hashCode()
                                : 0);
        result = 31 * result + (referenceUuid != null
                                ? referenceUuid.hashCode()
                                : 0);
        result = 31 * result + (customerTagUuid != null
                                ? customerTagUuid.hashCode()
                                : 0);
        result = 31 * result + (tagName != null
                                ? tagName.hashCode()
                                : 0);
        result = 31 * result + (tickerSymbol != null
                                ? tickerSymbol.hashCode()
                                : 0);
        result = 31 * result + (createDate != null
                                ? createDate.hashCode()
                                : 0);
        result = 31 * result + (updateDate != null
                                ? updateDate.hashCode()
                                : 0);
        return result;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "VStockTagEntity{" );
        sb.append( "id=" ).append( uuid );
        sb.append( ", customerId=" ).append( customerUuid );
        sb.append( ", referenceType=" ).append( referenceType );
        sb.append( ", referenceId=" ).append( referenceUuid );
        sb.append( ", customerTagId=" ).append( customerTagUuid );
        sb.append( ", tagName='" ).append( tagName ).append( '\'' );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", createDate=" ).append( createDate );
        sb.append( ", updateDate=" ).append( updateDate );
        sb.append( '}' );
        return sb.toString();
    }
}
