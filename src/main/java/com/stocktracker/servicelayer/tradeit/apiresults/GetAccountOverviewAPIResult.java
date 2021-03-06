package com.stocktracker.servicelayer.tradeit.apiresults;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class is returned from TradeIt when getting the account overview information.
 *
 * @see: https://www.trade.it/documentation#BalanceService
 * @author michael.earl 1/17/2018
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class GetAccountOverviewAPIResult extends TradeItAPIResult
{
    private double availableCash;
    private double buyingPower;
    private double totalValue;
    private double dayAbsoluteReturn;
    private double dayPercentReturn;
    private double totalAbsoluteReturn;
    private double totalPercentReturn;
    private double marginCash;

    public GetAccountOverviewAPIResult()
    {
    }

    /**
     * Sets the results
     * @param getAccountOverviewAPIResult
     */
    public void setResults( final GetAccountOverviewAPIResult getAccountOverviewAPIResult )
    {
        super.setResults( getAccountOverviewAPIResult );
        this.availableCash = getAccountOverviewAPIResult.availableCash;
        this.buyingPower = getAccountOverviewAPIResult.buyingPower;
        this.totalValue = getAccountOverviewAPIResult.totalValue;
        this.dayAbsoluteReturn = getAccountOverviewAPIResult.dayAbsoluteReturn;
        this.dayPercentReturn = getAccountOverviewAPIResult.dayPercentReturn;
        this.totalAbsoluteReturn = getAccountOverviewAPIResult.totalAbsoluteReturn;
        this.totalPercentReturn = getAccountOverviewAPIResult.totalPercentReturn;
        this.marginCash = getAccountOverviewAPIResult.marginCash;
    }

    /**
     * Cash available to withdraw [+ / null]
     */
    public double getAvailableCash()
    {
        return availableCash;
    }

    public void setAvailableCash( double availableCash )
    {
        this.availableCash = availableCash;
    }

    /**
     * The buying power of the account [+ / null]
     */
    public double getBuyingPower()
    {
        return buyingPower;
    }

    public void setBuyingPower( double buyingPower )
    {
        this.buyingPower = buyingPower;
    }

    /**
     * The total account value [+ / null]
     */
    public double getTotalValue()
    {
        return totalValue;
    }

    public void setTotalValue( double totalValue )
    {
        this.totalValue = totalValue;
    }

    /**
     * The daily return of the account [+ / - / null]. If null this field must not be displayed to the user.
     */
    public double getDayAbsoluteReturn()
    {
        return dayAbsoluteReturn;
    }

    public void setDayAbsoluteReturn( double dayAbsoluteReturn )
    {
        this.dayAbsoluteReturn = dayAbsoluteReturn;
    }

    /**
     * The daily return percentage [+ / - / null]. If null this field must not be displayed to the user.
     */
    public double getDayPercentReturn()
    {
        return dayPercentReturn;
    }

    public void setDayPercentReturn( double dayPercentReturn )
    {
        this.dayPercentReturn = dayPercentReturn;
    }

    /**
     * The total absolute return on the account [+ / - / null]. If null this field must not be displayed to the user.
     */
    public double getTotalAbsoluteReturn()
    {
        return totalAbsoluteReturn;
    }

    public void setTotalAbsoluteReturn( double totalAbsoluteReturn )
    {
        this.totalAbsoluteReturn = totalAbsoluteReturn;
    }

    /**
     * The total percentage return on the account [+ / - / null]. If null this field must not be displayed to the user.
     */
    public double getTotalPercentReturn()
    {
        return totalPercentReturn;
    }

    public void setTotalPercentReturn( double totalPercentReturn )
    {
        this.totalPercentReturn = totalPercentReturn;
    }

    /**
     * The margin cash balance of the account [+ / - / null]
     */
    public double getMarginCash()
    {
        return marginCash;
    }

    public void setMarginCash( double marginCash )
    {
        this.marginCash = marginCash;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "GetAccountOverViewAPIResult{" );
        sb.append( "availableCash=" ).append( availableCash );
        sb.append( ", buyingPower=" ).append( buyingPower );
        sb.append( ", totalValue=" ).append( totalValue );
        sb.append( ", dayAbsoluteReturn=" ).append( dayAbsoluteReturn );
        sb.append( ", dayPercentReturn=" ).append( dayPercentReturn );
        sb.append( ", totalAbsoluteReturn=" ).append( totalAbsoluteReturn );
        sb.append( ", totalPercentReturn=" ).append( totalPercentReturn );
        sb.append( ", marginCash=" ).append( marginCash );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
