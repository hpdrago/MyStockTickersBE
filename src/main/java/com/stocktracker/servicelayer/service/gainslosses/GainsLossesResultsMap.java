package com.stocktracker.servicelayer.service.gainslosses;

import io.reactivex.processors.AsyncProcessor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This map contains the last results of an import for a customer.
 */
public class GainsLossesResultsMap
{
    private Map<UUID,AsyncProcessor<String>> resultsMap = Collections.synchronizedMap( new HashMap<>( ) );

    /**
     * Creates an entry in the map for the customer.
     * @param customerUuid
     */
    public void createEntry( final UUID customerUuid )
    {
        final AsyncProcessor<String> asyncProcessor = AsyncProcessor.create();
        this.resultsMap
            .put( customerUuid, asyncProcessor );
    }

    /**
     * Remove the results from the map.
     * @param customerUuid
     */
    public void removeEntry( final UUID customerUuid )
    {
        this.resultsMap
            .remove( customerUuid );
    }

    /**
     * Adds the import results for a customer.
     * @param customerUuid
     * @param results
     */
    public void addResults( final UUID customerUuid, final String results )
    {
        final AsyncProcessor<String> asyncProcessor = this.getMapEntry( customerUuid );
        asyncProcessor.onNext( results );
        asyncProcessor.onComplete();
    }

    /**
     * Gets the results for the customer.  This method will block and wait until the results are completed.
     * @param customerUuid
     * @return
     */
    public String getResults( final UUID customerUuid )
    {
        final AsyncProcessor<String> mapEntry = this.getMapEntry( customerUuid );
        return mapEntry.getValue();
    }

    /**
     * Gets the map entry for the {@code customerUuid}.
     * @param customerUuid
     * @return AsyncProcess entry for the customer.
     * @throws IllegalArgumentException if the customerUuid is not found.
     */
    private AsyncProcessor<String> getMapEntry( final UUID customerUuid )
    {
        final AsyncProcessor<String> asyncProcessor = this.resultsMap
            .get( customerUuid );
        if ( asyncProcessor == null )
        {
            throw new IllegalArgumentException( String.format( "Customer uuid: %s is not in the map", customerUuid ));
        }
        return asyncProcessor;
    }
}
