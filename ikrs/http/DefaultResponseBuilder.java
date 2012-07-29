package ikrs.http;

/**
 * @author Ikaros Kappler
 * @date 2012-07-16
 * @version 1.0.0
 **/

import java.io.IOException;
import java.net.Socket;
import java.util.MissingResourceException;
import java.util.UUID;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import ikrs.http.resource.ByteArrayResource;
import ikrs.http.response.GeneralPreparedResponse;
import ikrs.http.response.successful.OK;

import ikrs.typesystem.*;

public class DefaultResponseBuilder
    extends AbstractResponseBuilder
    implements ResponseBuilder {

    
    /**
     * For the case the request cannot be processed it will be forwarded to the internal
     * error response builder.
     **/
    private ErrorResponseBuilder errorResponseBuilder;


    /**
     * The constructor.
     **/
    public DefaultResponseBuilder( HTTPHandler handler ) {
	super( handler );	

	this.errorResponseBuilder = new ErrorResponseBuilder(handler);
    }


    //---BEGIN------------------------------ ResponseBuilder implementaion -----------------------
    /**
     * This method translates the given headers and socket into an executable 
     * PreparedResponse object.
     *
     * The method does not throw any exceptions as the error reporting is part of HTTP
     * itself.
     *
     * @param headers  The previously processed headers.
     * @param socketID The unique socket ID.
     * @param socket   The acutual socket.
      * @param additionals A map containing non-essential builder params. The expected 
     *                    map contents depends on the underlying implementation; some
     *                    builders even allow null-additionals.
     *
     * @return A new HTTPRequest built from the HTTP headers.
     *
     **/
    public PreparedHTTPResponse create( HTTPHeaders headers,
					UUID socketID,
					Socket socket,

					Map<String,BasicType> additionals
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
	    // This is a critical step that might raise lots of different errors.
	    // It is IMPORTANT that this method is called INSIDE the builder!
	    // (errors should be handled INSIDE the HTTP protocol)
	    response.prepare();

	    
	    return response;

	} catch( UnknownMethodException e ) {
	    
	    this.getHTTPHandler().getLogger().log( Level.INFO,
						   getClass().getName() + ".create(...)",
						   "UnknownMethodException: " + e.getMessage()
						   );  

	    // Return an apropriate error response	    
	    return buildPreparedErrorResponse( headers, socketID, socket, 
					       e,
					       Constants.HTTP_STATUS_CLIENTERROR_METHOD_NOT_ALLOWED,
					       "Method Not Allowed",
					       e.getMessage()
					       );	    
	    

	} catch( ConfigurationException e ) {
	    
	    this.getHTTPHandler().getLogger().log( Level.INFO,
						   getClass().getName() + ".create(...)",
						   "ConfigurationException: " + e.getMessage()
						   );

	    // Return an apropriate error response
	    return buildPreparedErrorResponse( headers, socketID, socket, 
					       e,
					       Constants.HTTP_STATUS_SERVERERROR_INTERNAL_SERVER_ERROR,
					       "Internal Server Error",
					       e.getMessage()
					       );

	} catch( UnsupportedVersionException e ) {
	    
	    this.getHTTPHandler().getLogger().log( Level.INFO,
						   getClass().getName() + ".create(...)",
						   "UnsupportedVersionException: " + e.getMessage()
						   ); 
 
	    // Return an apropriate error response	    
	    return buildPreparedErrorResponse( headers, socketID, socket, 
					       e,
					       Constants.HTTP_STATUS_SERVERERROR_HTTP_VERSION_NOT_SUPPORTED,
					       "HTTP Version Not Supported",
					       e.getMessage()
					       );

	} catch( SecurityException e ) {
	    
	    this.getHTTPHandler().getLogger().log( Level.INFO,
						   getClass().getName() + ".create(...)",
						   "MalformedRequestException: " + e.getMessage()
						   ); 

	    // Return an apropriate error response	    
	    return buildPreparedErrorResponse( headers, socketID, socket, 
					       e,
					       Constants.HTTP_STATUS_CLIENTERROR_FORBIDDEN,
					       "Forbidden",
					       e.getMessage()
					       );

	} catch( MalformedRequestException e ) {
	    
	    this.getHTTPHandler().getLogger().log( Level.INFO,
						   getClass().getName() + ".create(...)",
						   "MalformedRequestException: " + e.getMessage()
						   );  
	    
	    // Return an apropriate error response	    
	    return buildPreparedErrorResponse( headers, socketID, socket, 
					       e,
					       Constants.HTTP_STATUS_CLIENTERROR_BAD_REQUEST,
					       "Bad Request",
					       e.getMessage()
					       );

	} catch( MissingResourceException e ) {
	    
	    this.getHTTPHandler().getLogger().log( Level.INFO,
						   getClass().getName() + ".create(...)",
						   "MissingResourceException: " + e.getMessage()
						   );  
	    
	    
	    // Return an apropriate error response	    
	    return buildPreparedErrorResponse( headers, socketID, socket, 
					       e,
					       Constants.HTTP_STATUS_CLIENTERROR_NOT_FOUND,
					       "Not Found",
					       e.getMessage()
					       );	

	} catch( IOException e ) {
	    
	    this.getHTTPHandler().getLogger().log( Level.INFO,
						   getClass().getName() + ".create(...)",
						   "IOException: " + e.getMessage()
						   ); 
	    
		// Return an apropriate error response	    
	    return buildPreparedErrorResponse( headers, socketID, socket, 
					       e,
					       Constants.HTTP_STATUS_SERVERERROR_INTERNAL_SERVER_ERROR, 
					       "Interal Server Error",
					       e.getMessage()
					       );	
	}

    }
    //---END-------------------------------- ReplyBuilder implementaion -----------------------


    private PreparedHTTPResponse buildPreparedErrorResponse( HTTPHeaders headers,
							     UUID socketID,
							     Socket socket,
							     Exception e,
							     int statusCode,
							     String reasonPhrase,
							     String errorMessage 
							     ) {

	Map<String,BasicType> additionals = new TreeMap<String,BasicType>();
	additionals.put( "statusCode",    new BasicNumberType(statusCode) );
	additionals.put( "reasonPhrase",  new BasicStringType(reasonPhrase) );
	additionals.put( "errorMessage",  new BasicStringType(errorMessage) );
	return this.errorResponseBuilder.create( headers,
						 socketID,
						 socket, 
						 additionals );
    }

}
