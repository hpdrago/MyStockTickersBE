package com.stocktracker.weblayer.dto;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockCompanyDTO extends BaseStockCompanyDTO implements VersionedDTO<String>
{
    private Integer version;

    @Override
    public String getId()
    {
        return this.getTickerSymbol();
    }

    @Override
    public Integer getVersion()
    {
        return version;
    }

    @Override
    public void setVersion( final Integer version )
    {
        this.version = version;
    }
}
