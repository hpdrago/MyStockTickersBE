package com.stocktracker.weblayer.dto.tradeit;

import com.stocktracker.servicelayer.tradeit.apiresults.KeepSessionAliveAPIResult;
import com.stocktracker.weblayer.dto.TradeItAccountDTO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The DTO returned when calling TradeIt to keep the session alive.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class KeepSessionAliveDTO extends KeepSessionAliveAPIResult<KeepSessionAliveDTO>
{
    private TradeItAccountDTO tradeItAccountDTO;

    public KeepSessionAliveDTO()
    {
    }

    public void copyResults( final KeepSessionAliveAPIResult keepSessionAliveAPIResult )
    {
        super.copyResults( keepSessionAliveAPIResult );
    }

    @Override
    public void setResults( final KeepSessionAliveDTO results )
    {
        super.setResults( results );
        this.tradeItAccountDTO = results.tradeItAccountDTO;
    }

    public TradeItAccountDTO getTradeItAccountDTO()
    {
        return tradeItAccountDTO;
    }

    public void setTradeItAccountDTO( final TradeItAccountDTO tradeItAccountDTO )
    {
        this.tradeItAccountDTO = tradeItAccountDTO;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "KeepSessionAliveDTO{" );
        sb.append( "tradeItAccountDTO=" ).append( tradeItAccountDTO );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
