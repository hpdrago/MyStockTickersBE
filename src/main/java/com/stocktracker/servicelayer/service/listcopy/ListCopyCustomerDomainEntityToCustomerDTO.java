package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.servicelayer.entity.CustomerDomainEntity;
import com.stocktracker.weblayer.dto.CustomerDTO;
import org.springframework.stereotype.Service;

/**
 * Created by mike on 9/10/2016.
 */
@Service
public class ListCopyCustomerDomainEntityToCustomerDTO extends ListCopyProperties<CustomerDomainEntity, CustomerDTO>
{
    public ListCopyCustomerDomainEntityToCustomerDTO()
    {
        super( CustomerDTO.class );
    }
}
