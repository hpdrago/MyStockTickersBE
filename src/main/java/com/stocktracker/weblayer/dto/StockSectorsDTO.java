package com.stocktracker.weblayer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the Stock Sector information.
 * The section information is organized hierarchically with a list of sectors that can possibly have a
 * list of sub sectors.
 *
 * Created by mike on 11/19/2016.
 */
public class StockSectorsDTO
{
    @JsonProperty
    private List<StockSector> stockSectors = new ArrayList<>();

    /**
     * Create a new instance from the list of stock sector information
     *
     * @param stockSectorDEList
     * @param stockSubSectorDEList
     * @return new StockSectorsDTO
     */
    /*
    public static StockSectorsDTO newInstance( final List<StockSectorDE> stockSectorDEList,
                                               final List<StockSubSectorDE> stockSubSectorDEList )
    {
        return new StockSectorsDTO( stockSectorDEList, stockSubSectorDEList );
    }
    */

    /**
     * Create a new instance from the list of stock sector information
     *
//     * @param stockSectorDEList
//     * @param stockSubSectorDEList
     */
//    private StockSectorsDTO( final List<StockSectorDE> stockSectorDEList,
//                             final List<StockSubSectorDE> stockSubSectorDEList )
//    {
//        /*
//         * Load the sectors first
//         */
//        stockSectorDEList.forEach( ( stockSectorDE ) -> stockSectors.add( new StockSector( stockSectorDE ) ) );
//
//        /*
//         * Now go through the list and add the sub sectors to the sector
//         */
//        for ( StockSubSectorDE stockSubSectorDE : stockSubSectorDEList )
//        {
//            this.stockSectors
//                .stream()
//                .filter( stockSector -> stockSector.sectorId == stockSubSectorDE.getSectorId() )
//                .findAny()
//                .orElseThrow( () -> new IllegalArgumentException( String.format( "Cannot find sector " + stockSubSectorDE ) ) )
//                .addSubSector( stockSubSectorDE );
//        }
//    }


    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockSectorsDTO{" );
        sb.append( "stockSectors=" ).append( stockSectors );
        sb.append( '}' );
        return sb.toString();
    }

    /**
     * This is a private class used to create the hierarchical sector/sub sector structure
     */
    private class StockSector
    {
        @JsonProperty
        private int sectorId;
        @JsonProperty
        private String sectorName;
        @JsonProperty
        private List<StockSector> subSectors;

        /*
        public StockSector( final StockSectorDTO stockSectorDE )
        {
            this.sectorId = stockSectorDE.getId();
            this.sectorName = stockSectorDE.getSector();
        }

        public StockSector( final Integer sectorId, final String subSectorName )
        {
            this.sectorId = sectorId;
            this.sectorName = subSectorName;
        }

        public void addSubSector( final StockSubSectorDE stockSubSectorDE )
        {
            if ( subSectors == null )
            {
                subSectors = new ArrayList<>();
            }
            subSectors.add( new StockSector( stockSubSectorDE.getSubSectorId(), stockSubSectorDE.getSubSector() ) );
        }

        @Override
        public String toString()
        {
            final StringBuilder sb = new StringBuilder( "StockSector{" );
            sb.append( "sectorId=" ).append( sectorId );
            sb.append( ", sectorName='" ).append( sectorName ).append( '\'' );
            sb.append( '}' );
            return sb.toString();
        }

        @Override
        public boolean equals( final Object o )
        {
            if ( this == o )
            {
                return true;
            }
            if ( o == null || getClass() != o.getClass() )
            {
                return false;
            }
            final StockSector that = (StockSector) o;
            return sectorId == that.sectorId;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash( sectorId );
        }
        */
    }
}
