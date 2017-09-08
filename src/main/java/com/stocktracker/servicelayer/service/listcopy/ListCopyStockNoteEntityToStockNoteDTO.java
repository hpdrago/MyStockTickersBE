package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.common.JSONDateConverter;
import com.stocktracker.repositorylayer.entity.StockNoteEntity;
import com.stocktracker.repositorylayer.entity.StockNoteStockEntity;
import com.stocktracker.weblayer.dto.StockNoteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will convert a list of StockNoteEntity instances to a list of StockNoteDTO instances.
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyStockNoteEntityToStockNoteDTO extends ListCopyProperties<StockNoteEntity, StockNoteDTO>
{
    private ListCopyStockNoteStockEntityToStockNoteStockDTO listCopyStockNoteStockEntityToStockNoteStockDTO;

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

        this.listCopyStockNoteStockEntityToStockNoteStockDTO.copy( source.getStockNoteStocks(),
                                                                   target.getStockNotesStocks() );
    }

    @Autowired
    public void setListCopyStockNoteStockEntityToStockNoteStockDTO(
        final ListCopyStockNoteStockEntityToStockNoteStockDTO listCopyStockNoteStockEntityToStockNoteStockDTO )
    {
        this.listCopyStockNoteStockEntityToStockNoteStockDTO = listCopyStockNoteStockEntityToStockNoteStockDTO;
    }
}
