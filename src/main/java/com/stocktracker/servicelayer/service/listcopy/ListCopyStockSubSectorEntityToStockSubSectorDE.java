package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.entity.StockSubSectorEntity;
import com.stocktracker.servicelayer.entity.StockSubSectorDE;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * Created by mike on 9/10/2016.
 */
@Service
public class ListCopyStockSubSectorEntityToStockSubSectorDE extends ListCopyProperties<StockSubSectorEntity, StockSubSectorDE>
{
    public ListCopyStockSubSectorEntityToStockSubSectorDE()
    {
        super( StockSubSectorDE.class );
    }

    /**
     * Copy the properties from the {@code source} to the {@code target}
     * @param source
     * @param target
     */
    protected void copyProperties( StockSubSectorEntity source, StockSubSectorDE target )
    {
        BeanUtils.copyProperties( source, target );
    }
}
