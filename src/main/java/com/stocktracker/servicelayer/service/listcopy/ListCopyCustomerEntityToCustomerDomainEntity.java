package com.stocktracker.servicelayer.service.listcopy;

import com.stocktracker.repositorylayer.db.entity.CustomerEntity;
import com.stocktracker.servicelayer.entity.CustomerDomainEntity;
import org.springframework.stereotype.Service;

/**
 * Created by mike on 9/10/2016.
 */
@Service
public class ListCopyCustomerEntityToCustomerDomainEntity extends ListCopyProperties<CustomerEntity, CustomerDomainEntity>
{
    public ListCopyCustomerEntityToCustomerDomainEntity()
    {
        super( CustomerDomainEntity.class );
    }
}
