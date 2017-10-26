package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.CustomerTagEntity;
import com.stocktracker.repositorylayer.entity.StockTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This is the database repository for Customer Tags
 */
public interface CustomerTagRepository extends JpaRepository<CustomerTagEntity, Integer>
{
}
