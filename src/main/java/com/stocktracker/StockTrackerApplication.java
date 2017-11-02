package com.stocktracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableSpringDataWebSupport
@EnableScheduling
/*
 * This is to avoid issues with DI of a Service that is transactional.
 * https://stackoverflow.com/questions/39483059/transactional-annotation-error
 */
public class StockTrackerApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run( StockTrackerApplication.class, args );
    }

    /**
     * https://drissamri.be/blog/java/enable-https-in-spring-boot/
     * @return
     */
    /*
    @Bean
    public EmbeddedServletContainerFactory servletContainer()
    {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory()
        {
            @Override
            protected void postProcessContext( Context context )
            {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint( "CONFIDENTIAL" );
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern( "/*" );
                securityConstraint.addCollection( collection );
                context.addConstraint( securityConstraint );
            }
        };

        tomcat.addAdditionalTomcatConnectors( initiateHttpConnector() );
        return tomcat;
    }

    private Connector initiateHttpConnector()
    {
        Connector connector = new Connector( "org.apache.coyote.http11.Http11NioProtocol" );
        connector.setScheme( "http" );
        connector.setPort( 8080 );
        connector.setSecure( false );
        connector.setRedirectPort( 8443 );

        return connector;
    }
    */
}
