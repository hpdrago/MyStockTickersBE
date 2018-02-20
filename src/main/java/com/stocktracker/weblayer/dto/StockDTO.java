package com.stocktracker.weblayer.dto;

import com.stocktracker.servicelayer.service.StockQuoteService;
import com.stocktracker.servicelayer.stockinformationprovider.StockTickerQuote;

import java.math.BigDecimal;

/**
 * Created by mike on 9/11/2016.
 */
public class StockDTO extends StockTickerQuote implements StockQuoteService.StockQuoteContainer,
                                                          VersionedDTO<String>
{
    private Integer version;

    public static StockDTO newInstance()
    {
        StockDTO stockDTO = new StockDTO();
        return stockDTO;
    }

    private StockDTO()
    {
    }

    @Override
    public String getId()
    {
        return super.getTickerSymbol();
    }

    @Override
    public Integer getVersion()
    {
        return version;
    }

    public void setVersion( final Integer version )
    {
        this.version = version;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockDTO{" );
        sb.append( "version=" ).append( version );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }

    @Override
    public BigDecimal getAvgAnalystPriceTarget()
    {
        return null;
    }

    @Override
    public void setAvgAnalystPriceTarget( final BigDecimal avgAnalystPriceTarget )
    {

    }

    @Override
    public Integer getCustomerId()
    {
        return null;
    }
}
