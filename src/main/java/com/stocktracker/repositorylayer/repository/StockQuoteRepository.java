package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockQuoteRepository extends JpaRepository<StockQuoteEntity,String>
{
}
