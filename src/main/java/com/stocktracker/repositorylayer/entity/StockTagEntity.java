package com.stocktracker.repositorylayer.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table( name = "stock_tag", schema = "stocktracker", catalog = "" )
public class StockTagEntity
{
    private Integer id;
    private Integer customerTagId;
    private String tickerSymbol;
    private Integer referenceType;
    private Integer referenceId;
    private Timestamp createDate;
    private Timestamp updateDate;

    public enum StockTagReferenceType
    {
        STOCK_TO_BUY( 1 ),
        STOCK_NOTES( 2 ),
        STOCK_CATALYST_EVENT( 3 );

        private int referenceType;
        StockTagReferenceType( final int referenceType )
        {
            this.referenceType = referenceType;
        }

        public int getReferenceType()
        {
            return this.referenceType;
        }

        /**
         * Get the integer (database storage) value
         * @param referenceId
         * @return
         */
        public static StockTagReferenceType fromInt( final int referenceId )
        {
            for ( StockTagReferenceType referenceType:  StockTagReferenceType.values() )
            {
                if ( referenceType.referenceType == referenceId )
                {
                    return referenceType;
                }
            }
            throw new IllegalArgumentException( referenceId + " is not a valid stock tag reference type" );
        }
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

    @Basic
    @Column( name = "customer_tag_id" )
    public Integer getCustomerTagId()
    {
        return customerTagId;
    }

    public void setCustomerTagId( final Integer customerTagId )
    {
        this.customerTagId = customerTagId;
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

        final StockTagEntity that = (StockTagEntity) o;

        return id.equals( that.id );
    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockTagEntity{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerTagId=" ).append( customerTagId );
        sb.append( ", tickerSymbol=" ).append( tickerSymbol );
        sb.append( ", referenceType=" ).append( referenceType );
        sb.append( ", referenceType=" ).append( referenceId );
        sb.append( ", createDate=" ).append( createDate );
        sb.append( ", updateDate=" ).append( updateDate );
        sb.append( '}' );
        return sb.toString();
    }
}
