package com.stocktracker.servicelayer.tradeit.apiresults;

import com.stocktracker.servicelayer.tradeit.types.TradeItPosition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * This class contains the TradeIt API results for the GetPositions call.
 * @see https://www.trade.it/documentation#GetPositions
 * @author michael.earl 1/17/2018
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class GetPositionsAPIResult extends TradeItAPIResult
{
    private String accountBaseCurrency;
    private int currentPage;
    private int totalPages;
    private TradeItPosition[] positions;

    /**
     * Sets the results.
     * @param getPositionsAPIResult
     */
    public void setResults( final GetPositionsAPIResult getPositionsAPIResult )
    {
        super.setResults( getPositionsAPIResult );
        this.accountBaseCurrency = getPositionsAPIResult.accountBaseCurrency;
        this.currentPage = getPositionsAPIResult.currentPage;
        this.totalPages = getPositionsAPIResult.totalPages;
        this.positions = getPositionsAPIResult.positions;
    }

    /**
     * Base currency of the account
     */
    public String getAccountBaseCurrency()
    {
        return accountBaseCurrency;
    }

    public void setAccountBaseCurrency( String accountBaseCurrency )
    {
        this.accountBaseCurrency = accountBaseCurrency;
    }

    /**
     * Indicates the page of a multi page result
     */
    public int getCurrentPage()
    {
        return currentPage;
    }

    public void setCurrentPage( int currentPage )
    {
        this.currentPage = currentPage;
    }

    /**
     * The total number of pages to retrieve all positions
     */
    public int getTotalPages()
    {
        return totalPages;
    }

    public void setTotalPages( int totalPages )
    {
        this.totalPages = totalPages;
    }

    /**
     * Array of position objects
     */
    public TradeItPosition[] getPositions()
    {
        return positions;
    }

    public void setPositions( TradeItPosition[] positions )
    {
        this.positions = positions;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "GetPositionsAPIResult{" );
        sb.append( "accountBaseCurrency='" ).append( accountBaseCurrency ).append( '\'' );
        sb.append( ", currentPage=" ).append( currentPage );
        sb.append( ", totalPages=" ).append( totalPages );
        sb.append( ", positions=" ).append( Arrays.toString( positions ) );
        sb.append( '}' );
        return sb.toString();
    }
}
