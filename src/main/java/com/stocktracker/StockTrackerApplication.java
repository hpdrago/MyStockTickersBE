package com.stocktracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
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
/*
 * This is to avoid issues with DI of a Service that is transactional.
 */
@EnableAspectJAutoProxy( proxyTargetClass = true) // https://stackoverflow.com/questions/39483059/transactional-annotation-error
public class StockTrackerApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run( StockTrackerApplication.class, args );
    }
}
