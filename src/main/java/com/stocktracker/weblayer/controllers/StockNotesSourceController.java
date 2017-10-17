package com.stocktracker.weblayer.controllers;

import com.stocktracker.servicelayer.service.StockNoteSourceService;
import com.stocktracker.weblayer.dto.StockNoteSourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
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
    private StockNoteSourceService stockNoteSourceService;

    /**
     * Get all of the stocks notes sources for a customer
     *
     * @return
     */
    @RequestMapping( value = "/stockNotesSource/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<StockNoteSourceDTO> getStockNotesSources( final @PathVariable int customerId )
    {
        final String methodName = "getStockNotesSources";
        logMethodBegin( methodName, customerId );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        List<StockNoteSourceDTO> stockNoteSourceDTOs = stockNoteSourceService.getStockNoteSources( customerId );
        logMethodEnd( methodName, stockNoteSourceDTOs.size() );
        return stockNoteSourceDTOs;
    }

    /**
     * Add a stock note source to the database
     *
     * @return The stock that was added
     */
    @CrossOrigin
    @RequestMapping( value = "/stockNotesSource",
                     method = RequestMethod.POST )
    public ResponseEntity<StockNoteSourceDTO> addStockNoteSource( @RequestBody StockNoteSourceDTO stockNoteSourceDTO )
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
        Assert.isTrue( stockNoteSourceDTO.getId() > 0, "stockNoteSourceDTO.id must be > 0" );
        Assert.isTrue( stockNoteSourceDTO.getCustomerId() > 0, "stockNoteSourceDTO.customerId must be > 0" );
    }

    @Autowired
    public void setStockNoteSourceService( final StockNoteSourceService stockNoteSourceService )
    {
        this.stockNoteSourceService = stockNoteSourceService;
    }
}
