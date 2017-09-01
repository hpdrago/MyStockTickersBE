package com.stocktracker.common;

/**
 * This enum contains the three values for the bull or bear column of the stock note table.
 */
public enum BullOrBear
{
    NEUTRAL( (byte)0 ),
    BULL( (byte)1 ),
    BEAR( (byte)2 );

    private byte byteValue;
    BullOrBear( byte byteValue )
    {
        this.byteValue = byteValue;
    }

    public static BullOrBear valueOf( byte byteValue )
    {
        switch ( byteValue )
        {
            case 0: return NEUTRAL;
            case 1: return BULL;
            case 2: return BEAR;
            default:
                throw new IllegalArgumentException( byteValue + " must be 0,1 or 2" );
        }
    }
}
