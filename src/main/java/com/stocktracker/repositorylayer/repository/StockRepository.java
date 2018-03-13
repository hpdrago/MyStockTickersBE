package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * Stock Entity Repository
 * Created by mike on 9/11/2016.
 */
public interface StockRepository extends JpaRepository<StockEntity,String>,
                                         QueryByExampleExecutor<StockEntity>
{
    Page<StockEntity> findByCompanyNameIsLikeOrTickerSymbolIsLike( Pageable pageRequest,
                                                                   String companyNameMatching,
                                                                   String tickerSymbolMatching );
}
