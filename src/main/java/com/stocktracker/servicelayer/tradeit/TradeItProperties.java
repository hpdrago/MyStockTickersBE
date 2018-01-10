package com.stocktracker.servicelayer.tradeit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * This class contains the TradeIt properties from the application.properties file
 */
@Configuration()
public class TradeItProperties
{
    public static final String BROKER_PARAM = "broker";
    public static final String USER_ID_PARAM = "userId";
    public static final String USER_TOKEN_PARAM = "userToken";
    public static final String API_KEY_PARAM = "apiKey";
    public static final String OAUTH_VERIFIER_PARAM = "oAuthVerifier";

    @Value( "${stocktracker.tradeit.api.key}" )
    private String apiKey;

    public String getApiKey()
    {
        return apiKey;
    }

}
