package com.stocktracker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AppConfig
{
    public static final String STOCK_PRICE_QUOTE_THREAD_POOL = "StockPriceQuoteThreadPool";
    public static final String STOCK_QUOTE_THREAD_POOL = "StockQuoteThreadPool";
    public static final String STOCK_COMPANY_THREAD_POOL = "StockCompanyThreadPool";
    public static final String STOCK_POSITION_EVALUATOR_THREAD_POOL = "StockPositionEvaluatorThreadPool";
    public static final String LINKED_ACCOUNT_GET_OVERVIEW_THREAD_POOL = "LinkedAccountGetOverviewThreadPool";

    /**
     * http://www.baeldung.com/spring-async
     * @return Thread pool executor
     */
    @Bean(name = STOCK_COMPANY_THREAD_POOL)
    public Executor stockCompanyThreadPoolTaskExecutor()
    {
        ThreadPoolTaskExecutor pool = getThreadPoolTaskExecutor( STOCK_COMPANY_THREAD_POOL + "-" );
        return pool;
    }

    /**
     * http://www.baeldung.com/spring-async
     * @return Thread pool executor
     */
    @Bean(name = STOCK_QUOTE_THREAD_POOL)
    public Executor stockQuoteThreadPoolTaskExecutor()
    {
        ThreadPoolTaskExecutor pool = getThreadPoolTaskExecutor( STOCK_QUOTE_THREAD_POOL + "-" );
        return pool;
    }

    /**
     * http://www.baeldung.com/spring-async
     * @return Thread pool executor
     */
    @Bean(name = STOCK_PRICE_QUOTE_THREAD_POOL)
    public Executor stockPriceQuoteThreadPoolTaskExecutor()
    {
        ThreadPoolTaskExecutor pool = getThreadPoolTaskExecutor( STOCK_PRICE_QUOTE_THREAD_POOL + "-" );
        return pool;
    }

    /**
     * http://www.baeldung.com/spring-async
     * @return Thread pool executor
     */
    @Bean(name = STOCK_POSITION_EVALUATOR_THREAD_POOL)
    public Executor stockPositionEvaluatorTaskExecutor()
    {
        ThreadPoolTaskExecutor pool = getThreadPoolTaskExecutor( STOCK_POSITION_EVALUATOR_THREAD_POOL );
        return pool;
    }

    /**
     * http://www.baeldung.com/spring-async
     * @return Thread pool executor
     */
    @Bean(name = LINKED_ACCOUNT_GET_OVERVIEW_THREAD_POOL)
    public Executor linkedAccountGetOverviewTaskExecutor()
    {
        ThreadPoolTaskExecutor pool = getThreadPoolTaskExecutor( LINKED_ACCOUNT_GET_OVERVIEW_THREAD_POOL );
        return pool;
    }

    private ThreadPoolTaskExecutor getThreadPoolTaskExecutor( final String threadPoolPrefix )
    {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize( 5 );
        pool.setMaxPoolSize( 30 );
        pool.setWaitForTasksToCompleteOnShutdown( true );
        pool.setThreadNamePrefix( threadPoolPrefix );
        return pool;
    }
}
