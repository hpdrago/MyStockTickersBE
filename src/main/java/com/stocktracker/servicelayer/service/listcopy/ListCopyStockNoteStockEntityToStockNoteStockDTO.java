package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.entity.StockNoteStockEntity;
import com.stocktracker.weblayer.dto.StockNoteStockDTO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyStockNoteStockEntityToStockNoteStockDTO extends ListCopyProperties<StockNoteStockEntity,
    StockNoteStockDTO>
{
    public ListCopyStockNoteStockEntityToStockNoteStockDTO()
    {
        super( StockNoteStockDTO.class );
    }

    /**
     * This method will create convert the {@code StockNoteStockEntity} instances into {@code StockNoteStockDTO} instancs.
     * @param stockNoteStockEntities
     * @param stockNoteStockDTOs
     */
    public void copy( final List<StockNoteStockEntity> stockNoteStockEntities,
                      final List<StockNoteStockDTO> stockNoteStockDTOs )
    {
        for ( StockNoteStockEntity stockNoteStockEntity: stockNoteStockEntities )
        {
            StockNoteStockDTO stockNoteStockDTO = StockNoteStockDTO.newInstance( stockNoteStockEntity );
            stockNoteStockDTOs.add( stockNoteStockDTO );
        }
    }
}
