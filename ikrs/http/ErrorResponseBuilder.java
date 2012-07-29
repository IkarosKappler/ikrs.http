package ikrs.http;

/**
 * @author Ikaros Kappler
 * @date 2012-07-29
 * @version 1.0.0
 **/

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.util.MissingResourceException;
import java.util.UUID;
import java.util.Map;
import java.util.logging.Level;

import ikrs.http.resource.ByteArrayResource;
import ikrs.http.response.GeneralPreparedResponse;
import ikrs.http.response.successful.OK;

import ikrs.typesystem.BasicType;
import ikrs.typesystem.BasicTypeException;

import ikrs.util.MIMEType;


public class ErrorResponseBuilder
    extends AbstractResponseBuilder
    implements ResponseBuilder {

    /**
     * The constructor.
     **/
    public ErrorResponseBuilder( HTTPHandler handler ) {
	super( handler );	
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
	

	BasicType statusCode   = additionals.get( "statusCode" );
	BasicType reasonPhrase = additionals.get( "reasonPhrase" );
	BasicType errorMessage = additionals.get( "errorMessage" );

	try {
	    

	    if( statusCode == null || reasonPhrase == null || errorMessage == null ) {

		this.getHTTPHandler().getLogger().log( Level.WARNING,
						       getClass().getName(),
						       "Cannot build error reply. The passed additionals miss some essential params (statusCode="+statusCode+", reasonPhrase="+reasonPhrase+", errorMessage="+errorMessage+"). Senging INTERNAL_SERVER_ERROR." 
						       );
		
		return buildPreparedErrorResponse( headers, 
						   socketID, 
						   socket, 
						   
						   Constants.HTTP_STATUS_SERVERERROR_INTERNAL_SERVER_ERROR,
						   "Internal Server Error",
						   "The system encountered a fatal error. Your request could not be processed."
						   );
	    } else {

		return buildPreparedErrorResponse( headers, 
						   socketID, 
						   socket, 
						   statusCode.getInt(),
						   reasonPhrase.getString(),
						   errorMessage.getString()
						   );

	    }

	} catch( BasicTypeException e ) {

	    this.getHTTPHandler().getLogger().log( Level.WARNING,
						       getClass().getName(),
						       "Cannot build error reply. The passed additionals miss some essential params (statusCode="+statusCode+", reasonPhrase="+reasonPhrase+", errorMessage="+errorMessage+"). Senging INTERNAL_SERVER_ERROR." 
						       );
		
	    return buildPreparedErrorResponse( headers, 
					       socketID, 
					       socket, 
					       
					       Constants.HTTP_STATUS_SERVERERROR_INTERNAL_SERVER_ERROR,
					       "Internal Server Error",
					       "The system encountered a fatal error. Your request could not be processed."
					       );

	}
	

    }
    //---END-------------------------------- ReplyBuilder implementaion -----------------------


    private PreparedHTTPResponse buildPreparedErrorResponse( HTTPHeaders headers, 
							     UUID socketID, 
							     Socket socket, 
							     int statusCode,
							     String reasonPhrase,
							     String errorMessage
							     ) {

	try {
	    GeneralPreparedResponse response = new GeneralPreparedResponse( this.getHTTPHandler(),
									    headers,
									    socketID,
									    socket,
									    
									    statusCode,
									    reasonPhrase
									    );

	    // Which resource to send: system file or raw text message?
	    Resource resource = null;
	    String errorFilePath = "/system/errors/Error." + statusCode + ".html";

	    try {
		// File exists?
 
		this.getHTTPHandler().getLogger().log( Level.FINE,
						       getClass().getName() + ".buildPreparedErrorResponse(...)",
						       "Trying to get error file resource '"+errorFilePath+"' from resource accessor."
						       );

		URI uri = new URI( errorFilePath );
		resource = this.getHTTPHandler().getResourceAccessor().locate( uri );

	    } catch( MissingResourceException e ) {
		
		// File not found -> use raw message
		this.getHTTPHandler().getLogger().log( Level.FINE,
						       getClass().getName() + ".buildPreparedErrorResponse(...)",
						       "Error file resource '"+errorFilePath+"' not found; using raw message."
						       );		
		resource = new ByteArrayResource( errorMessage.getBytes(), 
						  true // useFairLocks?
						  );
		// Overwrite the MIME type (the resource think's it is raw binary data)
		resource.getMetaData().setMIMEType( new MIMEType("text/plain") );
	    }

	    response.setResponseDataResource( resource );
	    response.prepare();
	    
	    return response;

	} catch( RuntimeException e ) {
	    
	    this.getHTTPHandler().getLogger().log( Level.WARNING,
						   getClass().getName() + ".buildPreparedErrorResponse(...)",
						   "Cannot build error reply (statusCode="+statusCode+", reasonPhrase="+reasonPhrase+"). RuntimeException: " + e.getMessage()
						   );  
	    return null;

	} catch( Exception e ) {
	    
	    this.getHTTPHandler().getLogger().log( Level.WARNING,
						   getClass().getName() + ".buildPreparedErrorResponse(...)",
						   "Cannot build error reply (statusCode="+statusCode+", reasonPhrase="+reasonPhrase+"). Exception: " + e.getMessage()
						   );  
	    return null;
	    
	}

    }

}
