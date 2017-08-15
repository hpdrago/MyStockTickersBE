package com.stocktracker.servicelayer.entity;

import com.stocktracker.repositorylayer.db.entity.StockNoteSourceEntity;
import org.springframework.beans.BeanUtils;

/**
 * Created by mike on 5/13/2017.
 */
public class StockNoteSourceDE
{
    private String noteSource;

    public String getNoteSource()
    {
        return noteSource;
    }

    public void setNoteSource( String noteSource )
    {
        this.noteSource = noteSource;
    }

    public static StockNoteSourceDE newInstance( final StockNoteSourceEntity stockNoteSourceEntity )
    {
        StockNoteSourceDE stockNoteSourceDE = new StockNoteSourceDE();
        BeanUtils.copyProperties( stockNoteSourceEntity, stockNoteSourceDE );
        stockNoteSourceDE.setNoteSource( stockNoteSourceEntity.getName() );
        return stockNoteSourceDE;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockNoteSourceDE{" );
        sb.append( "noteSource='" ).append( noteSource ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
