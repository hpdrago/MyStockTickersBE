package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.servicelayer.entity.CustomerDE;
import com.stocktracker.weblayer.dto.CustomerDTO;
import org.springframework.stereotype.Service;

/**
 * Created by mike on 9/10/2016.
 */
@Service
public class ListCopyCustomerDEToCustomerDTO extends ListCopyProperties<CustomerDE, CustomerDTO>
{
    public ListCopyCustomerDEToCustomerDTO()
    {
        super( CustomerDTO.class );
    }
}
