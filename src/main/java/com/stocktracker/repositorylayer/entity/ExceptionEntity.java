package com.stocktracker.repositorylayer.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Created by mike on 5/7/2017.
 */
@Entity
@Table( name = "exception", schema = "stocktracker", catalog = "" )
public class ExceptionEntity
{
    private Integer id;
    private String className;
    private String methodName;
    private String arguments;
    private String stackTrace;
    private Timestamp datetime;

    @Id
    @Column( name = "id", nullable = false )
    public Integer getId()
    {
        return id;
    }

    public void setId( final Integer id )
    {
        this.id = id;
    }

    @Basic
    @Column( name = "class_name", nullable = false, length = 45 )
    public String getClassName()
    {
        return className;
    }

    public void setClassName( final String className )
    {
        this.className = className;
    }

    @Basic
    @Column( name = "method_name", nullable = false, length = 45 )
    public String getMethodName()
    {
        return methodName;
    }

    public void setMethodName( final String methodName )
    {
        this.methodName = methodName;
    }

    @Basic
    @Column( name = "arguments", nullable = false, length = 255 )
    public String getArguments()
    {
        return arguments;
    }

    public void setArguments( final String arguments )
    {
        this.arguments = arguments;
    }

    @Basic
    @Column( name = "stack_trace", nullable = false, length = 4096 )
    public String getStackTrace()
    {
        return stackTrace;
    }

    public void setStackTrace( final String stackTrace )
    {
        this.stackTrace = stackTrace;
    }

    @Basic
    @Column( name = "datetime", nullable = false )
    public Timestamp getDatetime()
    {
        return datetime;
    }

    public void setDatetime( final Timestamp datetime )
    {
        this.datetime = datetime;
    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        final ExceptionEntity that = (ExceptionEntity) o;
        return Objects.equals( id, that.id ) &&
               Objects.equals( className, that.className ) &&
               Objects.equals( methodName, that.methodName ) &&
               Objects.equals( arguments, that.arguments ) &&
               Objects.equals( stackTrace, that.stackTrace ) &&
               Objects.equals( datetime, that.datetime );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( id, className, methodName, arguments, stackTrace, datetime );
    }
}
