package com.stocktracker.repositorylayer;

import com.stocktracker.repositorylayer.db.entity.StockEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * Stock Entity Repository
 * Created by mike on 9/11/2016.
 */
public interface StockRepository extends PagingAndSortingRepository<StockEntity,String>,
                                         QueryByExampleExecutor<StockEntity>
{
    Page<StockEntity> findByCompanyNameIsLikeOrTickerSymbolIsLike( Pageable pageRequest,
                                                                   String companyNameMatching,
                                                                   String tickerSymbolMatching );
}
