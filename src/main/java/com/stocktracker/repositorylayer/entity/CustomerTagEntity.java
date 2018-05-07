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

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "customer_tag", schema = "stocktracker", catalog = "" )
public class CustomerTagEntity extends UUIDEntity
                               implements CustomerUuidContainer
{
    private UUID customerUuid;
    private String tagName;

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
    @Column( name = "tag_name", nullable = false, length = 20 )
    public String getTagName()
    {
        return tagName;
    }

    public void setTagName( final String tagName )
    {
        this.tagName = tagName;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "CustomerTagEntity{" );
        sb.append( "uuid=" ).append( getUuidString() );
        sb.append( ", tagName='" ).append( tagName ).append( '\'' );
        sb.append( ", super=" ).append( tagName ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
