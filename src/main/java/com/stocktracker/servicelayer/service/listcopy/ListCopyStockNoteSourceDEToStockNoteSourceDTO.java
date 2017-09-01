package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.db.entity.StockNoteSourceEntity;
import com.stocktracker.servicelayer.entity.StockNoteSourceDE;
import com.stocktracker.weblayer.dto.StockNoteSourceDTO;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyStockNoteSourceDEToStockNoteSourceDTO extends ListCopyProperties<StockNoteSourceDE, StockNoteSourceDTO>
{
    public ListCopyStockNoteSourceDEToStockNoteSourceDTO()
    {
        super( StockNoteSourceDTO.class );
    }
}
