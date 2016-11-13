package com.stocktracker.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.MessageFormat;

/**
 * This method defines a set of logging methods to produce more consistent and valuable logging messages.  All methods
 * require the name of the method that is creating the log message.  The hashCode of the object creating the log message
 * is included by default and the format of the output is:
 *
 * [hashCode] - [method name] [log message]
 *
 * If you do not wish to include the hashCode for a particular class then override the {@code addHashCodeToLogMessage} to
 * return false
 *
 * To use this interface all you need to do is implement it. The interface provides default methods to create a Logger
 * for the object instance.  You may override the {@code getLogger} method to provide a different Logger instance than
 * the default object instance Logger
 *
 * This interface provides most of the common methods as {@code Log4J.Logger} and these method calls are forwarded to the
 * Logger instance returned by the {@codegetLogger} method.  However, calling these methods is slightly different since
 * you don't need access to the Logger instance.  The methods are as follows:
 *
 * logInfo
 * logWarn
 * logError
 * logTrace
 * logDebug
 *
 * Two additional methods are also default: {@code logMethodBegin}
 * and {@code logExisting}.  These methods should be used as the first and last (respectively) logging calls for methods
 * that you wish to have entering and existing log messages.  The format of these logging messages are:
 *
 * [hashCode] [method name].begin [message]
 * [hashCode] [method name].end [message]
 *
 * If you pass the list of arguments to the logMethodBegin method, these will also be including after the hashCode in the
 * form of
 *
 * [hashCode] [method name].begin arg1: [arg1.toString()] arg2: [toString()]
 *
 * If you pass a second value to logMethodEnd the output will be in the form of
 *
 * [hashCode] [method name].end return Value: [returnValue.toString()]
 *
 * When using the logging methods in this class, it is highly encouraged that you create a:
 *
 * {@code final String methodName = "foo";}
 *
 * variable at the top of the method and use the {@code methodName} variable in all subsequent logging calls so that the
 * logging messages are guaranteed to contain the same name and this will also ease the burden when refactoring code to
 * a new * method and thus you only need to change the name of the methodName in a single location and not all logging
 * calls.
 *
 * The three argument {@code (String methodName, String message, Object ... args)} version of {@code logError},
 * {@code logWarn}, {@code logInfo}, {@code logDebug}, {@code logTrace}, assume that {@code message} and {@code args}
 * are intended to be used as arguments to {@code MessageFormat} http://docs.oracle.com/javase/1.5.0/docs/api/java/text/MessageFormat.html
 * which provides nice formatting capabilities which allow the argument values to be copied into the the message similar
 * to String.format.
 *
 * *************************************************************
 * I M P O R T A N T
 * *************************************************************
 * Subclasses of parent classes that implement this interface should override the {@code getLogger} method so that
 * the logging messages will correct come from the subclass and not the parent class.  Failure to do so will result
 * in all log messages appear to come from the parent class
 *
 * Created by mearl on 2/29/2016.
 */
public interface MyLogger
{
    /**
     * Get the Logger instance.  This method will get the Log4J logger for the class instance.  override this
     * method to use a different logger
     * @return
     */
    default Log getLogger()
    {
        return LogFactory.getLog( getClass() );
    }

    /**
     * By default, all log messages will be created in the form of "[hashCode] [log message]".
     * The presence of the hashCode of the class logging the method is often time valuable information to differentiate
     * one log message from another
     * Override this method to return false to exclude the instance hashCode from the log message
     * @return
     */
    default boolean addHashCodeToLogMessage()
    {
        return false;
    }

    /**
     * Log the entering of a method in the form of
     * [methodName].begin
     *
     * @param methodName
     */
    default void logMethodBegin( final String methodName )
    {
        final Object args = null;
        if ( isDebugEnabled() )
        {
            final StringBuilder sb = new StringBuilder();
            if ( addHashCodeToLogMessage() )
            {
                sb.append( hashCode() );
                sb.append( " " );
            }
            sb.append( methodName );
            sb.append( ".begin " );
            getLogger().debug( sb.toString() );
        }
    }

    /**
     * Log the beginning of a method in the form of:
     * [methodName].begin arg1: [arg1.toString()] arg2: [arg2.toString()]
     *
     * @param methodName the name of the method creating the log message
     * @param args method arguments
     */
    default void logMethodBegin( final String methodName, final Object... args )
    {
        if ( isDebugEnabled() )
        {
            final StringBuilder sb = new StringBuilder();
            if ( addHashCodeToLogMessage() )
            {
                sb.append( hashCode() );
                sb.append( " " );
            }
            sb.append( methodName );
            sb.append( ".begin " );
            appendArgs( sb, args );
            getLogger().debug( sb.toString() );
        }
    }

    /**
     * Internal method
     * @param sb
     * @param args
     */
    default void appendArgs( final StringBuilder sb, final Object... args )
    {
        if ( args != null )
        {
            for ( int i = 0; i < args.length; i++ )
            {
                sb.append( String.format( "arg%d: %s ", i, args[i] ) );
            }
        }
    }

