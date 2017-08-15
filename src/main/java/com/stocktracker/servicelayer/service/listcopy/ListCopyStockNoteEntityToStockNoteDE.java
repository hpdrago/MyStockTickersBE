package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.db.entity.StockNoteEntity;
import com.stocktracker.servicelayer.entity.StockNoteDE;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyStockNoteEntityToStockNoteDE extends ListCopyProperties<StockNoteEntity, StockNoteDE>
{
    public ListCopyStockNoteEntityToStockNoteDE()
    {
        super( StockNoteDE.class );
    }
}
