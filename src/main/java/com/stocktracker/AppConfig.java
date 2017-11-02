package com.stocktracker;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAutoConfiguration( exclude={DataSourceAutoConfiguration.class})
public class AppConfig
{
    /**
     * https://www.mkyong.com/spring/spring-and-java-thread-example/
     * @return
     */
    @Bean
    public ThreadPoolTaskExecutor taskExecutor()
    {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(5);
        pool.setMaxPoolSize(30);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        return pool;
    }

}
