package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.VersionedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface VersionedEntityRepository<K extends Serializable,E extends VersionedEntity<K>> extends JpaRepository<E, K>
{
    E findByCustomerIdAndId( final int customerId, final K id );
}
