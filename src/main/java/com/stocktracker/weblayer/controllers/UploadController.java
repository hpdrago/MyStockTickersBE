package com.stocktracker.weblayer.controllers;


import com.stocktracker.servicelayer.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * file upload controller copied from:
 * http://javasampleapproach.com/spring-framework/spring-boot/angular-5-upload-get-multipartfile-to-from-spring-boot-server
 */
@Controller
@CrossOrigin
public class UploadController extends AbstractController
{
    private static final String CONTEXT_URL = "/upload";

    @Autowired
    private StorageService storageService;

    //private List<String> files = new ArrayList<String>();
    private Map<String,CustomerFile> customerFiles = Collections.synchronizedMap( new HashMap<>() );

    /**
     * File upload.
     * @param file
     * @return
     */
    /*
    @RequestMapping( value = CONTEXT_URL + "/file",
                     method=RequestMethod.POST,
                     consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                     produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleFileUpload( @RequestParam( "file" ) MultipartFile file )
    {
        final String methodName = "handleFileUpload";
        logMethodBegin( methodName, file.getOriginalFilename(), file.getSize() );
        String message = "";
        try
        {
            storageService.store( file );
            files.add( file.getOriginalFilename() );
            message = "You successfully uploaded " + file.getOriginalFilename() + "!";
            logMethodEnd( methodName, message );
            return ResponseEntity.status( HttpStatus.OK ).body( message );
        }
        catch( Exception e )
        {
            message = "FAIL to upload " + file.getOriginalFilename() + "!";
            return ResponseEntity.status( HttpStatus.EXPECTATION_FAILED ).body( message );
        }
    }*/

    /**
     * File upload.
     * @param files
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/customerId/{customerId}/file",
                     method=RequestMethod.POST,
                     consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                     produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> handleFileUpload( @PathVariable final String customerId,
                                                      @RequestParam final String importFormat,
                                                      @RequestParam final String importMode,
                                                      @RequestParam( "file" ) MultipartFile multipartFile )
    {
        final String methodName = "handleFileUpload";
        logMethodBegin( methodName, customerId );
        //for ( final MultipartFile multipartFile: files )
        //{
            logDebug( methodName, "{0} size: {0}", multipartFile.getOriginalFilename(), multipartFile.getSize() );
        //}
        //String message = "";
        try
        {
            MultipartFile firstFile = null;
            //for ( final MultipartFile multipartFile: files )
            //{
                //storageService.store( multipartFile );
                if ( firstFile == null )
                {
                    firstFile = multipartFile;
                }
                //files.( file.getOriginalFilename() );
            //}
            //storageService.store( file );
            //files.add( file.getOriginalFilename() );
            //message = "You successfully uploaded " + file.getOriginalFilename() + "!";
            //logMethodEnd( methodName, message );
            ByteArrayResource resource = new ByteArrayResource( firstFile.getBytes() );
            this.customerFiles.put( customerId, new CustomerFile( multipartFile.getOriginalFilename(), resource ) );
            return ResponseEntity.status( HttpStatus.OK ).body( resource );
        }
        catch( Exception e )
        {
            //message = "FAIL to upload " + file.getOriginalFilename() + "!";
            logError( methodName, e );
            return ResponseEntity.status( HttpStatus.EXPECTATION_FAILED ).body( null );
        }
    }

    /**
     * Get all files.
     * @param model
     * @return
     */
    /*
    @GetMapping( CONTEXT_URL + "/getallfiles" )
    public ResponseEntity<List<String>> getListFiles( final Model model )
    {
        final String methodName = "getListFiles";
        logMethodBegin( methodName, model );
        List<String> fileNames = files.stream()
                                      .map( fileName -> MvcUriComponentsBuilder
                                      .fromMethodName( UploadController.class, "getFile", fileName ).build().toString() )
                                      .collect( Collectors.toList() );
        logMethodEnd( methodName, fileNames );
        return ResponseEntity.ok().body( fileNames );
    }
    */

    /**
     * Get a single file.
     * @param filename
     * @return
     */
    @GetMapping( CONTEXT_URL + "/customerId/{customerId}/files/{filename:.+}" )
    @ResponseBody
    public ResponseEntity<Resource> getFile( @PathVariable final String customerId,
                                             @PathVariable final String filename )
    {
        final String methodName = "getFile";
        logMethodBegin( methodName, filename );
        final Resource file = storageService.loadFile( filename );
        logMethodEnd( methodName, file );
        return ResponseEntity.ok()
                             .header( HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file
                             .getFilename() + "\"" )
                             .body( file );
    }

    /**
     * Get last file
     * @param customerId
     * @return
     */
    @GetMapping( CONTEXT_URL + "/customerId/{customerId}/file" )
    @ResponseBody
    public ResponseEntity<Resource> getFile( @PathVariable final String customerId )
    {
        final String methodName = "getLastFile";
        logMethodBegin( methodName, customerId );
        final CustomerFile customerFile = this.customerFiles.get( customerId );
        final Resource file = customerFile.file;
        logMethodEnd( methodName, customerFile );
        return ResponseEntity.ok()
                             .header( HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file
                                 .getFilename() + "\"" )
                             .body( file );
    }

    private class CustomerFile
    {
        final ByteArrayResource file;
        final String fileName;

        private CustomerFile( final String fileName, final ByteArrayResource file )
        {
            this.fileName = fileName;
            this.file = file;
        }

        @Override
        public String toString()
        {
            return fileName + " " + file.getByteArray().length + " bytes";
        }
    }
}