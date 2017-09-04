package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.entity.PortfolioStockEntity;
import com.stocktracker.servicelayer.entity.PortfolioStockDE;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyPortfolioStockEntityToPortfolioStockDE extends ListCopyProperties<PortfolioStockEntity, PortfolioStockDE>
{
    public ListCopyPortfolioStockEntityToPortfolioStockDE()
    {
        super( PortfolioStockDE.class );
    }
}
