package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.servicelayer.entity.PortfolioStockDE;
import com.stocktracker.weblayer.dto.PortfolioStockDTO;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyPortfolioStockDEToPortfolioStockDTO extends ListCopyProperties<PortfolioStockDE, PortfolioStockDTO>
{
    public ListCopyPortfolioStockDEToPortfolioStockDTO()
    {
        super( PortfolioStockDTO.class );
    }
}
