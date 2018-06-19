package com.stocktracker.servicelayer.service;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.common.CustomerUuidContainer;
import com.stocktracker.repositorylayer.entity.UUIDEntity;
import com.stocktracker.weblayer.dto.common.CustomerIdContainer;
import com.stocktracker.weblayer.dto.common.UuidDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Service class for managing database entities with UUID as primary keys.
 *
 * @param <E> The database entity type.
 * @param <D> The DTO type.
 * @param <R> The entity repository type.
 */
public abstract class UuidEntityService<E extends UUIDEntity,
                                        D extends UuidDTO,
                                        R extends JpaRepository<E, UUID>>
    extends VersionedEntityService<UUID,E,String,D,R>
{
    /**
     * Retrieves the entity and converts it into a DTO.
     * @param uuid Converted to UUID UUID type.
     * @return
     * @throws VersionedEntityNotFoundException
     */
    public D getDTO( final String uuid )
        throws VersionedEntityNotFoundException
    {
        return super.getDTO( UUIDUtil.uuid( uuid ) );
    }

    /**
     * Get the entity by the primary key (uuid).
     * @param uuid Will be converted to UUID UUID.
     * @return
     * @throws VersionedEntityNotFoundException
     */
    public E getEntity( final String uuid )
        throws VersionedEntityNotFoundException
    {
        return super.getEntity( UUIDUtil.uuid( uuid ));
    }

    /**
     * Delete the entity by the primary key (uuid).
     * @param uuid Will be converted to UUID UUID.
     * @throws VersionedEntityNotFoundException
     */
    public void deleteEntity( final String uuid )
        throws VersionedEntityNotFoundException
    {
        super.deleteEntity( UUIDUtil.uuid( uuid ));
    }

    /**
     * Check if the entity exists by searching on the primary key (uuid).
     * @param uuid Will be converted to UUID UUID.
     * @return
     */
    public boolean isExists( final String uuid )
    {
        return super.isExists( UUIDUtil.uuid( uuid ));
    }

    /**
     * Overridden to set the {@code id} value from the entity's UUID value.
     * @param entity Contains the entity information.
     * @return
     */
    public D entityToDTO( final E entity )
    {
        //final String methodName = "cachedDataToDTO";
        //logMethodBegin( methodName, entity );
        final D dto = super.entityToDTO( entity );
        dto.setId( entity.getUuid().toString() );
        /*
         * Convert the customer uuid (binary) to a customer id (string)
         */
        if ( dto instanceof CustomerIdContainer &&
             entity instanceof CustomerUuidContainer )
        {
            if ( ((CustomerUuidContainer) entity).getCustomerUuid() != null )
            {
                ((CustomerIdContainer)dto).setCustomerId( ((CustomerUuidContainer) entity).getCustomerUuid().toString() );
            }
        }
        //logMethodEnd( methodName, entity );
        return dto;
    }

    /**
     * Overridden to set the uuid of the Entity from the id of the DTO.
     * @param dto of type <D>
     * @return
     */
    protected E dtoToEntity( final D dto )
    {
        final E entity = super.dtoToEntity( dto );
        /*
         * For new entities, the dto.id will not be populated.
         */
        if ( dto.getId() != null && dto.getId().length() > 0 )
        {
            entity.setUuid( UUIDUtil.uuid( dto.getId() ) );
        }
        /*
         * Convert the customer id (string) to a customer uuid (binary)
         */
        if ( entity instanceof CustomerUuidContainer &&
                dto instanceof CustomerIdContainer )
        {
            ((CustomerUuidContainer)entity).setCustomerUuid(
                UUIDUtil.uuid( ((CustomerIdContainer)dto).getCustomerId() ));
        }
        return entity;
    }
}
