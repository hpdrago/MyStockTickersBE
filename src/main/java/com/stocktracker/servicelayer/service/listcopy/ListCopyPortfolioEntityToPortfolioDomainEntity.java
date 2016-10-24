package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.db.entity.PortfolioEntity;
import com.stocktracker.servicelayer.entity.PortfolioDomainEntity;
import org.springframework.stereotype.Service;

/**
 * Created by mike on 9/10/2016.
 */
@Service
public class ListCopyPortfolioEntityToPortfolioDomainEntity extends ListCopyProperties<PortfolioEntity, PortfolioDomainEntity>
{
    public ListCopyPortfolioEntityToPortfolioDomainEntity()
    {
        super( PortfolioDomainEntity.class );
    }
}
