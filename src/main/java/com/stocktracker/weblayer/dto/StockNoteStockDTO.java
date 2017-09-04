package com.stocktracker.weblayer.dto;

public class StockNoteStockDTO
{
    private Integer stockNotesId;
    private Integer customerId;
    private String tickerSymbol;
    private Float stockPrice;

    public Integer getStockNotesId()
    {
        return stockNotesId;
    }

    public void setStockNotesId( Integer stockNotesId )
    {
        this.stockNotesId = stockNotesId;
    }

    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( Integer customerId )
    {
        this.customerId = customerId;
    }

    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    public Float getStockPrice()
    {
        return stockPrice;
    }

    public void setStockPrice( Float stockPrice )
    {
        this.stockPrice = stockPrice;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockNoteStockDTO{" );
        sb.append( "stockNotesId=" ).append( stockNotesId );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", stockPrice=" ).append( stockPrice );
        sb.append( '}' );
        return sb.toString();
    }
}
