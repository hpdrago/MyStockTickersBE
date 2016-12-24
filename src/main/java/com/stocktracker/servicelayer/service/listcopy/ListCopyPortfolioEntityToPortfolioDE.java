package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.db.entity.PortfolioEntity;
import com.stocktracker.servicelayer.entity.PortfolioDE;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyPortfolioEntityToPortfolioDE extends ListCopyProperties<PortfolioEntity, PortfolioDE>
{
    public ListCopyPortfolioEntityToPortfolioDE()
    {
        super( PortfolioDE.class );
    }
}
