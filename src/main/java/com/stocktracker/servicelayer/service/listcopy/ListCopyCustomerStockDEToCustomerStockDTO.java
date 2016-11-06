package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.entity.CustomerStockDE;
import com.stocktracker.weblayer.dto.CustomerStockDTO;
import org.springframework.stereotype.Service;

/**
 * Created by mike on 9/10/2016.
 */
@Service
public class ListCopyCustomerStockDEToCustomerStockDTO extends ListCopyProperties<CustomerStockDE, CustomerStockDTO>
    implements MyLogger
{
    public ListCopyCustomerStockDEToCustomerStockDTO()
    {
        super( CustomerStockDTO.class );
    }
}
