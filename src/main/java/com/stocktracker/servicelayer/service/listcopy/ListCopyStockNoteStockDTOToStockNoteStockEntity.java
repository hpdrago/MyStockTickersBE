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

    /**
     * Creates a list of StockNoteStockEntity instances from the {@code dtos} and the {@code stockNoteEntity}
     * @param stockNoteDTO
     * @return
     */
    public List<StockNoteStockEntity> copy( final StockNoteEntity stockNoteEntity, final StockNoteDTO stockNoteDTO )
    {
        logMethodBegin( "copy", stockNoteEntity, stockNoteDTO );
        List<StockNoteStockEntity> entities = new ArrayList<>( stockNoteDTO.getStocks().size() );
        /*
         * Need to set the stock note id that was just created in the stocks
         */
        for ( StockNoteStockDTO stockNoteStockDTO: stockNoteDTO.getStocks() )
        {
            StockNoteStockEntity stockNoteStockEntity = new StockNoteStockEntity();
            stockNoteStockEntity.setId( stockNoteEntity.getId(), stockNoteStockDTO.getTickerSymbol() );
            stockNoteStockEntity.setCustomerId( stockNoteEntity.getCustomerId() );
            stockNoteStockEntity.setStockPrice( stockService.getStockPrice(
                stockNoteStockEntity.getId()
                                    .getTickerSymbol() ));
            entities.add( stockNoteStockEntity );
        }
        logMethodEnd( "copy", entities );
        return entities;
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
