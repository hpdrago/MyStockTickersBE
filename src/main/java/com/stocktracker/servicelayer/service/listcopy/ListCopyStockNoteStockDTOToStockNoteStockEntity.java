package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.entity.StockNoteStockEntity;
import com.stocktracker.weblayer.dto.StockNoteStockDTO;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/4/2017.
 */
@Component
public class ListCopyStockNoteStockDTOToStockNoteStockEntity
    extends ListCopyProperties<StockNoteStockDTO, StockNoteStockEntity>
{
    public ListCopyStockNoteStockDTOToStockNoteStockEntity()
    {
        super( StockNoteStockEntity.class );
    }

}
