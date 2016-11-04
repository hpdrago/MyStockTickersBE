package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.db.entity.VPortfolioStockEntity;
import com.stocktracker.servicelayer.entity.CustomerStockDomainEntity;
import org.springframework.stereotype.Service;

/**
 * Created by mike on 9/10/2016.
 */
@Service
public class ListCopyVPortfolioStockEntityToCustomerStockDomainEntity extends ListCopyProperties<VPortfolioStockEntity, CustomerStockDomainEntity>
{
    public ListCopyVPortfolioStockEntityToCustomerStockDomainEntity()
    {
        super( CustomerStockDomainEntity.class );
    }
}
