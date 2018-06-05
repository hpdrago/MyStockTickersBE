package com.stocktracker.weblayer.dto;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Generic DTO that contains a string value.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StringDTO
{
    private String value;

    public String getValue()
    {
        return value;
    }

    public void setValue( final String value )
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StringDTO{" );
        sb.append( "value='" ).append( value ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
