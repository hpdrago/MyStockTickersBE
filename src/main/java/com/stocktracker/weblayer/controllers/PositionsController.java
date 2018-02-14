package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.weblayer.dto.LinkedAccountPositionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

public class PositionsController extends AbstractController
{
    private static final String CONTEXT_URL = "/positions";
    @Autowired
    private ApplicationContext context;
    /**
     * Get all of the positions for a linked account.
     * @return
     */
    @RequestMapping( value = CONTEXT_URL
                             + "/linkedAccountId/{linkedAccountId}"
                             + "/tradeItAccountId/{tradeItAccountId}"
                             + "/customer/{customerId}",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public LinkedAccountPositionDTO getPositions( final @PathVariable int linkedAccountId,
                                                  final @PathVariable int tradeItAccountId,
                                                  final @PathVariable int customerId )
        throws LinkedAccountNotFoundException
    {
        final String methodName = "getPositions";
        logMethodBegin( methodName, linkedAccountId, tradeItAccountId, customerId );
        final LinkedAccountPositionDTO linkedAccountPositionDTO = this.linkedAccountEntityService
            .getPositions( customerId, tradeItAccountId, linkedAccountId );
        logMethodEnd( methodName, "positions size: " + linkedAccountPositionDTO.size() );
        return linkedAccountPositionDTO;
    }
}
