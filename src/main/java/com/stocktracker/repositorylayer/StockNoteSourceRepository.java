package com.stocktracker.repositorylayer;

import com.stocktracker.repositorylayer.db.entity.StockNoteSourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mike on 5/7/2017.
 */
public interface StockNoteSourceRepository extends JpaRepository<StockNoteSourceEntity, Integer>
{
}
