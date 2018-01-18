package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TradeItAccount entity repository
 *
 * Created by mike on 12/4/2017.
 */
public interface LinkedAccountRepository extends JpaRepository<LinkedAccountEntity, Integer>
{
    /**
     * Gets the linked account by the primary key.
     * @param id
     * @return
     */
    LinkedAccountEntity findById( final int id );
}
