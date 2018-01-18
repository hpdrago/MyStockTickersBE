package com.stocktracker.repositorylayer.collection;

import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LinkedAccountEntities
{
    private Map<String, LinkedAccountEntity> entityMap = new HashMap<>();

    public LinkedAccountEntities()
    {
    }

    /**
     * Creates a new instance containing the contents of {@code collection}
     * @param collection
     */
    public LinkedAccountEntities( final Collection<LinkedAccountEntity> collection )
    {
        Objects.requireNonNull( collection, "collection cannot be null" );
        collection.stream().forEach( this::addAccount );
    }

    /**
     * Adds an account to the collection.
     * @param linkedAccountEntity
     */
    public void addAccount( final LinkedAccountEntity linkedAccountEntity )
    {
        Objects.requireNonNull( linkedAccountEntity, "linkedAccountEntity cannot be null" );
        this.entityMap.put( linkedAccountEntity.getAccountNumber(), linkedAccountEntity );
    }

    /**
     * Get the account by the account number.
     * @param accountNumber
     * @return null if not found.
     */
    public LinkedAccountEntity getAccount( final String accountNumber )
    {
        Objects.requireNonNull( accountNumber, "accountNumber cannot be null" );
        return this.entityMap.get( accountNumber );
    }
}
