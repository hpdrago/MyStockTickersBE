package com.stocktracker.weblayer.controllers;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAPIException;
import com.stocktracker.common.exceptions.TradeItAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAuthenticationException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.servicelayer.service.StockPositionService;
import com.stocktracker.weblayer.dto.StockPositionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * This controller handles request for stock positions from the LINKED_ACCOUNT_POSITION table.
 */
@RestController
@CrossOrigin
public class StockPositionController extends AbstractController
{
    private static final String CONTEXT_URL = "/stockPosition";

    /**
     * Service class that performs all of the entity work.
     */
    private StockPositionService stockPositionService;

    /**
     * Get all of the positions for a linked account.
     * @return
     * @throws TradeItAccountNotFoundException
     * @throws LinkedAccountNotFoundException
     * @throws TradeItAuthenticationException
     * @throws EntityVersionMismatchException
     * @throws VersionedEntityNotFoundException
     * @throws DuplicateEntityException
     */
    @RequestMapping( value = CONTEXT_URL
                             + "/linkedAccountId/{linkedAccountId}"
                             + "/tradeItAccountId/{tradeItAccountId}"
                             + "/customerId/{customerId}",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<StockPositionDTO> getPositions( final @PathVariable String linkedAccountId,
                                                final @PathVariable String tradeItAccountId,
                                                final @PathVariable String customerId )
        throws LinkedAccountNotFoundException,
               TradeItAccountNotFoundException,
               TradeItAPIException,
               EntityVersionMismatchException,
               VersionedEntityNotFoundException,
               DuplicateEntityException
    {
        final String methodName = "getPositions";
        logMethodBegin( methodName, linkedAccountId, tradeItAccountId, customerId );
        final List<StockPositionDTO> positions = this.stockPositionService
                                                     .getPositions( UUIDUtil.uuid( customerId ),
                                                                    UUIDUtil.uuid( tradeItAccountId ),
                                                                    UUIDUtil.uuid( linkedAccountId ));
        //logDebug( methodName, "positions: {0}", positions );
        logMethodEnd( methodName, "positions size: " + positions.size() );
        return positions;
    }

    @Autowired
    public void setStockPositionService( final StockPositionService stockPositionService )
    {
        this.stockPositionService = stockPositionService;
    }
}
