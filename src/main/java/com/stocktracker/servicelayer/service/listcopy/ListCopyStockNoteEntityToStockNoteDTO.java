package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.entity.StockNoteEntity;
import com.stocktracker.weblayer.dto.StockNoteDTO;
import org.springframework.stereotype.Component;

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
}
