package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import com.stocktracker.weblayer.dto.StockNoteSourceDTO;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyStockNoteSourceEntityToStockNoteSourceDTO
    extends ListCopyProperties<StockNoteSourceEntity, StockNoteSourceDTO>
{
    public ListCopyStockNoteSourceEntityToStockNoteSourceDTO()
    {
        super( StockNoteSourceDTO.class );
    }
}
