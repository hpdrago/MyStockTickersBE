package com.stocktracker.repositorylayer;

import com.stocktracker.repositorylayer.db.entity.VStockNoteCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mike on 5/7/2017.
 */
public interface VStockNoteCountRepository extends JpaRepository<VStockNoteCountEntity, Integer>
{
    /**
     * Get the list of ticker symbols and the number of notes for a single customer.
     * @param customerId The customer's id
     * @return List of entities
     */
    List<VStockNoteCountEntity> findByCustomerId( final int customerId );
}
