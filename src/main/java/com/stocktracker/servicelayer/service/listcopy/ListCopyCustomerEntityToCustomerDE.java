package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.db.entity.CustomerEntity;
import com.stocktracker.servicelayer.entity.CustomerDE;
import org.springframework.stereotype.Component;

/**
 * Created by mike on 9/10/2016.
 */
@Component
public class ListCopyCustomerEntityToCustomerDE extends ListCopyProperties<CustomerEntity, CustomerDE>
{
    public ListCopyCustomerEntityToCustomerDE()
    {
        super( CustomerDE.class );
    }
}
