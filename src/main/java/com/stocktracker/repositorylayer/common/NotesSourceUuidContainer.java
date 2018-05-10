package com.stocktracker.repositorylayer.common;

import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;

import java.util.Optional;

/**
 * Entities that contain a note source UUID implement this interface to enable automatic UUID conversion.
 */
public interface NotesSourceUuidContainer
{
    /**
     * Set the note source id.
     * @param noteSourceId
     */
    //void setNotesSourceUuid( final UUID noteSourceId );

    /**
     * Get the note source id.
     * @return
     */
    //UUID getNotesSourceUuid();

    void setNotesSourceEntity( final StockNoteSourceEntity stockNoteSourceEntity );
    Optional<StockNoteSourceEntity> getNotesSourceEntity();

    //Optional<UUID> getStockNoteSourceUuid();
}
