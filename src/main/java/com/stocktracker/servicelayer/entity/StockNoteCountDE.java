package com.stocktracker.servicelayer.entity;

/**
 *
 */
public class StockNoteCountDE
{
    private int customerId;
    private String tickerSymbol;
    private int noteCount;

    public int getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( int customerId )
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

    public int getNoteCount()
    {
        return noteCount;
    }

    public void setNoteCount( int noteCount )
    {
        this.noteCount = noteCount;
    }

    @Override
    public String toString()
    {
        return "StockNoteCountDE{" +
               "customerId=" + customerId +
               ", tickerSymbol='" + tickerSymbol + '\'' +
               ", noteCount=" + noteCount +
               '}';
    }
}
