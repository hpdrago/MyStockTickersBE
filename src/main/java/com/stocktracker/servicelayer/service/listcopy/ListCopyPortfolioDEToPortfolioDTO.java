package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.entity.PortfolioDE;
import com.stocktracker.weblayer.dto.PortfolioDTO;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyPortfolioDEToPortfolioDTO extends ListCopyProperties<PortfolioDE, PortfolioDTO>
    implements MyLogger
{
    public ListCopyPortfolioDEToPortfolioDTO()
    {
        super( PortfolioDTO.class );
    }
}
