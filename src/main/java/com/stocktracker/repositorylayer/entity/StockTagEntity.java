package com.stocktracker.repositorylayer.entity;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "stock_tag", schema = "stocktracker", catalog = "" )
public class StockTagEntity extends UUIDEntity
{
    private UUID customerTagUuid;
    private String tickerSymbol;
    private Integer referenceType;
    private UUID referenceUuid;

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

    @Basic
    @Column( name = "customer_tag_uuid" )
    public UUID getCustomerTagUuid()
    {
        return customerTagUuid;
    }

    public void setCustomerTagUuid( final UUID customerTagUuid )
    {
        this.customerTagUuid = customerTagUuid;
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
    @Column( name = "reference_uuid" )
    public UUID getReferenceUuid()
    {
        return referenceUuid;
    }

    public void setReferenceUuid( final UUID referenceUuid )
    {
        this.referenceUuid = referenceUuid;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockTagEntity{" );
        sb.append( "uuid=" ).append( getUuidString() );
        sb.append( ", customerTagUuid=" ).append( customerTagUuid );
        sb.append( ", tickerSymbol=" ).append( tickerSymbol );
        sb.append( ", referenceType=" ).append( referenceType );
        sb.append( ", referenceUuid=" ).append( referenceUuid );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
