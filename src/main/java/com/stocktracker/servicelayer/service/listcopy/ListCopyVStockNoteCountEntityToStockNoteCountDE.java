package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.db.entity.VStockNoteCountEntity;
import com.stocktracker.servicelayer.entity.StockNoteCountDE;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyVStockNoteCountEntityToStockNoteCountDE
    extends ListCopyProperties<VStockNoteCountEntity, StockNoteCountDE>
{
    public ListCopyVStockNoteCountEntityToStockNoteCountDE()
    {
        super( StockNoteCountDE.class );
    }
}
