package com.stocktracker;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA Configuration
 */
@Configuration
@EnableJpaRepositories( basePackages = {"com.stocktracker.repositorylayer.repository"} )
@EnableAspectJAutoProxy( proxyTargetClass = true )
@EnableTransactionManagement
@EnableRetry
public class PersistenceJPAConfig
{
    /*
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean()
    {
        return null;
    }

    @Bean
    public PlatformTransactionManager transactionManager()
    {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory( entityManagerFactoryBean().getObject() );
        return transactionManager;
    }
    */
}
