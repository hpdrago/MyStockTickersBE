package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.db.entity.StockNoteEntity;
import com.stocktracker.repositorylayer.db.entity.StockNoteSourceEntity;
import com.stocktracker.servicelayer.entity.StockNoteDE;
import com.stocktracker.servicelayer.entity.StockNoteSourceDE;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyStockNoteSourceEntityToStockNoteSourceDE extends ListCopyProperties<StockNoteSourceEntity, StockNoteSourceDE>
{
    public ListCopyStockNoteSourceEntityToStockNoteSourceDE()
    {
        super( StockNoteSourceDE.class );
    }
}
