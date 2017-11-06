package com.stocktracker.weblayer.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stocktracker.common.JSONMoneySerializer;
import com.stocktracker.servicelayer.service.StockService;
import com.stocktracker.servicelayer.service.stockinformationprovider.StockQuoteState;
import com.stocktracker.servicelayer.service.stockinformationprovider.StockTickerQuote;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by mike on 9/11/2016.
 */
public class StockDTO extends StockTickerQuote implements StockService.StockQuoteContainer
{
    private String exchange;
    private int createdBy;
    private boolean userEntered;

    public static StockDTO newInstance()
    {
        StockDTO stockDTO = new StockDTO();
        return stockDTO;
    }

    private StockDTO()
    {
    }

    public String getExchange()
    {
        return exchange;
    }

    public void setExchange( final String exchange )
    {
        this.exchange = exchange;
    }

    public int getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy( int createdBy )
    {
        this.createdBy = createdBy;
    }

    public boolean isUserEntered()
    {
        return userEntered;
    }

    public void setUserEntered( boolean userEntered )
    {
        this.userEntered = userEntered;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockDTO{" );
        sb.append( "exchange='" ).append( exchange ).append( '\'' );
        sb.append( ", createdBy=" ).append( createdBy );
        sb.append( ", userEntered=" ).append( userEntered );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
