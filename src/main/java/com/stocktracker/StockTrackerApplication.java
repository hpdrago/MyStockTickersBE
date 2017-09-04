package com.stocktracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.stocktracker.repositorylayer.repository"})
@EnableSpringDataWebSupport
@EnableScheduling
public class StockTrackerApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run( StockTrackerApplication.class, args );
    }
}
