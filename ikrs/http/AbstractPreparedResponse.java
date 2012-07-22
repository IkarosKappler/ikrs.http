package ikrs.http;

/**
 * This interface is meant to wrap prepared HTTP reply objects.
 *
 * @author Ikaros Kappler
 * @date 2012-07-16
 * @version 1.0.0
 **/

import java.net.Socket;
import java.nio.charset.Charset;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.UUID;
import java.util.logging.Level;


/*import ikrs.http.MalformedRequestException;
import ikrs.http.Resource;
import ikrs.http.UnsupportedVersionException;
import ikrs.http.UnknownMethodException;
*/

public abstract class AbstractPreparedResponse
    implements PreparedHTTPResponse {

    
    /**
     * The top level HTTP handler.
     **/
    private HTTPHandler httpHandler;

    /**
     * The response's HTTP request (!) headers.
     **/
    private HTTPHeaders requestHeaders;

    /**
     * The response's socketID (server side).
     **/
    private UUID socketID;

    /**
     * The response's actual connection socket.
     **/
    private Socket socket;


    /**
     * This is an _internal_ field to buffer the actual response headers.
     **/
    private HTTPHeaders responseHeaders;

    
    private Resource responseDataResource;
    
    public AbstractPreparedResponse( HTTPHandler handler,
				     HTTPHeaders requestHeaders,
				     UUID socketID,
				     Socket socket ) {
	super();

	this.httpHandler = handler;
	this.requestHeaders = requestHeaders;
	this.socketID = socketID;
	this.socket = socket;

	this.responseHeaders = new HTTPHeaders();

    }

    /**
     * Get this response's HTTP handler.
     **/
    protected HTTPHandler getHTTPHandler() {
	return this.httpHandler;
    }

    /**
     * Get this respone's HTTP request headers.
     **/
    protected HTTPHeaders getRequestHeaders() {
	return this.requestHeaders;
    }

    /**
     * Get the server side socket ID.
     **/
    protected UUID getSocketID() {
	return this.socketID;
    }

    /**
     * Get the actual connection socket.
     **/
    protected Socket getSocket() {
	return this.socket;
    }

    /**
     * These headers are initially empty and need to be filled with the response header data.
     **/
    public HTTPHeaders getResponseHeaders() {
	return this.responseHeaders;
    }

    /**
     * Add a new line (key-value-pair) to the response headers.
     **/
    public void addResponseHeader( String key, String value ) {
	this.responseHeaders.add( key, value );
    }


    public void setResponseDataResource( Resource resource ) {
	this.responseDataResource = resource;
    }

    public Resource getResponseDataResource() {
	return this.responseDataResource;
    }


    //---BEGIN------------------------------ PreparedResponse implementation -----------------------------
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
    public abstract void prepare() 
	throws MalformedRequestException,
	       UnsupportedVersionException,
	       UnknownMethodException,
	       SecurityException,
	       IOException;
      
    /**
     * This method executes the prepared reply; this means that all necessary resources will be accessed,
     * the actual reply built and sent back to the client.
     *
     * @throws IOException If any IO errors occur.
     **/
    public final void execute()
	throws IOException {

	// Check if the response headers exist
	if( this.getResponseHeaders().size() == 0 ) {

	    this.httpHandler.getLogger().log( Level.SEVERE,
					      getClass().getName()+".execute()",
					      "Cannot send HTTP response: prepared headers are empty." );
	    throw new IOException( "Cannot send HTTP response: prepared headers are empty." );

	}

	
	OutputStream out = this.getSocket().getOutputStream();

	Charset charset = Charset.forName("UTF-8");
	
	// First: write headers
	Iterator<HTTPHeaderLine> iter = this.getResponseHeaders().iterator();
	// There is at least one line
	do {

	    HTTPHeaderLine line = iter.next();

	    System.out.println( "Sending: " + line.toString() );

	    byte[] b = line.getRawBytes( charset );
	    
	    // Write header line bytes to output stream
	    out.write( b );
	    

	} while( iter.hasNext() );
	
	// Send an empty line that implies 'end-of-headers'
	out.write( (byte)'\n' );

	// Flush the header data
	out.flush();


	// Send data? (resource MUST be locked!)
	try {
	    
	    // Resource must already be opened!
	    if( this.responseDataResource != null && this.responseDataResource.getLength() > 0 ) {
		
		
		//this.responseDataResource.open( true ); // Open in read-only mode

		InputStream resourceIn = this.responseDataResource.getInputStream();

		byte[] buffer = new byte[ 1024 ];
		int len = -1;
		// Read the resource's data chunk by chunk
		while( (len = resourceIn.read(buffer)) > 0 ) {
		    
		    // And write each chunck into the socket's output stream
		    out.write( buffer, 0, len );

		}
		
		// And flush data
		out.flush();
		
	    }

	} catch( IOException e ) {

	    // Catch this exception to be sure the socket was closed
	    this.httpHandler.getLogger().log( Level.SEVERE,
					      getClass().getName()+".execute()",
					      "Cannot send HTTP response (data). The passed resource threw an IOException: " + e.getMessage() );

	} finally {

	    out.close();

	}


	// Now send the binary data

    }


    /**
     * This method will be called in the final end - even if the execute() method failed.
     *
     * It has to clean up, release resources and all locks!
     **/
    public abstract void dispose();
    //---END-------------------------------- PreparedResponse implementation -----------------------------


}
