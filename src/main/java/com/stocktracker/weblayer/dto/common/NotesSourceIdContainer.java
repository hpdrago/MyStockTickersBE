package com.stocktracker.weblayer.dto.common;

/**
 * This interface contains the methods for DTO's that contains a notes source id -- string version of the notes source UUID.
 */
public interface NotesSourceIdContainer
{
    String getCustomerId();
    void setNotesSourceName( final String noteSourceName );
    void setNotesSourceId( final String notesSourceId );
    String getNotesSourceName();
    String getNotesSourceId();
}
