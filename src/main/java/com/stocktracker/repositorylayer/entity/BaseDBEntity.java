package com.stocktracker.repositorylayer.entity;

import org.springframework.beans.BeanUtils;

/**
 * Created by mike on 11/5/2016.
 */
public class BaseDBEntity<E,DE>
{
    /**
     * Default method that copies properties from the Domain Entity to the DB Entity
     * @param domainEntity
     * @param dbEntity
     */
    public void fromDomainEntityToDBEntity( DE domainEntity, E dbEntity )
    {
        BeanUtils.copyProperties( domainEntity, dbEntity );
    }

    /**
     * Default method to copy properties from the DB Entity to the Domain Entity
     * @param dbEntity
     * @param domainEntity
     */
    public void fromDBEntityToDomainEntity( E dbEntity, DE domainEntity )
    {
        BeanUtils.copyProperties( dbEntity, domainEntity );
    }
}
