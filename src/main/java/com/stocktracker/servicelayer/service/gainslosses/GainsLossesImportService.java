package com.stocktracker.servicelayer.service.gainslosses;

import com.stocktracker.repositorylayer.entity.GainsLossesEntity;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.repository.GainsLossesRepository;
import com.stocktracker.servicelayer.service.BaseService;
import com.stocktracker.servicelayer.service.cache.stockcompany.StockCompanyEntityCache;
import com.stocktracker.servicelayer.service.cache.stockcompany.StockCompanyEntityCacheEntry;
import com.stocktracker.weblayer.dto.GainsLossesImportConfigurationDTO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * This service contains the logic to import gain/loss data from an excel spreadsheet and insert rows into the
 * gain_loss table.
 */
@Service
public class GainsLossesImportService extends BaseService
{
    @Autowired
    private StockCompanyEntityCache stockCompanyEntityCache;

    @Autowired
    private GainsLossesRepository gainsLossesRepository;

    /**
     * Imports a single file.
     * @param customerUuid
     * @param linkedAccountEntity
     * @param multipartFile
     * @param gainsLossesImportConfigurationDTO
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    @Transactional
    public void importGainsLosses( final UUID customerUuid,
                                   final LinkedAccountEntity linkedAccountEntity,
                                   final MultipartFile multipartFile,
                                   final GainsLossesImportConfigurationDTO gainsLossesImportConfigurationDTO,
                                   final StringBuilder results )
        throws IOException,
               InvalidFormatException
    {
        final String methodName = "importGainsLosses";
        logMethodBegin( methodName, customerUuid, linkedAccountEntity.getAccountNumber(),
                        multipartFile.getOriginalFilename(), gainsLossesImportConfigurationDTO );
        results.append( "Results for file: " + multipartFile.getOriginalFilename() ).append( '\n' );
        final ImportStats importStats = new ImportStats();
        try( InputStream excelBuffer = new ByteArrayInputStream( multipartFile.getBytes() ))
        {
            final Workbook workbook = WorkbookFactory.create( excelBuffer );
            final Sheet sheet = workbook.getSheetAt( 0);
            int firstRow = sheet.getFirstRowNum();
            int lastRow = sheet.getLastRowNum();
            firstRow += gainsLossesImportConfigurationDTO.getSkipHeaderRows();
            lastRow -= gainsLossesImportConfigurationDTO.getSkipFooterRows();
            for ( int rowNumber = firstRow; rowNumber < lastRow; rowNumber++ )
            {
                final Row row = sheet.getRow( rowNumber );
                importRow( customerUuid, linkedAccountEntity, row, gainsLossesImportConfigurationDTO, results, importStats );
            }
        }
        results.append( "Successfully imported" );
        logMethodEnd( methodName );
    }

    /**
     * Import a single row.
     * @param customerUuid
     * @param linkedAccountEntity
     * @param row
     * @param gainsLossesImportConfigurationDTO
     * @param results
     * @param importStats
     */
    private void importRow( final UUID customerUuid,
                            final LinkedAccountEntity linkedAccountEntity,
                            final Row row,
                            final GainsLossesImportConfigurationDTO gainsLossesImportConfigurationDTO,
                            final StringBuilder results,
                            final ImportStats importStats )
    {
        final String methodName = "importRow";
        Objects.requireNonNull( row, "row argument cannot be null" );
        Objects.requireNonNull( gainsLossesImportConfigurationDTO, "gainsLossesImportConfigurationDTO argument cannot be null" );
        Objects.requireNonNull( results, "results argument cannot be null" );
        final String tickerSymbol = this.getTickerSymbol( row, gainsLossesImportConfigurationDTO, results );
        logMethodBegin( methodName, customerUuid, row.getRowNum(), tickerSymbol );
        /*
         * The tickerSymbol will be null if there are errors encountered while attempting to get a stock company
         * or extracting the ticker symbol from the import data.  The results argument will contain the cumulative
         * error information to report back to the user.  The results will contain the accumulated errors.
         */
        if ( tickerSymbol != null )
        {
            /*
             * This isn't your typical error handling since we are trying to process the entire file and accumulate
             * any errors and return those errors.
             */
            boolean errorsFound = false;
            final GainsLossesEntity existingGainsLossesEntity = this.gainsLossesRepository
                                                                    .findByCustomerUuidAndLinkedAccountUuidAndTickerSymbol(
                                                                        customerUuid, linkedAccountEntity.getUuid(),
                                                                        tickerSymbol );
            final GainsLossesEntity gainsLossesEntity = this.context.getBean( GainsLossesEntity.class );
            /*
             * There is either a column that contains either a gain(+) or loss(-) value
             * or there's two separate columns which is the else condition.
             */
            if ( gainsLossesImportConfigurationDTO.getGainsLossColumn() != -1 )
            {
                final Double gainLossValue = this.getGainLossColumnValue( row,
                                                                          gainsLossesImportConfigurationDTO,
                                                                          results );
                gainsLossesEntity.setTickerSymbol( tickerSymbol );
                gainsLossesEntity.setCustomerUuid( customerUuid );
                if ( gainLossValue < 0 )
                {
                    gainsLossesEntity.setLosses( new BigDecimal( gainLossValue ) );
                }
                else
                {
                    gainsLossesEntity.setGains( new BigDecimal( gainLossValue ) );
                }
            }
            else if ( gainsLossesImportConfigurationDTO.getGainsColumn() != -1 &&
                      gainsLossesImportConfigurationDTO.getGainsLossColumn() != -1 )
            {
                final Double gains = this.getNumericColumn( row,
                                                            "gains",
                                                            gainsLossesImportConfigurationDTO.getGainsColumn(),
                                                            results );
                if ( gains == null )
                {
                    errorsFound = true;
                }
                else
                {
                    gainsLossesEntity.setGains( new BigDecimal( gains ) );
                    final Double losses = this.getNumericColumn( row,
                                                                "gains",
                                                                gainsLossesImportConfigurationDTO.getGainsColumn(),
                                                                results );
                    if ( losses == null )
                    {
                        errorsFound = true;
                    }
                    else
                    {
                        gainsLossesEntity.setLosses( new BigDecimal( losses ));
                    }
                }
            }
            else
            {
                throw new IllegalArgumentException( "Invalid gains/loss column configuration" );
            }

            /*
             * Make the database update
             */
            if ( !errorsFound )
            {
                if ( existingGainsLossesEntity == null )
                {
                    gainsLossesEntity.setLinkedAccountByLinkedAccountUuid( linkedAccountEntity );
                    gainsLossesEntity.setCustomerUuid( customerUuid );
                    this.gainsLossesRepository
                        .save( gainsLossesEntity );
                    importStats.newEntries++;
                }
                else
                {
                    if ( gainsLossesEntity.getGains() != null )
                    {
                        existingGainsLossesEntity.addGains( gainsLossesEntity.getGains() );
                    }
                    if ( gainsLossesEntity.getLosses() != null )
                    {
                        existingGainsLossesEntity.addLosses( gainsLossesEntity.getLosses() );
                    }
                    this.gainsLossesRepository
                        .save( existingGainsLossesEntity );
                    importStats.updatedEntries++;
                }
            }
            else
            {
                importStats.importErrors++;
            }
        }
        logMethodEnd( methodName );
    }

