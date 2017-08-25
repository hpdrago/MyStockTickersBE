package com.stocktracker.weblayer.dto;

public class StockNoteCountDTO
{
    private int customerId;
    private String tickerSymbol;
    private int noteCount;

    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    public int getNoteCount()
    {
        return noteCount;
    }

    public void setNoteCount( int noteCount )
    {
        this.noteCount = noteCount;
    }

    public int getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( int customerId )
    {
        this.customerId = customerId;
    }
}
