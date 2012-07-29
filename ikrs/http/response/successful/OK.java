package ikrs.http.response.successful;

/**
 * This interface is meant to wrap prepared HTTP reply objects.
 *
 * @author Ikaros Kappler
 * @date 2012-07-16
 * @version 1.0.0
 **/

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.MissingResourceException;
import java.util.UUID;
import java.util.logging.Level;

import ikrs.http.AbstractPreparedResponse;
import ikrs.http.ConfigurationException;
import ikrs.http.Constants;
import ikrs.http.HTTPHandler;
import ikrs.http.HTTPHeaders;
import ikrs.http.MalformedRequestException;
import ikrs.http.Resource;
import ikrs.http.UnsupportedVersionException;
import ikrs.http.UnknownMethodException;
import ikrs.http.resource.ByteArrayResource;
import ikrs.http.response.GeneralPreparedResponse;


import ikrs.typesystem.BasicTypeException;


public class OK
    extends GeneralPreparedResponse {



    public OK( HTTPHandler handler,
	       HTTPHeaders headers,
	       UUID socketID,
	       Socket socket ) {
	super( handler, headers, socketID, socket, Constants.HTTP_STATUS_SUCCESSFUL_OK, "OK" );

	    

    }
    

    //---BEGIN------------------------- AbstractPreparedResponse implementation ------------------------------
    /**
     * This method must be implemented by all subclasses. It must prepare the HTTP response.
     * This means that all required ressources must be acquired (use locks), all headers prepared (by the use
     * of addResponseHeader(String,String) or getResponseHeaders()) and perform all necessary security checks.
     *
     * Subclasses implementing this method should call the setPrepared() method when ready.
     *
     * @throws MalformRequestException If the passed HTTP request headers are malformed and cannot be processed.
     * @throws UnsupportedVersionException If the headers' HTTP version is not supported (supported versions are
     *                                     1.0 and 1.1).
     * @throws UnknownMethodException If the headers' method (from the request line) is unknown.
     * @throws ConfigurationException If the was a server configuration issue the server cannot work properly with.
     * @throws MissingResourceException If the requested resource cannot be found.
     * @throws SecurityException If the request cannot be processed due to security reasons.
     * @throws IOException If any IO errors occur.
     **/
    public void prepare() 
	throws MalformedRequestException,
	       UnsupportedVersionException,
	       UnknownMethodException,
	       ConfigurationException,
	       MissingResourceException,
	       SecurityException,
	       IOException {


	// Fetch the request URI
	String requestURI = this.getRequestHeaders().getRequestURI();
	if( requestURI == null )
	    throw new MalformedRequestException( "Malformed request line. URI missing." );


	try {
	    
	    URI uri = new URI( requestURI ); 
	    Resource resource = this.getHTTPHandler().getResourceAccessor().locate( uri );


	    resource.getReadLock().lock();
	    resource.open( true ); // Open in read-only mode 

       
	    // super.addResponseHeader( "HTTP/1.1 "+this.getStatusCode()+" OK", null );
	
	    super.addResponseHeader( "Server",            this.getHTTPHandler().getServerName() ); //"Apache/1.3.29 (Unix) PHP/4.3.4" );
	    super.addResponseHeader( "Content-Length",    Long.toString(resource.getLength()) ); // Integer.toString(fakeReply.length()) );
	    super.addResponseHeader( "Content-Language",  "en" );
	    super.addResponseHeader( "Connection",        "close" );
	    super.addResponseHeader( "Content-Type",      resource.getMetaData().getMIMEType().getContentType() ); //"text/plain" );

	

	    super.setResponseDataResource( resource );

	} catch( URISyntaxException e ) {

	    throw new MalformedRequestException( "Malformed request URI '"+requestURI+"': " + e.getMessage() );

	} catch( BasicTypeException e ) {

	    throw new ConfigurationException( e.getMessage() );

	}
    }


    /**
     * This method will be called in the final end - even if the execute() method failed.
     *
     * Subclasses implementing this method should call the setDisposed() method when done.
     *
     *
     * It has to clean up, release resources and all locks!
     **/
    public void dispose() {

	if( this.getResponseDataResource() == null )
	    return; // There is nothing to release


	try {
	    
	    // Close the resource.
	    this.getResponseDataResource().close();

	} catch( IOException e ) {

	    this.getHTTPHandler().getLogger().log( Level.WARNING,
						   getClass().getName() + ".dispose()",
						   "Faile to close resource: " + e.getMessage() );

	} finally {

	    // Release the read-lock
	    this.getResponseDataResource().getReadLock().unlock();

	}

	

    }
    //---END--------------------------- AbstractPreparedResponse implementation ------------------------------



}
