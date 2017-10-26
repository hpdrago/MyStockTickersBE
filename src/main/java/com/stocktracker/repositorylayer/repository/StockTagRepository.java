package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This is the database repository for Stock Tags
 */
public interface StockTagRepository extends JpaRepository<StockTagEntity, Integer>
{
}
