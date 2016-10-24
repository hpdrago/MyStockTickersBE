package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.entity.PortfolioDomainEntity;
import com.stocktracker.weblayer.dto.PortfolioDTO;
import org.springframework.stereotype.Service;

/**
 * Created by mike on 9/10/2016.
 */
@Service
public class ListCopyPortfolioDomainEntityToPortfolioDTO extends ListCopyProperties<PortfolioDomainEntity, PortfolioDTO>
    implements MyLogger
{
    public ListCopyPortfolioDomainEntityToPortfolioDTO()
    {
        super( PortfolioDTO.class );
    }
}
