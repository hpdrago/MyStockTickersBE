package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.StockNoteEntity;
import com.stocktracker.repositorylayer.entity.StockNoteStockEntity;
import com.stocktracker.repositorylayer.entity.StockNoteStockEntityPK;
import com.stocktracker.servicelayer.service.StockService;
import com.stocktracker.weblayer.dto.StockNoteDTO;
import com.stocktracker.weblayer.dto.StockNoteStockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * This class converts a list of StockNoteStockDTOs into a list of StockNoteStockEntity instances.
 * Custom logic is required here as the StockNoteStockEntity has a composite primary key so a simple property copy
 * does not work in this case.
 *
 * Created by mike on 9/4/2017.
 */
@Component
public class ListCopyStockNoteStockDTOToStockNoteStockEntity
    extends ListCopyProperties<StockNoteStockDTO, StockNoteStockEntity>
    implements MyLogger
{
    /**
     * Autowired service class
     */
    private StockService stockService;

    public ListCopyStockNoteStockDTOToStockNoteStockEntity()
    {
        super( StockNoteStockEntity.class );
    }

    /**
     * Allow DI to set the StockService
     *
     * @param stockService
     */
    @Autowired
    public void setStockService( final StockService stockService )
    {
        this.stockService = stockService;
    }

    @Override
    protected void copyProperties( final StockNoteStockDTO source, final StockNoteStockEntity target )
    {
        StockNoteStockEntityPK id = new StockNoteStockEntityPK( source.getStockNotesId(), source.getTickerSymbol() );
        target.setId( id );
        target.setStockPrice( source.getStockPrice() );
        target.setCustomerId( source.getCustomerId() );
    }
}
