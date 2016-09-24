package com.stocktracker.common;

import com.stocktracker.repositorylayer.db.entity.StockEntity;
import com.stocktracker.servicelayer.entity.StockDomainEntity;
import org.springframework.stereotype.Service;

/**
 * Created by mike on 9/10/2016.
 */
@Service
public class ListCopyStockEntityToStockDomainEntity extends ListCopyProperties<StockEntity, StockDomainEntity>
{
    public ListCopyStockEntityToStockDomainEntity()
    {
        super( StockDomainEntity.class );
    }
}
