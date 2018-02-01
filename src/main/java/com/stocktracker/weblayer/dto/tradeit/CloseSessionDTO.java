package com.stocktracker.weblayer.dto.tradeit;


import com.stocktracker.servicelayer.tradeit.apiresults.CloseSessionAPIResult;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class CloseSessionDTO extends CloseSessionAPIResult
{
}
