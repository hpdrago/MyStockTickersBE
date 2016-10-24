package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.entity.StockDomainEntity;
import com.stocktracker.weblayer.dto.StockDTO;
import org.springframework.stereotype.Service;

/**
 * Created by mike on 9/10/2016.
 */
@Service
public class ListCopyStockDomainEntityToStockDTO extends ListCopyProperties<StockDomainEntity, StockDTO>
    implements MyLogger
{
    public ListCopyStockDomainEntityToStockDTO()
    {
        super( StockDTO.class );
    }
}
