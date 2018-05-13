package com.stocktracker.common;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Utility class for working with trading hours.
 */
public class TradingHours
{
    final static Calendar estCalendar = Calendar.getInstance( TimeZone.getTimeZone( "EST") );

    /**
     * Determines if the market is currently in session.
     * @return
     */
    public static boolean isInSession()
    {
        /*
         * Return false for weekends.
         */
        int dayOfWeek = estCalendar.get(Calendar.DAY_OF_WEEK);
        if ( dayOfWeek == 0 || dayOfWeek == 7 )
        {
            return false;
        }
        int hour = getHour();
        int minute = estCalendar.get(Calendar.MINUTE);
        return (hour > 9 || (hour == 9 && minute >= 30)) && (hour < 16);
    }

    /**
     * Get the start time of the current day's session.
     * @return
     */
    public static long getSessionStartTime()
    {
        final Calendar myCalendar = Calendar.getInstance( TimeZone.getTimeZone( "EST") );
        myCalendar.set( Calendar.HOUR_OF_DAY, 9 );
        myCalendar.set( Calendar.MINUTE, 0 );
        return myCalendar.getTimeInMillis();
    }

    /**
     * Get the EST hour.
     * @return
     */
    public static int getHour()
    {
        return estCalendar.get( Calendar.HOUR_OF_DAY );
    }
}
