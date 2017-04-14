package com.stocktracker.repositorylayer.common;

/**
 * Created by mike on 11/5/2016.
 */
public class BooleanUtils
{
    /**
     * Converts a database boolean ('Y','N') character to a boolean
     * @param charValue
     * @return
     */
    public static boolean fromCharToBoolean( final char charValue )
    {
        return Character.toUpperCase(charValue) == 'Y';
    }

    /**
     * Converts a boolean value into a character boolean ('Y','N')
     * @param booleanValue
     * @return
     */
    public static char fromBooleanToChar( final boolean booleanValue )
    {
        return booleanValue ? 'Y' : 'N';
    }
}
