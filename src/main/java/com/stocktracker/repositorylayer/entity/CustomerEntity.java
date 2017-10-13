package com.stocktracker.repositorylayer.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by mike on 9/2/2016.
 */
@Entity
@Table( name = "customer", schema = "stocktracker", catalog = "" )
public class CustomerEntity
{
    private int id;
    private String email;
    private String password;
    private Timestamp createDate;
    private Collection<PortfolioEntity> portfolios;
    private Collection<StockNoteEntity> stockNotes;

    public CustomerEntity()
    {
    }

    public static CustomerEntity newInstance()
    {
        return new CustomerEntity();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column( name = "email", nullable = false, length = 45 )
    public String getEmail()
    {
        return email;
    }

    public void setEmail( final String email )
    {
        this.email = email;
    }

    @Basic
    @Column( name = "password", nullable = false, length = 45 )
    public String getPassword()
    {
        return password;
    }

    public void setPassword( final String password )
    {
        this.password = password;
    }

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

    @OneToMany
    @JoinColumn( name = "customer_id", referencedColumnName = "id" )
    public Collection<PortfolioEntity> getPortfolios()
    {
        return portfolios;
    }

    public void setPortfolios( final Collection<PortfolioEntity> portfoliosById )
    {
        this.portfolios = portfoliosById;
    }

    @OneToMany
    @JoinColumn( name = "notes_source_id", referencedColumnName = "id" )
    public Collection<StockNoteEntity> getStockNotes()
    {
        return stockNotes;
    }

    public void setStockNotes( final Collection<StockNoteEntity> stockNotesById )
    {
        this.stockNotes = stockNotesById;
    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !(o instanceof CustomerEntity) )
        {
            return false;
        }
        final CustomerEntity that = (CustomerEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( id );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "CustomerEntity{" );
        sb.append( "id=" ).append( id );
        sb.append( ", email='" ).append( email ).append( '\'' );
        sb.append( ", portfoliosById=" ).append( portfolios );
        sb.append( '}' );
        return sb.toString();
    }
}
