package com.stocktracker.common;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utility class for working with trading hours.
 */
public class TradingHours
{
    final static Calendar estCalendar = Calendar.getInstance( TimeZone.getTimeZone( "EST") );
    final static TimeZone estTimeZone = TimeZone.getTimeZone( "EST");
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
    public static long getTodaysSessionStartTime()
    {
        final Calendar myCalendar = getCalendar();
        myCalendar.set( Calendar.HOUR_OF_DAY, 9 );
        myCalendar.set( Calendar.MINUTE, 30 );
        return myCalendar.getTimeInMillis();
    }

    /**
     * Session ends at 4:00pm EST.
     * @return
     */
    public static long getTodaysSessionEndTime()
    {
        final Calendar myCalendar = getCalendar();
        myCalendar.set( Calendar.HOUR_OF_DAY, 16 );
        myCalendar.set( Calendar.MINUTE, 00 );
        return myCalendar.getTimeInMillis();
    }

    /**
     * Get the start time of the current day's session.
     * @return
     */
    public static long getNextDaysSessionStartTime()
    {
        final Calendar myCalendar = getCalendar();
        myCalendar.add( Calendar.DATE, 1 ); // add one day.
        myCalendar.set( Calendar.HOUR_OF_DAY, 9 );
        myCalendar.set( Calendar.MINUTE, 30 );
        /*
         * If Saturday, move to Monday
         */
        if ( myCalendar.get( Calendar.DAY_OF_WEEK ) == 7 )
        {
            myCalendar.add( Calendar.DATE, 2 );
        }
        /*
         * If Sunday, move to Monday
         */
        else if ( myCalendar.get( Calendar.DAY_OF_WEEK ) == 6 )
        {
            myCalendar.add( Calendar.DATE, 1 );
        }
        return myCalendar.getTimeInMillis();
    }

    /**
     * Determines if {@code time} is after the end of today's session and before the next days session.
     * @param time
     * @return
     */
    public static boolean isAfterSessionEnd( final long time )
    {
        return time > getTodaysSessionEndTime() &&
               time < getNextDaysSessionStartTime();
    }

    private static Calendar getCalendar()
    {
        Calendar calendar = Calendar.getInstance( estTimeZone );
        calendar.setTime( new Date() );
        return calendar;
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
