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
    /**
     * http://www.baeldung.com/spring-async
     * @return Thread pool executor
     */
    @Bean(name = "stockQuoteThreadPool")
    public Executor stockQuoteThreadPoolTaskExecutor()
    {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(5);
        pool.setMaxPoolSize(30);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        pool.setThreadNamePrefix( "StockQuote-" );
        return pool;
    }

    /**
     * http://www.baeldung.com/spring-async
     * @return Thread pool executor
     */
    @Bean(name = "stockPositionEvaluatorThreadPool")
    public Executor stockPositionEvaluatorTaskExecutor()
    {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(5);
        pool.setMaxPoolSize(30);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        pool.setThreadNamePrefix( "PositionEvaluator-" );
        return pool;
    }
}
