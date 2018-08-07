package com.stocktracker.repositorylayer.entity;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.common.VersionedEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@MappedSuperclass
public abstract class BaseEntity<K extends Serializable> implements VersionedEntity<K>,
                                                                    MyLogger
{
    private Integer version;
    private Timestamp createDate;
    private Timestamp updateDate;

    @Transient
    public abstract K getId();

    @Basic
    @Column( name = "create_date", nullable = false )
    public Timestamp getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate( final Timestamp createDate )
    {
        this.createDate = createDate;
    }

    @Basic
    @Column( name = "update_date", nullable = true )
    public Timestamp getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate( final Timestamp updateDate )
    {
        this.updateDate = updateDate;
    }

    @Version
    @Override
    @Column( name = "version", nullable = true )
    public Integer getVersion()
    {
        return version;
    }

    public void setVersion( final Integer version )
    {
        this.version = version;
    }

    @PrePersist
    void preInsert()
    {
        if ( this.createDate == null )
        {
            logDebug( "preInsert", "id: {0}", this.getId() );
            this.createDate = new Timestamp( System.currentTimeMillis() );
            this.updateDate = this.createDate;
        }
        this.version = 1;
    }

    @PreUpdate
    void preUpdate()
    {
        logDebug( "preUpdate", "id: {0}", this.getId() );
        Objects.requireNonNull( this.getId(), "entity id is null, cannot save entity." );
        this.updateDate = new Timestamp( System.currentTimeMillis() );
    }

    /**
     * Copies the base values from one entity to this instance.
     * @param baseEntity
     */
    public void copyVitalEntityValues( final BaseEntity<K> baseEntity )
    {
        this.createDate = baseEntity.createDate;
        this.updateDate = baseEntity.updateDate;
        this.version = baseEntity.version;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "BaseEntity{" );
        sb.append( "version=" ).append( version );
        sb.append( ", createDate=" ).append( createDate );
        sb.append( ", updateDate=" ).append( updateDate );
        sb.append( '}' );
        return sb.toString();
    }
}
