package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.entity.VPortfolioStockEntity;
import com.stocktracker.servicelayer.entity.PortfolioStockDE;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyVPortfolioStockEntityToCustomerStockDE extends ListCopyProperties<VPortfolioStockEntity, PortfolioStockDE>
{
    public ListCopyVPortfolioStockEntityToCustomerStockDE()
    {
        super( PortfolioStockDE.class );
    }
}
