package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.common.JSONDateConverter;
import com.stocktracker.repositorylayer.entity.StockNoteEntity;
import com.stocktracker.weblayer.dto.StockNoteDTO;
import org.springframework.stereotype.Component;

import java.text.ParseException;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyStockNoteEntityToStockNoteDTO extends ListCopyProperties<StockNoteEntity, StockNoteDTO>
{
    public ListCopyStockNoteEntityToStockNoteDTO()
    {
        super( StockNoteDTO.class );
    }

    @Override
    protected void copyProperties( final StockNoteEntity source, final StockNoteDTO target )
    {
        super.copyProperties( source, target );
        try
        {
            target.setNotesDate( JSONDateConverter.toString( source.getNotesDate() ) );
        }
        catch ( ParseException e )
        {
            throw new IllegalArgumentException( "Failed to convert notes_date to a string", e );
        }
    }
}
