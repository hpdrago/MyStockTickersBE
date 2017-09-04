package com.stocktracker;

import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * JPA Configuration
 */
public class PersistenceJPAConfig
{
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
}
