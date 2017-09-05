package com.stocktracker.repositorylayer.db.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table( name = "stock_note", schema = "stocktracker", catalog = "" )
public class StockNoteEntity
{
    private Integer id;
    private String notes;
    private Integer notesRating;
    private Timestamp notesDate;
    private Byte bullOrBear;
    private String publicInd;
    private Timestamp dateCreated;
    private Timestamp dateModified;

    @Id
    @Column( name = "id" )
    public Integer getId()
    {
        return id;
    }

    public void setId( final Integer id )
    {
        this.id = id;
    }

    @Basic
    @Column( name = "notes" )
    public String getNotes()
    {
        return notes;
    }

    public void setNotes( final String notes )
    {
        this.notes = notes;
    }

    @Basic
    @Column( name = "notes_rating" )
    public Integer getNotesRating()
    {
        return notesRating;
    }

    public void setNotesRating( final Integer notesRating )
    {
        this.notesRating = notesRating;
    }

    @Basic
    @Column( name = "notes_date" )
    public Timestamp getNotesDate()
    {
        return notesDate;
    }

    public void setNotesDate( final Timestamp notesDate )
    {
        this.notesDate = notesDate;
    }

    @Basic
    @Column( name = "bull_or_bear" )
    public Byte getBullOrBear()
    {
        return bullOrBear;
    }

    public void setBullOrBear( final Byte bullOrBear )
    {
        this.bullOrBear = bullOrBear;
    }

    @Basic
    @Column( name = "public_ind" )
    public String getPublicInd()
    {
        return publicInd;
    }

    public void setPublicInd( final String publicInd )
    {
        this.publicInd = publicInd;
    }

    @Basic
    @Column( name = "date_created" )
    public Timestamp getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated( final Timestamp dateCreated )
    {
        this.dateCreated = dateCreated;
    }

    @Basic
    @Column( name = "date_modified" )
    public Timestamp getDateModified()
    {
        return dateModified;
    }

    public void setDateModified( final Timestamp dateModified )
    {
        this.dateModified = dateModified;
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

        final StockNoteEntity that = (StockNoteEntity) o;

        if ( id != null
             ? !id.equals( that.id )
             : that.id != null )
        {
            return false;
        }
        if ( notes != null
             ? !notes.equals( that.notes )
             : that.notes != null )
        {
            return false;
        }
        if ( notesRating != null
             ? !notesRating.equals( that.notesRating )
             : that.notesRating != null )
        {
            return false;
        }
        if ( notesDate != null
             ? !notesDate.equals( that.notesDate )
             : that.notesDate != null )
        {
            return false;
        }
        if ( bullOrBear != null
             ? !bullOrBear.equals( that.bullOrBear )
             : that.bullOrBear != null )
        {
            return false;
        }
        if ( publicInd != null
             ? !publicInd.equals( that.publicInd )
             : that.publicInd != null )
        {
            return false;
        }
        if ( dateCreated != null
             ? !dateCreated.equals( that.dateCreated )
             : that.dateCreated != null )
        {
            return false;
        }
        if ( dateModified != null
             ? !dateModified.equals( that.dateModified )
             : that.dateModified != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null
                     ? id.hashCode()
                     : 0;
        result = 31 * result + (notes != null
                                ? notes.hashCode()
                                : 0);
        result = 31 * result + (notesRating != null
                                ? notesRating.hashCode()
                                : 0);
        result = 31 * result + (notesDate != null
                                ? notesDate.hashCode()
                                : 0);
        result = 31 * result + (bullOrBear != null
                                ? bullOrBear.hashCode()
                                : 0);
        result = 31 * result + (publicInd != null
                                ? publicInd.hashCode()
                                : 0);
        result = 31 * result + (dateCreated != null
                                ? dateCreated.hashCode()
                                : 0);
        result = 31 * result + (dateModified != null
                                ? dateModified.hashCode()
                                : 0);
        return result;
    }
}
