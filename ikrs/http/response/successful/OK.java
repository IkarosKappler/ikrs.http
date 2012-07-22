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
import java.util.UUID;
import java.util.logging.Level;

import ikrs.http.AbstractPreparedResponse;
import ikrs.http.Constants;
import ikrs.http.HTTPHandler;
import ikrs.http.HTTPHeaders;
import ikrs.http.MalformedRequestException;
import ikrs.http.Resource;
import ikrs.http.UnsupportedVersionException;
import ikrs.http.UnknownMethodException;
import ikrs.http.resource.ByteArrayResource;

public class OK
    extends AbstractPreparedResponse {


    public OK( HTTPHandler handler,
	       HTTPHeaders headers,
	       UUID socketID,
	       Socket socket ) {
	super( handler, headers, socketID, socket );
    }
    

    //---BEGIN------------------------- AbstractPreparedResponse implementation ------------------------------
    /**
     * This method must be implemented by all subclasses. It must prepare the HTTP response.
     * This means that all required ressources must be acquired (use locks), all headers prepared (by the use
     * of addResponseHeader(String,String) or getResponseHeaders()) and perform all necessary security checks.
     *
     * @throws MalformRequestException If the passed HTTP request headers are malformed and cannot be processed.
     * @throws UnsupportedVersionException If the headers' HTTP version is not supported (supported versions are
     *                                     1.0 and 1.1).
     * @throws UnknownMethodException If the headers' method (from the request line) is unknown.
     * @throws SecurityException If the request cannot be processed due to security reasons.
     * @throws IOException If any IO errors occur.
     **/
    public void prepare() 
	throws MalformedRequestException,
	       UnsupportedVersionException,
	       UnknownMethodException,
	       SecurityException,
	       IOException {


	// This must be handled via a ResourceAccessor later!
	String fakeContent = "Thank you.\nCome back soon.";
	byte[] data = fakeContent.getBytes();

	// Locate resource
	Resource resource = new ByteArrayResource( data, 0, data.length, true );

	resource.getReadLock().lock();
	resource.open( true ); // Open in read-only mode 

       
	super.addResponseHeader( "HTTP/1.1 "+Constants.HTTP_STATUS_SUCCESSFUL_OK+" OK", null );
	
	super.addResponseHeader( "Server",            "Apache/1.3.29 (Unix) PHP/4.3.4" );
	super.addResponseHeader( "Content-Length",    Long.toString(resource.getLength()) ); // Integer.toString(fakeReply.length()) );
	super.addResponseHeader( "Content-Language",  "en" );
	super.addResponseHeader( "Connection",        "close" );
	super.addResponseHeader( "Content-Type",      "text/plain" );

	

	super.setResponseDataResource( resource );
	

    }


    /**
     * This method will be called in the final end - even if the execute() method failed.
     *
     * It has to clean up, release resources and all locks!
     **/
    public void dispose() {

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
