package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.VStockTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VStockTagRepository extends JpaRepository<VStockTagEntity, Integer>
{
}
