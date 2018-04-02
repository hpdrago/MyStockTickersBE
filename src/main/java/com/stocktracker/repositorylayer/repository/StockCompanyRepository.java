package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockCompanyRepository extends JpaRepository<StockCompanyEntity,String>
{
    @Cacheable
    StockCompanyEntity findByTickerSymbol( final String tickerSymbol );

    @Cacheable
    Page<StockCompanyEntity> findByCompanyNameIsLikeOrTickerSymbolIsLike( Pageable pageRequest,
                                                                          String companyNameMatching,
                                                                          String tickerSymbolMatching );
}
