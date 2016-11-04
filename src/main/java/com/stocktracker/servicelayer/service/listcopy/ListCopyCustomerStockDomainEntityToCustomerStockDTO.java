package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.entity.CustomerStockDomainEntity;
import com.stocktracker.weblayer.dto.CustomerStockDTO;
import org.springframework.stereotype.Service;

/**
 * Created by mike on 9/10/2016.
 */
@Service
public class ListCopyCustomerStockDomainEntityToCustomerStockDTO extends ListCopyProperties<CustomerStockDomainEntity, CustomerStockDTO>
    implements MyLogger
{
    public ListCopyCustomerStockDomainEntityToCustomerStockDTO()
    {
        super( CustomerStockDTO.class );
    }
}
