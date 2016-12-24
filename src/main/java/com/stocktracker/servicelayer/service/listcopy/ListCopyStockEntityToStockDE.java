package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.db.entity.StockEntity;
import com.stocktracker.servicelayer.entity.StockDE;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyStockEntityToStockDE extends ListCopyProperties<StockEntity, StockDE>
{
    public ListCopyStockEntityToStockDE()
    {
        super( StockDE.class );
    }

    /**
     * Copy the properties from the {@code source} to the {@code target}
     * @param source
     * @param target
     */
    protected void copyProperties( StockEntity source, StockDE target )
    {
        source.fromDBEntityToDomainEntity( source, target );
    }
}
