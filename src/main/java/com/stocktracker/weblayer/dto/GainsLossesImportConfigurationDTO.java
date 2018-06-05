package com.stocktracker.weblayer.dto;

/**
 * This class contains the parameters to import gains and losses in an excel file.
 */
public class GainsLossesImportConfigurationDTO
{
    private int skipHeaderRows;
    private int skipFooterRows;
    private boolean tickerSymbolEmbeddedWithParens;
    private int tickerSymbolColumn;
    private int gainsColumn;
    private int lossColumn;
    private int gainsLossColumn;
    private boolean clearEntries;

    public boolean isClearEntries()
    {
        return clearEntries;
    }

    public void setClearEntries( final boolean clearEntries )
    {
        this.clearEntries = clearEntries;
    }

    public boolean isTickerSymbolEmbeddedWithParens()
    {
        return tickerSymbolEmbeddedWithParens;
    }

    public void setTickerSymbolEmbeddedWithParens( final boolean tickerSymbolEmbeddedWithParens )
    {
        this.tickerSymbolEmbeddedWithParens = tickerSymbolEmbeddedWithParens;
    }

    public int getTickerSymbolColumn()
    {
        return tickerSymbolColumn;
    }

    public void setTickerSymbolColumn( final int tickerSymbolColumn )
    {
        this.tickerSymbolColumn = tickerSymbolColumn;
    }

    public int getGainsColumn()
    {
        return gainsColumn;
    }

    public void setGainsColumn( final int gainsColumn )
    {
        this.gainsColumn = gainsColumn;
    }

    public int getLossColumn()
    {
        return lossColumn;
    }

    public void setLossColumn( final int lossColumn )
    {
        this.lossColumn = lossColumn;
    }

    public int getGainsLossColumn()
    {
        return gainsLossColumn;
    }

    public void setGainsLossColumn( final int gainsLossColumn )
    {
        this.gainsLossColumn = gainsLossColumn;
    }

    public int getSkipHeaderRows()
    {
        return skipHeaderRows;
    }

    public void setSkipHeaderRows( final int skipHeaderRows )
    {
        this.skipHeaderRows = skipHeaderRows;
    }

    public int getSkipFooterRows()
    {
        return skipFooterRows;
    }

    public void setSkipFooterRows( final int skipFooterRows )
    {
        this.skipFooterRows = skipFooterRows;
    }


    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "GainsLossesImportConfigurationDTO{" );
        sb.append( "skipHeaderRows=" ).append( skipHeaderRows );
        sb.append( ", skipFooterRows=" ).append( skipFooterRows);
        sb.append( ", tickerSymbolEmbeddedWithParens=" ).append( tickerSymbolEmbeddedWithParens );
        sb.append( ", tickerSymbolColumn=" ).append( tickerSymbolColumn );
        sb.append( ", gainsColumn=" ).append( gainsColumn );
        sb.append( ", lossColumn=" ).append( lossColumn );
        sb.append( ", gainsLossColumn=" ).append( gainsLossColumn );
        sb.append( ", clearEntries=" ).append( clearEntries );
        sb.append( '}' );
        return sb.toString();
    }

}