    /**
     * Get the gain/loss column.  A loss is a negative value and a gain is a positive value contained in the same column
     * but different rows.
     * @param row
     * @param gainsLossesImportConfigurationDTO
     * @param results
     * @return
     */
    private Double getGainLossColumnValue( final Row row,
                                           final GainsLossesImportConfigurationDTO gainsLossesImportConfigurationDTO,
                                           final StringBuilder results )
    {
        return getNumericColumn( row,
                                 "Gain/Loss",
                                 gainsLossesImportConfigurationDTO.getGainsLossColumn(),
                                 results );
    }

    /**
     * Extract the numeric value from the co
     * @param row
     * @param columnNumber
     * @return
     */
    private Double getNumericColumn( final Row row,
                                     final String columnName,
                                     final int columnNumber,
                                     final StringBuilder results )
    {
        if ( columnNumber == 0 )
        {
            throw new IllegalArgumentException( "columnName must be > 0" );
        }
        /*
         * The cells are zero based but the configuration DTO is 1 based
         */
        final Cell gainLossColumn = row.getCell( columnNumber - 1 );
        return gainLossColumn.getNumericCellValue();
    }

    /**
     * Extracts the ticker symbol from the Excel {@code row}.
     * @param row
     * @param gainsLossesImportConfigurationDTO
     * @param results
     * @return
     */
    private String getTickerSymbol( final Row row,
                                    final GainsLossesImportConfigurationDTO gainsLossesImportConfigurationDTO,
                                    final StringBuilder results )

    {
        String tickerSymbol = null;
        /*
         * The cells are zero based but the configuration DTO is 1 based
         */
        final Cell tickerSymbolCell = row.getCell( gainsLossesImportConfigurationDTO.getTickerSymbolColumn() - 1 );
        tickerSymbol = tickerSymbolCell.getStringCellValue();
        if ( gainsLossesImportConfigurationDTO.isTickerSymbolEmbeddedWithParens() )
        {
            tickerSymbol = this.extractTickerFromParents( tickerSymbol );
        }
        final StockCompanyEntityCacheEntry cacheEntry = this.stockCompanyEntityCache
                                                            .synchronousGet( tickerSymbol );
        switch ( cacheEntry.getCacheState() )
        {
            case NOT_FOUND:
                results.append( String.format( "Ticker symbol %s was not found", tickerSymbol )).append( '\n' );
                tickerSymbol = null;
                break;

            case CURRENT:
                break; // expected result

            case STALE:
                throw new IllegalStateException( "Invalid STALE stock company cache state for " + tickerSymbol );

            case FAILURE:
                results.append( cacheEntry.getFetchThrowable().getMessage() ).append( '\n' );
                tickerSymbol = null;
                break;
        }
        return tickerSymbol;
    }

    /**
     * Extracts the ticker symbol between ().
     * @param tickerSymbol
     * @return
     */
    private String extractTickerFromParents( final String tickerSymbol )
    {
        final int leftParenPos = tickerSymbol.indexOf( '(' );
        final int rightParenPos = tickerSymbol.indexOf( ')' );
        String returnValue = tickerSymbol;
        if ( leftParenPos >= 0 && rightParenPos > leftParenPos )
        {
            returnValue = tickerSymbol.substring( leftParenPos+1, rightParenPos );
        }
        return returnValue;
    }

    private class ImportStats
    {
        private int newEntries;
        private int updatedEntries;
        private int importErrors;
    }
}