package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.entity.StockNoteStockEntity;
import com.stocktracker.weblayer.dto.StockNoteStockDTO;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyStockNoteStockEntityToStockNoteStockDTO extends ListCopyProperties<StockNoteStockEntity,
    StockNoteStockDTO>
{
    public ListCopyStockNoteStockEntityToStockNoteStockDTO()
    {
        super( StockNoteStockDTO.class );
    }
}
