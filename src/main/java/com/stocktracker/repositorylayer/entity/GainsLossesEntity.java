package com.stocktracker.repositorylayer.entity;

import com.stocktracker.repositorylayer.common.CustomerUuidContainer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * This entity contains the gains and losses for a single stock for a customers linked account.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "gains_losses", schema = "stocktracker", catalog = "" )
public class GainsLossesEntity extends UUIDEntity
                               implements CustomerUuidContainer
{
    private String tickerSymbol;
    private UUID customerUuid;
    private UUID linkedAccountUuid;
    private BigDecimal gains;
    private BigDecimal losses;
    private BigDecimal totalGainsLosses;
    private LinkedAccountEntity linkedAccountByLinkedAccountUuid;

    @Basic
    @Column( name = "ticker_symbol", nullable = false, length = 20 )
    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    @Basic
    @Column( name = "gains", nullable = true, precision = 2 )
    public BigDecimal getGains()
    {
        return gains;
    }

    public void setGains( final BigDecimal gains )
    {
        this.gains = gains;
    }

    @Basic
    @Column( name = "losses", nullable = true, precision = 2 )
    public BigDecimal getLosses()
    {
        return losses;
    }

    public void setLosses( final BigDecimal losses )
    {
        this.losses = losses;
    }

    @Basic
    @Column( name = "total_gains_losses", nullable = true, precision = 2 )
    public BigDecimal getTotalGainsLosses()
    {
        return totalGainsLosses;
    }

    public void setTotalGainsLosses( final BigDecimal totalGainsLosses )
    {
        this.totalGainsLosses = totalGainsLosses;
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
    @Column( name = "linked_account_uuid", nullable = false, updatable = false, insertable = false )
    public UUID getLinkedAccountUuid()
    {
        return linkedAccountUuid;
    }

    public void setLinkedAccountUuid( final UUID linkedAccountUuid )
    {
        this.linkedAccountUuid = linkedAccountUuid;
    }

    @ManyToOne
    @JoinColumn( name = "linked_account_uuid", referencedColumnName = "uuid", nullable = false )
    public LinkedAccountEntity getLinkedAccountByLinkedAccountUuid()
    {
        return linkedAccountByLinkedAccountUuid;
    }

    public void setLinkedAccountByLinkedAccountUuid( final LinkedAccountEntity linkedAccountByLinkedAccountUuid )
    {
        this.linkedAccountByLinkedAccountUuid = linkedAccountByLinkedAccountUuid;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "GainsLossesEntity{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", customerUuid=" ).append( customerUuid );
        sb.append( ", linkedAccountUuid=" ).append( linkedAccountUuid );
        sb.append( ", gains=" ).append( gains );
        sb.append( ", losses=" ).append( losses );
        sb.append( ", totalGainsLosses=" ).append( totalGainsLosses );
        sb.append( ", linkedAccountByLinkedAccountUuid=" ).append( linkedAccountByLinkedAccountUuid );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
