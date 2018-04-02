package com.stocktracker.weblayer.dto;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/10/2017.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockNoteSourceDTO implements VersionedDTO<Integer>
{
    private Integer id;
    private String name;
    private Integer customerId;
    private Integer version;

    public static StockNoteSourceDTO newInstance()
    {
        StockNoteSourceDTO stockNoteSourceDTO = new StockNoteSourceDTO();
        return stockNoteSourceDTO;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( final Integer id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( final String name )
    {
        this.name = name;
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
    public Integer getVersion()
    {
        return version;
    }

    public void setVersion( final Integer version )
    {
        this.version = version;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockNoteSourceDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", name='" ).append( name ).append( '\'' );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", version=" ).append( version );
        sb.append( '}' );
        return sb.toString();
    }
}
