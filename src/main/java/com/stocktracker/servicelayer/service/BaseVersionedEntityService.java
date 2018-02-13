package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.repositorylayer.entity.VersionedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public abstract class BaseVersionedEntityService<E extends VersionedEntity,
                                                 K extends Serializable,
                                                 D extends VersionedEntity,
                                                 R extends JpaRepository<E,K>>
    extends BaseEntityService<E,K,D,R>
{
}
