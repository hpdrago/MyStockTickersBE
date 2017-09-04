package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockNoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mike on 5/7/2017.
 */
public interface StockNoteRepository extends JpaRepository<StockNoteEntity, Integer>
{
    List<StockNoteEntity> findByCustomerIdOrderByDateCreatedDesc( final int customerId );
}
