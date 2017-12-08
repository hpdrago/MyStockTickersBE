package com.stocktracker.servicelayer.tradeit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * This class contains the TradeIt properties from the application.properties file
 */
@Configuration()
public class TradeItProperties
{
    @Value( "${stocktracker.tradeit.api.key}" )
    private String apiKey;

    public String getApiKey()
    {
        return apiKey;
    }

}
