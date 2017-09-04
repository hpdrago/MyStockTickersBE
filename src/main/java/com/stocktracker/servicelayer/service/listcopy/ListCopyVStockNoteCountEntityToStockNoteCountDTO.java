package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.entity.VStockNoteCountEntity;
import com.stocktracker.weblayer.dto.StockNoteCountDTO;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyVStockNoteCountEntityToStockNoteCountDTO
    extends ListCopyProperties<VStockNoteCountEntity, StockNoteCountDTO>
{
    public ListCopyVStockNoteCountEntityToStockNoteCountDTO()
    {
        super( StockNoteCountDTO.class );
    }
}
