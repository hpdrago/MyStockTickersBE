package com.stocktracker.repositorylayer.entity;

import com.stocktracker.repositorylayer.common.CustomerUuidContainer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Created by mike on 9/4/2016.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "portfolio", schema = "stocktracker", catalog = "" )
public class PortfolioEntity extends UUIDEntity
                             implements CustomerUuidContainer
{
    private String name;
    private UUID customerUuid;
    private UUID linkedAccountUuid;

    @Basic
    @Column( name = "name", nullable = false, length = 20 )
    public String getName()
    {
        return name;
    }

    public void setName( final String name )
    {
        this.name = name;
    }

    @Basic
    @Column( name = "customer_uuid", nullable = false )
    public UUID getCustomerUuid()
    {
        return customerUuid;
    }

    public void setCustomerUuid( UUID customerId )
    {
        this.customerUuid = customerId;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "PortfolioEntity{" );
        sb.append( "uuid=" ).append( getUuidString() );
        sb.append( ", name='" ).append( name ).append( '\'' );
        sb.append( ", linkedAccountUuid=" ).append( linkedAccountUuid );
        sb.append( ", customerUuid=" ).append( customerUuid );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
