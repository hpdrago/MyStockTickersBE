package com.stocktracker.weblayer.dto;

import com.stocktracker.servicelayer.entity.StockNoteSourceDE;
import org.springframework.beans.BeanUtils;

/**
 * Created by mike on 5/13/2017.
 */
public class StockNoteSourceDTO
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

    public static StockNoteSourceDTO newInstance( final StockNoteSourceDE stockNoteSourceDE )
    {
        StockNoteSourceDTO stockNoteSourceDTO = new StockNoteSourceDTO();
        BeanUtils.copyProperties( stockNoteSourceDE, stockNoteSourceDTO );
        stockNoteSourceDTO.setNoteSource( stockNoteSourceDE.getNoteSource() );
        return stockNoteSourceDTO;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockNoteSourceDTO{" );
        sb.append( "noteSource='" ).append( noteSource ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
