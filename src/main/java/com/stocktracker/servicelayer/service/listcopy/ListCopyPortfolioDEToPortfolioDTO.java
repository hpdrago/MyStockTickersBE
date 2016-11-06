package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.entity.PortfolioDE;
import com.stocktracker.weblayer.dto.PortfolioDTO;
import org.springframework.stereotype.Service;

/**
 * Created by mike on 9/10/2016.
 */
@Service
public class ListCopyPortfolioDEToPortfolioDTO extends ListCopyProperties<PortfolioDE, PortfolioDTO>
    implements MyLogger
{
    public ListCopyPortfolioDEToPortfolioDTO()
    {
        super( PortfolioDTO.class );
    }
}
