package com.stocktracker.weblayer.dto.common;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This is the base class for DTO's that are derived from database entities.  It contains the required fields in order
 * to perform updates and inserts into the database.
 */
public abstract class DatabaseEntityDTO<K extends Serializable> implements VersionedDTO<K>
{
    private K id;
    private Integer version;
    private Timestamp createDate;
    private Timestamp updateDate;

    @Override
    public K getId()
    {
        return this.id;
    }

    public void setId( final K id )
    {
        this.id = id;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion( final Integer version )
    {
        this.version = version;
    }

    public Timestamp getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate( final Timestamp createDate )
    {
        this.createDate = createDate;
    }

    public Timestamp getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate( final Timestamp updateDate )
    {
        this.updateDate = updateDate;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "DatabaseEntityDTO{" );
        sb.append( "version=" ).append( version );
        sb.append( ", createDate=" ).append( createDate );
        sb.append( ", updateDate=" ).append( updateDate );
        sb.append( '}' );
        return sb.toString();
    }
}
