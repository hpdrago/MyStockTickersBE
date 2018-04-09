package com.stocktracker.servicelayer.service.stocks;

/**
 * DTOs that implement this information will have the company properties set.
 */
public interface StockCompanyContainer
{
    String getTickerSymbol();
    void setTickerSymbol( final String tickerSymbol );
    String getCompanyName();
    void setCompanyName( final String companyName );
    //String getCompanyURL();
    //void setCompanyURL( final String quoteUrl );
    String getSector();
    void setSector( final String sector );
    String getIndustry();
    void setIndustry( final String industry );
}
