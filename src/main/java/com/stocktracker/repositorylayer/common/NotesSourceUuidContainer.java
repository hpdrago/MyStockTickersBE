package com.stocktracker.repositorylayer.common;

import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;

import java.util.Optional;

/**
 * Entities that contain a note source UUID implement this interface to enable automatic UUID conversion.
 */
public interface NotesSourceUuidContainer
{
    /**
     * Sets the child notes source entity.
     * @param stockNoteSourceEntity
     */
    void setNotesSourceEntity( final StockNoteSourceEntity stockNoteSourceEntity );

    /**
     * Gets the notes source entity.
     * @return
     */
    Optional<StockNoteSourceEntity> getNotesSourceEntity();
}
