package com.stocktracker.weblayer.controllers;

import com.stocktracker.servicelayer.tradeit.TradeItService;
import com.stocktracker.weblayer.dto.TradeItBrokersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * TradeIt REST Controller
 */
@RestController
@CrossOrigin
public class TradeItController extends AbstractController
{
    private static final String CONTEXT_URL = "/tradeIt";
    private TradeItService tradeItService;

    /**
     * Cache this result as it should hardly change
     */
    private TradeItBrokersDTO tradeItBrokersDTO;

    /**
     * Get all of the stock to buy for a customer
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/brokers",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public TradeItBrokersDTO getBrokers()
    {
        final String methodName = "getBrokers";
        logMethodBegin( methodName );
        if ( this.tradeItBrokersDTO == null )
        {
            logDebug( methodName, "Brokers not cached, retrieving now" );
            this.tradeItBrokersDTO = this.tradeItService.getBrokers();
        }
        else
        {
            logDebug( methodName, "returning cached broker list");
        }
        logMethodEnd( methodName );
        return this.tradeItBrokersDTO;
    }

    @Autowired
    public void setTradeItService( final TradeItService tradeItService )
    {
        this.tradeItService = tradeItService;
    }
}
