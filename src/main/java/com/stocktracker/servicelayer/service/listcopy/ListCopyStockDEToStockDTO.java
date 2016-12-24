package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.entity.StockDE;
import com.stocktracker.weblayer.dto.StockDTO;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyStockDEToStockDTO extends ListCopyProperties<StockDE, StockDTO>
    implements MyLogger
{
    public ListCopyStockDEToStockDTO()
    {
        super( StockDTO.class );
    }
}
