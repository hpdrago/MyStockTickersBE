package com.stocktracker.common;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class encapsulates the comparison of two Collections of type <T>.  The {@code SetComparatorResults} return value
 * contains three sets: new items, deleted items, and matching items.  Each of these sets can be empty sets.
 * @param <T>
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class SetComparator<T>
{
    /**
     * Compares the to sets and returns {@code SetComparatorResults} which contains the differences between
     * {@code collection1} and {@code collection2}.
     * @param collection1
     * @param collection2
     * @return {@code SetComparatorResults} which includes new, deleted, and matching items.
     */
    public SetComparatorResults compareSets( final Collection<T> collection1, final Collection<T> collection2 )
    {
        SetComparatorResults results = new SetComparatorResults();
        TreeSet<T> set1 = new TreeSet<>( collection1 );
        TreeSet<T> set2 = new TreeSet<>( collection2 );
        results.newItems = Sets.difference( set1, set2 );
        results.deletedItems = Sets.difference( set2, set1 );
        results.matchingItems = Sets.intersection( set1, set2 );
        return results;
    }

    /**
     * This class contains the results of the set comparison.
     * It contains new items, deleted items, and matching items which can be retrieved through the getter methods.
     */
    public class SetComparatorResults
    {
        private Set<T> newItems;
        private Set<T> deletedItems;
        private Set<T> matchingItems;

        @Override
        public String toString()
        {
            final StringBuilder sb = new StringBuilder( "SetComparatorResults{" );
            sb.append( "newItems=" ).append( newItems );
            sb.append( ", deletedItems=" ).append( deletedItems );
            sb.append( ", matchingItems=" ).append( matchingItems );
            sb.append( '}' );
            return sb.toString();
        }

        /**
         * Get the items that are in {@code set2} but not in {@set 1}.
         * @return
         */
        public Set<T> getNewItems()
        {
            return newItems;
        }

        /**
         * Get the items that are in {@code set1} but not in {@set 2}.
         * @return
         */
        public Set<T> getDeletedItems()
        {
            return deletedItems;
        }

        /**
         * Get the items that are in both sets.
         * @return
         */
        public Set<T> getMatchingItems()
        {
            return matchingItems;
        }
    }
}
