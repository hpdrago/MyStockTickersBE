package com.stocktracker.weblayer.dto;

import com.stocktracker.weblayer.dto.common.CustomerIdContainer;
import com.stocktracker.weblayer.dto.common.DatabaseEntityDTO;
import com.stocktracker.weblayer.dto.common.UuidDTO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class GainsLossesDTO extends DatabaseEntityDTO<String>
                            implements UuidDTO,
                                       CustomerIdContainer
{
    private String tickerSymbol;
    private String customerId;
    private String linkedAccountId;
    private BigDecimal gains;
    private BigDecimal losses;
    private BigDecimal totalGainsLosses;

    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    public String getLinkedAccountId()
    {
        return linkedAccountId;
    }

    public void setLinkedAccountId( final String linkedAccountId )
    {
        this.linkedAccountId = linkedAccountId;
    }

    public BigDecimal getGains()
    {
        return gains;
    }

    public void setGains( final BigDecimal gains )
    {
        this.gains = gains;
    }

    public BigDecimal getLosses()
    {
        return losses;
    }

    public void setLosses( final BigDecimal losses )
    {
        this.losses = losses;
    }

    public BigDecimal getTotalGainsLosses()
    {
        return totalGainsLosses;
    }

    public void setTotalGainsLosses( final BigDecimal totalGainsLosses )
    {
        this.totalGainsLosses = totalGainsLosses;
    }

    @Override
    public void setCustomerId( final String customerId )
    {
        this.customerId = customerId;
    }

    @Override
    public String getCustomerId()
    {
        return this.customerId;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "GainsLossesDTO{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", linkedAccountId=" ).append( linkedAccountId );
        sb.append( ", gains=" ).append( gains );
        sb.append( ", losses=" ).append( losses );
        sb.append( ", totalGainsLosses=" ).append( totalGainsLosses );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
