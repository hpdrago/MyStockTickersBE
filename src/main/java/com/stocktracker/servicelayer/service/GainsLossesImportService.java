package com.stocktracker.servicelayer.service;

import com.stocktracker.repositorylayer.entity.GainsLossesEntity;
import com.stocktracker.repositorylayer.repository.GainsLossesRepository;
import com.stocktracker.servicelayer.service.cache.stockcompany.StockCompanyEntityCache;
import com.stocktracker.servicelayer.service.cache.stockcompany.StockCompanyEntityCacheEntry;
import com.stocktracker.weblayer.dto.GainsLossesImportConfigurationDTO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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
     * @param multipartFile
     * @param gainsLossesImportConfigurationDTO
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public String importGainsLosses( final UUID customerUuid,
                                     final MultipartFile multipartFile,
                                     final GainsLossesImportConfigurationDTO gainsLossesImportConfigurationDTO )
        throws IOException,
               InvalidFormatException
    {
        final StringBuilder results = new StringBuilder();
        results.append( "Results for file: " + multipartFile.getOriginalFilename() ).append( "\n" );
        try( InputStream excelBuffer = new ByteArrayInputStream( multipartFile.getBytes() ))
        {
            final Workbook workbook = WorkbookFactory.create( excelBuffer );
            final Sheet sheet = workbook.getSheetAt( 0);
            int firstRow = sheet.getFirstRowNum();
            int lastRow = sheet.getLastRowNum();
            if ( gainsLossesImportConfigurationDTO.isSkipFirstRow() )
            {
                firstRow++;
            }
            if ( gainsLossesImportConfigurationDTO.isSkipLastRow() )
            {
                lastRow--;
            }
            for ( int rowNumber = firstRow; rowNumber <= lastRow; rowNumber++ )
            {
                final Row row = sheet.getRow( rowNumber );
                final String tickerSymbol = this.getTickerSymbol( row, gainsLossesImportConfigurationDTO, results );
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
                                                                            .findByCustomerUuidAndTickerSymbol( customerUuid,
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

                    if ( !errorsFound )
                    {

                    }
                }
            }
            /*
            if (cell == null)
                cell = row.createCell(3);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("a test");*/
        }
        return "";
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
     * @param columnName
     * @param columnNumber
     * @param results
     * @return
     */
    private Double getNumericColumn( final Row row,
                                     final String columnName,
                                     final int columnNumber,
                                     final StringBuilder results )
    {
        final Cell gainLossColumn = row.getCell( columnNumber );
        if ( gainLossColumn.getCellTypeEnum() != CellType.NUMERIC )
        {
            results.append( String.format( "%s column (%d) is not a string data type",
                                           columnName, columnNumber )).append( "\n" );
            return null;
        }
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
        final Cell tickerSymbolCell = row.getCell( gainsLossesImportConfigurationDTO.getTickerSymbolColumn() );
        if ( tickerSymbolCell.getCellTypeEnum() != CellType.STRING )
        {
            results.append( String.format( "Ticker symbol column (%d) is not a string data type",
                                           gainsLossesImportConfigurationDTO.getTickerSymbolColumn() )).append( "\n" );
            return null;
        }
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
                results.append( String.format( "Ticker symbol %s was not found", tickerSymbol )).append( "\n" );
                tickerSymbol = null;
                break;

            case CURRENT:
                break; // expected result

            case STALE:
                throw new IllegalStateException( "Invalid STALE stock company cache state for " + tickerSymbol );

            case FAILURE:
                results.append( String.format( "Encountered error '%s' retrieving stock company for %s",
                                               cacheEntry.getFetchThrowable().getMessage(), tickerSymbol ) );
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
            returnValue = tickerSymbol.substring( leftParenPos+1, rightParenPos-1 );
        }
        return returnValue;
    }
}