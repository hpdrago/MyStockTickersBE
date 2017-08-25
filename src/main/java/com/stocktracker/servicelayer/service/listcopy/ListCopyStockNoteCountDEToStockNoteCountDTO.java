package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.servicelayer.entity.StockNoteCountDE;
import com.stocktracker.weblayer.dto.StockNoteCountDTO;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyStockNoteCountDEToStockNoteCountDTO
    extends ListCopyProperties<StockNoteCountDE, StockNoteCountDTO>
{
    public ListCopyStockNoteCountDEToStockNoteCountDTO()
    {
        super( StockNoteCountDTO.class );
    }
}
