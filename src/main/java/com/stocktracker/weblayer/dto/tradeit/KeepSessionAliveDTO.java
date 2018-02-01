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
public class KeepSessionAliveDTO extends KeepSessionAliveAPIResult
{
    private TradeItAccountDTO tradeItAccount;

    public KeepSessionAliveDTO()
    {
    }

    public void setResults( final KeepSessionAliveDTO results )
    {
        super.setResults( results );
        this.tradeItAccount = results.tradeItAccount;
    }

    public TradeItAccountDTO getTradeItAccount()
    {
        return tradeItAccount;
    }

    public void setTradeItAccount( final TradeItAccountDTO tradeItAccount )
    {
        this.tradeItAccount = tradeItAccount;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "KeepSessionAliveDTO{" );
        sb.append( "tradeItAccount=" ).append( tradeItAccount );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
