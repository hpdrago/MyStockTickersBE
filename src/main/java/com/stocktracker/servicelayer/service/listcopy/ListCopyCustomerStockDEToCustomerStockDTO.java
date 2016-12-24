package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.entity.PortfolioStockDE;
import com.stocktracker.weblayer.dto.PortfolioStockDTO;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyCustomerStockDEToCustomerStockDTO extends ListCopyProperties<PortfolioStockDE, PortfolioStockDTO>
    implements MyLogger
{
    public ListCopyCustomerStockDEToCustomerStockDTO()
    {
        super( PortfolioStockDTO.class );
    }
}
