package com.stocktracker;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA Configuration
 */
@Configuration
@EnableJpaRepositories( basePackages = {"com.stocktracker.repositorylayer.repository"} )
@EnableAspectJAutoProxy( proxyTargetClass = true )
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
