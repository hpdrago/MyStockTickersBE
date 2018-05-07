package com.stocktracker.weblayer.dto;

import com.stocktracker.weblayer.dto.common.CustomerIdContainer;
import com.stocktracker.weblayer.dto.common.UuidDTO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This DTO contains the information for the source information of a stock note.
 * Created by mike on 9/10/2017.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockNoteSourceDTO implements UuidDTO,
                                           CustomerIdContainer
{
    private String id;
    private String name;
    private String customerId;
    private Integer version;

    public static StockNoteSourceDTO newInstance()
    {
        StockNoteSourceDTO stockNoteSourceDTO = new StockNoteSourceDTO();
        return stockNoteSourceDTO;
    }

    public String getId()
    {
        return id;
    }

    public void setId( final String id )
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

    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( final String customerId )
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
