package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.db.entity.VPortfolioStockEntity;
import com.stocktracker.servicelayer.entity.CustomerStockDE;
import org.springframework.stereotype.Service;

/**
 * Created by mike on 9/10/2016.
 */
@Service
public class ListCopyVPortfolioStockEntityToCustomerStockDE extends ListCopyProperties<VPortfolioStockEntity, CustomerStockDE>
{
    public ListCopyVPortfolioStockEntityToCustomerStockDE()
    {
        super( CustomerStockDE.class );
    }
}
