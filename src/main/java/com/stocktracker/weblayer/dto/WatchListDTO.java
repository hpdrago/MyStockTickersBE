package com.stocktracker.weblayer.dto;

import com.stocktracker.weblayer.dto.common.DatabaseEntityDTO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by mike on 5/7/2017.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class WatchListDTO extends DatabaseEntityDTO<UUID>
{
    private String name;

    public String getName()
    {
        return name;
    }

    public void setName( final String name )
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "WatchListDTO{" );
        sb.append( "name='" ).append( name ).append( '\'' );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
