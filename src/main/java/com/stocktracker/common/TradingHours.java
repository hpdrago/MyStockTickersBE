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
        if ( isWeekend() )
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
        final Calendar estCalendar = getESTCalendar();
        estCalendar.set( Calendar.HOUR_OF_DAY, 9 );
        estCalendar.set( Calendar.MINUTE, 30 );
        return estCalendar.getTimeInMillis();
    }

    /**
     * Session ends at 4:00pm EST.
     * @return
     */
    public static long getTodaysSessionEndTime()
    {
        final Calendar estCalendar = getESTCalendar();
        estCalendar.set( Calendar.HOUR_OF_DAY, 16 );
        estCalendar.set( Calendar.MINUTE, 00 );
        return estCalendar.getTimeInMillis();
    }

    /**
     * Get the start time of the next trading day's session.
     * @return
     */
    public static long getNextDaysSessionStartTime()
    {
        final Calendar estCalendar = getESTCalendar();
        estCalendar.set( Calendar.HOUR_OF_DAY, 9 );
        estCalendar.set( Calendar.MINUTE, 30 );
        switch ( estCalendar.get( Calendar.DAY_OF_WEEK ) )
        {
            case Calendar.FRIDAY:
                estCalendar.add( Calendar.DATE, 3 );
                break;
            case Calendar.SATURDAY:
                estCalendar.add( Calendar.DATE, 2 );
                break;
            case Calendar.SUNDAY:
                estCalendar.add( Calendar.DATE, 1 );
                break;
            default:
                estCalendar.add( Calendar.DATE, 1 );
                break;
        }
        return estCalendar.getTimeInMillis();
    }

    /**
     * Determines if {@code time} is after the end of today's session and before the next days session.
     * @param time
     * @return
     */
    public static boolean isAfterSessionEnd( final long time )
    {
        final Calendar estCalendar = getESTCalendar();
        return time > getTodaysSessionEndTime() &&
               time < getNextDaysSessionStartTime();
    }

    /**
     * Get the EST Timezone calendar
     * @return
     */
    private static Calendar getESTCalendar()
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

    /**
     * Returns true if it is currently the weekend (Friday after 4:00pm EST, SAT, SUN
     * @return
     */
    public static boolean isWeekend()
    {
        final Calendar estCalendar = getESTCalendar();
        return estCalendar.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY ||
               estCalendar.get( Calendar.DAY_OF_WEEK ) == Calendar.SATURDAY;
    }
}
