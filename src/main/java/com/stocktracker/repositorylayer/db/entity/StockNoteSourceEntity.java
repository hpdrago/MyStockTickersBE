package com.stocktracker.repositorylayer.db.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by mike on 5/7/2017.
 */
@Entity
@Table( name = "stock_note_source", schema = "stocktracker", catalog = "" )
public class StockNoteSourceEntity
{
    private Integer id;
    private String name;
    private Integer customerId;
    private Timestamp dateCreated;

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
    @Column( name = "name", nullable = false, length = 20 )
    public String getName()
    {
        return name;
    }

    public void setName( final String name )
    {
        this.name = name;
    }

    @Basic
    @Column( name = "customer_id", nullable = false )
    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( final Integer customerId )
    {
        this.customerId = customerId;
    }

    @Basic
    @Column( name = "date_created", nullable = false )
    public Timestamp getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated( final Timestamp dateCreated )
    {
        this.dateCreated = dateCreated;
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
        final StockNoteSourceEntity that = (StockNoteSourceEntity) o;
        return Objects.equals( id, that.id ) &&
               Objects.equals( name, that.name ) &&
               Objects.equals( customerId, that.customerId ) &&
               Objects.equals( dateCreated, that.dateCreated );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( id, name, customerId, dateCreated );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockNoteSourceEntity{" );
        sb.append( "id=" ).append( id );
        sb.append( ", name='" ).append( name ).append( '\'' );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", dateCreated=" ).append( dateCreated );
        sb.append( '}' );
        return sb.toString();
    }
}
