package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.entity.StockSectorEntity;
import com.stocktracker.servicelayer.entity.StockSectorDE;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyStockSectorEntityToStockSectorDE extends ListCopyProperties<StockSectorEntity, StockSectorDE>
{
    public ListCopyStockSectorEntityToStockSectorDE()
    {
        super( StockSectorDE.class );
    }

    /**
     * Copy the properties from the {@code source} to the {@code target}
     * @param source
     * @param target
     */
    protected void copyProperties( StockSectorEntity source, StockSectorDE target )
    {
        BeanUtils.copyProperties( source, target );
    }
}
