package com.stocktracker;

import com.stocktracker.servicelayer.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
@EnableTransactionManagement
@EnableSpringDataWebSupport
@EnableScheduling
/*
 * This is to avoid issues with DI of a Service that is transactional.
 * https://stackoverflow.com/questions/39483059/transactional-annotation-error
 */
public class StockTrackerApplication implements CommandLineRunner
{
    @Resource
    private StorageService storageService;

    public static void main( String[] args )
    {
        final Path filesToDelete = Paths.get( "./logs" );
        try
        {
            //System.out.print( "Current dir: " + Paths.get( "" ).toAbsolutePath().toString() );
            //System.exit( 1 );
            Files.walk( filesToDelete )
                 .map( Path::toFile )
                 .forEach( file -> file.delete() );
                Files.deleteIfExists( filesToDelete );
        }
        catch( IOException e )
        {
            // don't care if this fails.
            //e.printStackTrace();
            //System.exit( 1 );
        }
        SpringApplication.run( StockTrackerApplication.class, args );
    }

    @Override
    public void run(String... arg) throws Exception
    {
        storageService.deleteAll();
        storageService.init();
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
