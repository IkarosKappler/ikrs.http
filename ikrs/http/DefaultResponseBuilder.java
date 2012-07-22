package ikrs.http;

/**
 * @author Ikaros Kappler
 * @date 2012-07-16
 * @version 1.0.0
 **/

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.logging.Level;


import ikrs.http.response.successful.OK;


public class DefaultResponseBuilder
    extends AbstractResponseBuilder
    implements ResponseBuilder {

    /**
     * The constructor.
     **/
    public DefaultResponseBuilder( HTTPHandler handler ) {
	super( handler );	
    }


    //---BEGIN------------------------------ ReplyBuilder implementaion -----------------------
    /**
     * This method translates the given headers and socket into an executable 
     * PreparedResponse object.
     *
     * The method does not throw any exceptions as the error reporting is part of HTTP
     * itself.
     *
     * @param headers  The previously processed headers.
     * @param socketID The unique socket ID.
     * @param socket   The acutual socket;
     *
     * @return A new HTTPRequest built from the HTTP headers.
     *
     **/
    public PreparedHTTPResponse create( HTTPHeaders headers,
					UUID socketID,
					Socket socket
					) {
	

	String command  = headers.getRequestMethod();
	String protocol = headers.getRequestProtocol();
	String version  = headers.getRequestVersion();
	String uri      = headers.getRequestURI();

	// Validate header data
	// ...


	try {

	    PreparedHTTPResponse response = new OK( this.getHTTPHandler(),
						    headers,
						    socketID,
						    socket );
	    // This is a critical steop that might raise lots of different errors
	    response.prepare();

	    
	    return response;

	} catch( UnknownMethodException e ) {
	    
	    this.getHTTPHandler().getLogger().log( Level.INFO,
						   getClass().getName() + ".create(...)",
						   "UnknownMethodException: " + e.getMessage()
						   );  
	    // This must be replaced by an appropriate error response object later!
	    return null;

	} catch( UnsupportedVersionException e ) {
	    
	    this.getHTTPHandler().getLogger().log( Level.INFO,
						   getClass().getName() + ".create(...)",
						   "UnsupportedVersionException: " + e.getMessage()
						   );  
	    // This must be replaced by an appropriate error response object later!
	    return null;

	} catch( SecurityException e ) {
	    
	    this.getHTTPHandler().getLogger().log( Level.INFO,
						   getClass().getName() + ".create(...)",
						   "MalformedRequestException: " + e.getMessage()
						   );  
	    // This must be replaced by an appropriate error response object later!
	    return null;

	} catch( MalformedRequestException e ) {
	    
	    this.getHTTPHandler().getLogger().log( Level.INFO,
						   getClass().getName() + ".create(...)",
						   "MalformedRequestException: " + e.getMessage()
						   );  
	    
	    // This must be replaced by an appropriate error response object later!
	    return null;
	} catch( IOException e ) {
	    
	    this.getHTTPHandler().getLogger().log( Level.INFO,
						   getClass().getName() + ".create(...)",
						   "IOException: " + e.getMessage()
						   );  
	    // This must be replaced by an appropriate error response object later!
	    return null;
	}

    }
    //---END-------------------------------- ReplyBuilder implementaion -----------------------


}
