package com.stocktracker.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * The dates/times that come from the web layer are strings in the format of yyyy-MM-dd'T'HH:mm:ss.SSS'Z'.
 * This class provides methods of converting from this string formation to a date object format.
 */
public class JSONDateConverter
{
    public static Timestamp toTimestamp( final String jsonUTCDate )
        throws ParseException
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyyMMdd" );
        dateFormat.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
        Date parsedDate = dateFormat.parse( jsonUTCDate );
        Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
        return timestamp;
    }

    public static String toString( final Timestamp utcTimestamp )
        throws ParseException
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        return dateFormat.format( utcTimestamp );
    }
}
