package com.stocktracker.repositorylayer.entity;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "v_stock_tag", schema = "stocktracker", catalog = "" )
public class VStockTagEntity
{
    private Integer id;
    private Integer customerId;
    private Integer referenceType;
    private Integer referenceId;
    private Integer customerTagId;
    private String tagName;
    private String tickerSymbol;
    private Timestamp createDate;
    private Timestamp updateDate;

    @Id
    @Column( name = "id" )
    public Integer getId()
    {
        return id;
    }

    public void setId( final Integer id )
    {
        this.id = id;
    }

    @Basic
    @Column( name = "customer_id" )
    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( final Integer customerId )
    {
        this.customerId = customerId;
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
    @Column( name = "reference_id" )
    public Integer getReferenceId()
    {
        return referenceId;
    }

    public void setReferenceId( final Integer referenceId )
    {
        this.referenceId = referenceId;
    }

    @Basic
    @Column( name = "customer_tag_id" )
    public Integer getCustomerTagId()
    {
        return customerTagId;
    }

    public void setCustomerTagId( final Integer tagId )
    {
        this.customerTagId = tagId;
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

        if ( id != null
             ? !id.equals( that.id )
             : that.id != null )
        {
            return false;
        }
        if ( customerId != null
             ? !customerId.equals( that.customerId )
             : that.customerId != null )
        {
            return false;
        }
        if ( referenceType != null
             ? !referenceType.equals( that.referenceType )
             : that.referenceType != null )
        {
            return false;
        }
        if ( referenceId != null
             ? !referenceId.equals( that.referenceId )
             : that.referenceId != null )
        {
            return false;
        }
        if ( customerTagId != null
             ? !customerTagId.equals( that.customerTagId )
             : that.customerTagId != null )
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
        int result = id != null
                     ? id.hashCode()
                     : 0;
        result = 31 * result + (customerId != null
                                ? customerId.hashCode()
                                : 0);
        result = 31 * result + (referenceType != null
                                ? referenceType.hashCode()
                                : 0);
        result = 31 * result + (referenceId != null
                                ? referenceId.hashCode()
                                : 0);
        result = 31 * result + (customerTagId != null
                                ? customerTagId.hashCode()
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
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", referenceType=" ).append( referenceType );
        sb.append( ", referenceId=" ).append( referenceId );
        sb.append( ", customerTagId=" ).append( customerTagId );
        sb.append( ", tagName='" ).append( tagName ).append( '\'' );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", createDate=" ).append( createDate );
        sb.append( ", updateDate=" ).append( updateDate );
        sb.append( '}' );
        return sb.toString();
    }
}
