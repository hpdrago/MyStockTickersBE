package com.stocktracker.servicelayer.service;

public enum StockNoteActionTaken
{
    NONE( "NONE" ),
    BUY( "BUY" ),
    SELL( "SELL" );

    private String actionTaken;
    private StockNoteActionTaken( final String actionTaken )
    {
        this.actionTaken = actionTaken;
    }
}
