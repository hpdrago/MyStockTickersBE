package com.stocktracker.weblayer.dto;

import com.stocktracker.repositorylayer.entity.StockNoteStockEntity;

import java.math.BigDecimal;

public class StockNoteStockDTO
{
    private Integer stockNotesId;
    private String tickerSymbol;
    private Integer customerId;
    private BigDecimal stockPrice;

    public static StockNoteStockDTO newInstance( final StockNoteStockEntity stockNoteStockEntity )
    {
        StockNoteStockDTO stockNoteStockDTO = new StockNoteStockDTO();
        stockNoteStockDTO.setStockNotesId( stockNoteStockEntity.getStockNoteId() );
        stockNoteStockDTO.setTickerSymbol( stockNoteStockEntity.getId().getTickerSymbol() );
        stockNoteStockDTO.setCustomerId( stockNoteStockEntity.getCustomerId() );
        stockNoteStockDTO.setStockPrice( stockNoteStockEntity.getStockPrice() );
        return stockNoteStockDTO;
    }

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

    public BigDecimal getStockPrice()
    {
        return stockPrice;
    }

    public void setStockPrice( BigDecimal stockPrice )
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
