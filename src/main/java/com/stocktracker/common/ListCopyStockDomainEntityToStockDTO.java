package com.stocktracker.common;

import com.stocktracker.servicelayer.entity.StockDomainEntity;
import com.stocktracker.weblayer.dto.StockDTO;
import org.springframework.stereotype.Service;

/**
 * Created by mike on 9/10/2016.
 */
@Service
public class ListCopyStockDomainEntityToStockDTO extends ListCopyProperties<StockDomainEntity, StockDTO>
{
    public ListCopyStockDomainEntityToStockDTO()
    {
        super( StockDTO.class );
    }
}