    /**
     * Log the end of a method in the form of
     * [methodName].end
     *
     * @param methodName the name of the method creating the log message
     */
    default void logMethodEnd( final String methodName )
    {
        if ( isDebugEnabled() )
        {
            final StringBuilder sb = new StringBuilder();
            if ( addHashCodeToLogMessage() )
            {
                sb.append( hashCode() );
                sb.append( " " );
            }
            sb.append( methodName );
            sb.append( ".end " );
            getLogger().debug( sb.toString() );
        }
    }

    /**
     * Log the end of a method including a return value in the form of
     * [methodName].end return value: [returnValue.toString]
     *
     * @param methodName the name of the method creating the log message
     * @param returnValue the method return value
     */
    default void logMethodEnd( final String methodName, final Object returnValue )
    {
        if ( isDebugEnabled() )
        {
            final StringBuilder sb = new StringBuilder();
            if ( addHashCodeToLogMessage() )
            {
                sb.append( hashCode() );
                sb.append( " " );
            }
            sb.append( methodName );
            sb.append( ".end return value: " + returnValue );
            getLogger().debug( sb.toString() );
        }
    }

    /**
     * Log a trace message with the hashcode as part of the message
     *
     * @param methodName the name of the method creating the log message
     * @param logMessage
     */
    default void logTrace( final String methodName, final String logMessage )
    {
        if ( isTraceEnabled() )
        {
            getLogger().trace( getLogMessage( methodName, logMessage ) );
        }
    }

    /**
     * Log a trace message with the hashcode as part of the message
     *
     * @param methodName the name of the method creating the log message
     * @param messageFormat a {@code MessageFormat} string format
     * @param args arguments to be used in the messageFormat by {@code MessageFormat}
     */
    default void logTrace( final String methodName, final String messageFormat, final Object... args )
    {
        if ( isTraceEnabled() )
        {
            getLogger().trace( getLogMessage( methodName, messageFormat, args ) );
        }
    }

    /**
     * Log a trace message with the hashcode as part of the message
     *
     * @param methodName the name of the method creating the log message
     * @param logMessage the log message
     * @param throwable exception to log
     */
    default void logTrace( final String methodName, final String logMessage, final Throwable throwable )
    {
        if ( isTraceEnabled() )
        {
            getLogger().trace( getLogMessage( methodName, logMessage ), throwable );
        }
    }

    /**
     * Log a trace message with the hashcode as part of the message
     *
     * @param methodName the name of the method creating the log message
     * @param messageFormat a {@code MessageFormat} string format
     * @param throwable exception to log
     * @param args arguments to be used in the messageFormat by {@code MessageFormat}
     */
    default void logTrace( final String methodName, final String messageFormat, final Throwable throwable,
                           final Object... args )
    {
        if ( isTraceEnabled() )
        {
            getLogger().trace( getLogMessage( methodName, messageFormat ), throwable );
        }
    }

    /**
     * Log a debug message with the hashcode as part of the message
     *
     * @param methodName the name of the method creating the log message
     * @param logMessage the message to log
     */
    default void logDebug( final String methodName, final String logMessage )
    {
        if ( isDebugEnabled() )
        {
            getLogger().debug( getLogMessage( methodName, logMessage ) );
        }
    }

    /**
     * Log a debug message with the hashcode as part of the message
     *
     * @param methodName the name of the method creating the log message
     * @param messageFormat a {@code MessageFormat} string format
     * @param args arguments to be used in the messageFormat by {@code MessageFormat}
     */
    default void logDebug( final String methodName, final String messageFormat, final Object... args )
    {
        if ( isDebugEnabled() )
        {
            getLogger().debug( getLogMessage( methodName, messageFormat, args ) );
        }
    }

    /**
     * Log a debug message with the hashcode as part of the message
     *
     * @param methodName the name of the method creating the log message
     * @param e exception to log stack trace for
     */
    default void logDebug( final String methodName, final Throwable e )
    {
        logDebug( methodName, null, e );
    }

    /**
     * Log a debug message with the hashcode as part of the message
     *
     * @param methodName the name of the method creating the log message
     * @param logMessage the message to log
     * @param throwable exception to log
     */
    default void logDebug( final String methodName, final String logMessage, final Throwable throwable )
    {
        if ( isDebugEnabled() )
        {
            getLogger().debug( getLogMessage( methodName, logMessage ), throwable );
        }
    }

    /**
     * Log a info message with the hashcode as part of the message
     *
     * @param methodName the name of the method creating the log message
     * @param logMessage the message to log
     */
    default void logInfo( final String methodName, final String logMessage )
    {
        getLogger().info( getLogMessage( methodName, logMessage ) );
    }

    /**
     * Log a info message with the hashcode as part of the message
     *
     * @param methodName the name of the method creating the log message
     * @param messageFormat a {@code MessageFormat} string format
     * @param args arguments to be used in the messageFormat by {@code MessageFormat}
     */
    default void logInfo( final String methodName, final String messageFormat, final Object... args )
    {
        getLogger().info( getLogMessage( methodName, messageFormat, args ) );
    }

