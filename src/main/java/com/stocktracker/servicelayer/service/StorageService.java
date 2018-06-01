package com.stocktracker.servicelayer.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Service to handle file storage.
 *
 * http://javasampleapproach.com/spring-framework/spring-boot/angular-5-upload-get-multipartfile-to-from-spring-boot-server
 */
@Service
public class StorageService extends BaseService
{
    private final Path rootLocation = Paths.get( "file-upload" );

    /**
     * Stores the file.
     * @param file
     */
    public void store( MultipartFile file )
    {
        final String methodName = "store";
        logMethodBegin( methodName, file );
        logDebug( methodName, "rootLocation {0}", rootLocation );
        try
        {
            Files.copy( file.getInputStream(), this.rootLocation.resolve( file.getOriginalFilename() ) );
            logMethodEnd( methodName );
        }
        catch( Exception e )
        {
            throw new RuntimeException( "FAIL!", e );
        }
    }

    /**
     * Loads a file
     * @param filename
     * @return
     */
    public Resource loadFile( String filename )
    {
        final String methodName = "loadFile";
        logMethodBegin( methodName, filename );
        try
        {
            Path file = rootLocation.resolve( filename );
            Resource resource = new UrlResource( file.toUri() );
            if ( resource.exists() || resource.isReadable() )
            {
                logMethodEnd( methodName, resource );
                return resource;
            }
            else
            {
                throw new RuntimeException( "FAIL!" );
            }
        }
        catch( MalformedURLException e )
        {
            throw new RuntimeException( "FAIL!", e );
        }
    }

    public void deleteAll()
    {
        FileSystemUtils.deleteRecursively( rootLocation.toFile() );
    }

    public void init()
    {
        try
        {
            Files.createDirectory( rootLocation );
        }
        catch( IOException e )
        {
            throw new RuntimeException( "Could not initialize storage!", e );
        }
    }
}
