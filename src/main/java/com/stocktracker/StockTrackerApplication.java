package com.stocktracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@EnableSpringDataWebSupport
public class StockTrackerApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run( StockTrackerApplication.class, args );
    }
}
