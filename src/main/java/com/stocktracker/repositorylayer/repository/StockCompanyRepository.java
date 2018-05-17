package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional( readOnly = true )
public interface StockCompanyRepository extends JpaRepository<StockCompanyEntity, String>
{
    @Cacheable
    StockCompanyEntity findByTickerSymbol( final String tickerSymbol );

    @Cacheable("companyCache")
    Page<StockCompanyEntity> findByCompanyNameIsLikeOrTickerSymbolIsLike( Pageable pageRequest,
                                                                          String companyNameMatching,
                                                                          String tickerSymbolMatching );

    @Override
    @Transactional
    @Modifying
    StockCompanyEntity save( StockCompanyEntity stockCompanyEntity );

    @Override
    @Transactional
    @Modifying
    <S extends StockCompanyEntity> List<S> save( Iterable<S> iterable );

    @Override
    @Transactional
    @Modifying
    <S extends StockCompanyEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( String s );

    @Override
    @Transactional
    @Modifying
    void delete( StockCompanyEntity stockCompanyEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<StockCompanyEntity> iterable );
}
