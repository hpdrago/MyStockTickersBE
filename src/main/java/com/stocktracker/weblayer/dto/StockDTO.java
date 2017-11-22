package com.stocktracker.weblayer.dto;

import com.stocktracker.servicelayer.service.StockQuoteService;
import com.stocktracker.servicelayer.service.stockinformationprovider.StockTickerQuote;

/**
 * Created by mike on 9/11/2016.
 */
public class StockDTO extends StockTickerQuote implements StockQuoteService.StockQuoteContainer
{

    public static StockDTO newInstance()
    {
        StockDTO stockDTO = new StockDTO();
        return stockDTO;
    }

    private StockDTO()
    {
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockDTO{" );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
