package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.servicelayer.entity.StockNoteDE;
import com.stocktracker.servicelayer.entity.StockNoteSourceDE;
import com.stocktracker.servicelayer.service.StockNoteSourceService;
import com.stocktracker.weblayer.dto.StockNoteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyStockNoteDEToStockNoteDTO extends ListCopyProperties<StockNoteDE, StockNoteDTO>
{
    @Autowired
    private StockNoteSourceService stockNoteSourceService;

    public ListCopyStockNoteDEToStockNoteDTO()
    {
        super( StockNoteDTO.class );
    }

    @Override
    protected void copyProperties( final StockNoteDE source, final StockNoteDTO target )
    {
        super.copyProperties( source, target );
        StockNoteSourceDE stockNoteSourceDE = stockNoteSourceService.getStockNoteSource( source.getNotesSourceId() );
        target.setSource( stockNoteSourceDE.getNoteSource() );
    }

    public StockNoteSourceService getStockNoteSourceService()
    {
        return stockNoteSourceService;
    }

    public void setStockNoteSourceService( StockNoteSourceService stockNoteSourceService )
    {
        this.stockNoteSourceService = stockNoteSourceService;
    }
}
