package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * This is the base class for all Service Classes.
 */
public abstract class BaseService implements MyLogger
{
    @Autowired
    protected ApplicationContext context;

    //private ServiceLogger serviceLogger = new ServiceLogger();

    /**
     * Local logger class needed because of Spring Proxy
     */
    private class ServiceLogger implements MyLogger
    {
        @Override
        public Logger getLogger()
        {
            return LoggerFactory.getLogger( BaseService.this.getClass() );
        }
    }

    /*
    protected void logMethodBegin( final String methodName, final Object ... args )
    {
        this.serviceLogger.logMethodBegin( methodName, args );
    }

    protected void logMethodEnd( final String methodName, final Object returnValue )
    {
        this.serviceLogger.logMethodEnd( methodName, returnValue );
    }

    protected void logMethodEnd( final String methodName )
    {
        this.serviceLogger.logMethodEnd( methodName );
    }

    protected void logDebug( final String methodName, final String logMessage )
    {
        this.serviceLogger.logDebug( methodName, logMessage );
    }

    protected void logDebug( final String methodName, final String messageFormat, final Object ... args )
    {
        this.serviceLogger.logDebug( methodName, messageFormat, args );
    }

    protected void logError( final String methodName, final Throwable throwable )
    {
        this.serviceLogger.logError( methodName, throwable );
    }

    protected void logError( final String methodName, final String logMessage )
    {
        this.serviceLogger.logError( methodName, logMessage );
    }

    protected void logWarn( final String methodName, final String logMessage )
    {
        this.serviceLogger.logWarn( methodName, logMessage );
    }

    protected void logWarn( final String methodName, final String messageFormat, final Object ... args )
    {
        this.serviceLogger.logWarn( methodName, messageFormat, args );
    }

    protected void logInfo( final String methodName, final String logMessage )
    {
        this.serviceLogger.logInfo( methodName, logMessage );
    }

    protected void logInfo( final String methodName, final String messageFormat, final Object ... args )
    {
        this.serviceLogger.logInfo( methodName, messageFormat, args );
    }

    protected void logError( final String methodName, final String messageFormat, final Object ... args )
    {
        this.serviceLogger.logError( methodName, messageFormat, args );
    }
    */
}
