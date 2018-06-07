package com.stocktracker.weblayer.controllers;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.servicelayer.service.StockNoteSourceEntityService;
import com.stocktracker.weblayer.dto.StockNoteSourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Objects;

/**
 * This is the control (entry point) for all REST services for stock notes sources.
 */
@RestController
@CrossOrigin
public class StockNotesSourceController extends AbstractController
{
    private StockNoteSourceEntityService stockNoteSourceService;
    private static final String CONTEXT_URL = "/stockNotesSource";

    /**
     * Get all of the stocks notes sources for a customer
     *
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<StockNoteSourceDTO> getStockNotesSources( final @PathVariable String customerId )
    {
        final String methodName = "getStockNotesSources";
        logMethodBegin( methodName, customerId );
        List<StockNoteSourceDTO> stockNoteSourceDTOs = this.stockNoteSourceService
                                                           .getStockNoteSources( UUIDUtil.uuid( customerId ));
        logMethodEnd( methodName, stockNoteSourceDTOs.size() );
        return stockNoteSourceDTOs;
    }

    /**
     * Add a stock note source to the database
     * @param stockNoteSourceDTO
     * @return The stock that was added
     * @return
     * @throws EntityVersionMismatchException
     * @throws DuplicateEntityException
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/customerId/customerId",
                     method = RequestMethod.POST )
    public ResponseEntity<StockNoteSourceDTO> addStockNoteSource( @RequestBody StockNoteSourceDTO stockNoteSourceDTO )
        throws EntityVersionMismatchException,
               DuplicateEntityException
    {
        final String methodName = "addStockNotesSource";
        logMethodBegin( methodName, stockNoteSourceDTO );
        validateStockNoteSourceDTO( stockNoteSourceDTO );
        StockNoteSourceDTO returnStockSourceDTO = this.stockNoteSourceService.createStockNoteSource( stockNoteSourceDTO );
        logDebug( methodName, "returnStockDTO: ", returnStockSourceDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( stockNoteSourceDTO ).toUri());
        logMethodEnd( methodName, returnStockSourceDTO );
        return new ResponseEntity<>( returnStockSourceDTO, httpHeaders, HttpStatus.CREATED );
    }

    private void validateStockNoteSourceDTO( final StockNoteSourceDTO stockNoteSourceDTO )
    {
        Objects.requireNonNull( stockNoteSourceDTO.getId(), "stockNoteSourceDTO.id cannot be null" );
        Objects.requireNonNull( stockNoteSourceDTO.getName(), "stockNoteSourceDTO.name cannot be null" );
        Objects.requireNonNull( stockNoteSourceDTO.getCustomerId(), "stockNoteSourceDTO.customerId cannot be null" );
    }

    @Autowired
    public void setStockNoteSourceService( final StockNoteSourceEntityService stockNoteSourceService )
    {
        this.stockNoteSourceService = stockNoteSourceService;
    }
}