    /**
     * Log a info message with the hashcode as part of the message
     *
     * @param methodName the name of the method creating the log message
     * @param logMessage the message to log
     * @param throwable exception to log
     */
    default void logInfo( final String methodName, final String logMessage, final Throwable throwable )
    {
        getLogger().info( getLogMessage( methodName, logMessage ), throwable );
    }

    /**
     * Log an error message with the hashcode as part of the message
     *
     * @param methodName the name of the method creating the log message
     * @param logMessage the message to log
     */
    default void logError( final String methodName, final String logMessage )
    {
        getLogger().error( getLogMessage( methodName, logMessage ) );
    }

    /**
     * Log an error message with the hashcode as part of the message
     *
     * @param methodName the name of the method creating the log message
     * @param messageFormat a {@code MessageFormat} string format
     * @param args arguments to be used in the messageFormat by {@code MessageFormat}
     */
    default void logError( final String methodName, final String messageFormat, final Object... args )
    {
        getLogger().error( getLogMessage( methodName, messageFormat, args ) );
    }

    /**
     * Log an error message with the hashcode as part of the message
     *
     * @param methodName the name of the method creating the log message
     * @param throwable exception to log
     */
    default void logError( final String methodName, final Throwable throwable )
    {
        getLogger().error( getLogMessage( methodName, "Exception: " ), throwable );
    }
    /**
     * Log an error message with the hashcode as part of the message
     *
     * @param methodName the name of the method creating the log message
     * @param logMessage the message to log
     * @param throwable exception to log
     */
    default void logError( final String methodName, final String logMessage, final Throwable throwable )
    {
        getLogger().error( getLogMessage( methodName, logMessage ), throwable );
    }

    /**
     * Log a warning message with the hashcode as part of the message
     *
     * @param methodName the name of the method creating the log message
     * @param logMessage the message to log
     */
    default void logWarn( final String methodName, final String logMessage )
    {
        getLogger().warn( getLogMessage( methodName, logMessage ) );
    }

    /**
     * Log a warning message with the hashcode as part of the message
     *
     * @param methodName the name of the method creating the log message
     * @param messageFormat a {@code MessageFormat} string format
     * @param args arguments to be used in the messageFormat by {@code MessageFormat}
     */
    default void logWarn( final String methodName, final String messageFormat, final Object args )
    {
        getLogger().warn( getLogMessage( methodName, messageFormat, args ) );
    }
    /**
     * Log a warning message with the hashcode as part of the message
     *
     * @param methodName the name of the method creating the log message
     * @param logMessage the message to log
     */
    default void logWarn( final String methodName, final String logMessage, final Throwable throwable )
    {
        getLogger().warn( getLogMessage( methodName, logMessage ), throwable );
    }

    /**
     * Returns true if trace level logging is enabled
     * @return
     */
    default boolean isTraceEnabled()
    {
        return getLogger().isTraceEnabled();
    }

    /**
     * Returns true if trace level logging is enabled
     * @return
     */
    default boolean isDebugEnabled()
    {
        return getLogger().isDebugEnabled();
    }

    /**
     * This method will add the instance hashCode to the log message in the form of "[methodName] [hashCode] [message]"
     * If addHashCodeToLogMessage returns true, the hashCode will be added, this is the default behavior
     *
     * @param methodName name of the method calling the log method
     * @param message message to be logged
     * @return a String formatted "[methodName] [hashCode] [message]" or without the [hashCode] value
     */
    default String getLogMessage( final String methodName, final String message )
    {
        final StringBuilder sb = new StringBuilder();
        if ( addHashCodeToLogMessage() )
        {
            if ( message == null )
            {
                sb.append( String.format( "%d %s", hashCode(), methodName ) );
            }
            else
            {
                sb.append( String.format( "%d %s %s", hashCode(), methodName, message ) );
            }
        }
        else
        {
            if ( message == null )
            {
                sb.append( String.format( "%s", methodName ) );
            }
            else
            {
                sb.append( String.format( "%s %s", methodName, message ) );
            }
        }
        return sb.toString();
    }

    /**
     * This method formats the log message similar to the version without the {@code args} arguments, except that
     * {@code messageFormat} and {@code args} are assumed to be arguments to {@code java.text.MessageFormat} which
     * behaves similar to String.format.  See reference here for details:
     * http://docs.oracle.com/javase/1.5.0/docs/api/java/text/MessageFormat.html
     *
     * @param methodName name of the method calling the log method
     * @param messageFormat message to be logged
     * @return a String formatted "[hashCode] [methodName] [message]" or without the [hashCode] value
     */
    default String getLogMessage( final String methodName, final String messageFormat, Object... args )
    {
        final StringBuilder sb = new StringBuilder();
        if ( addHashCodeToLogMessage() )
        {
            sb.append( String.format( "%d %s %s", hashCode(), methodName, MessageFormat.format( messageFormat, args ) ));
        }
        else
        {
            String message = MessageFormat.format( messageFormat, args );
            sb.append( String.format( "%s %s", methodName, message ));
        }
        return sb.toString();
    }
}
