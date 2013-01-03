package ikrs.httpd;

/**
 * @author Ikaros Kappler
 * @date 2012-07-29
 * @version 1.0.0
 **/

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.util.List;
import java.util.MissingResourceException;
import java.util.UUID;
import java.util.Map;
import java.util.logging.Level;

import ikrs.httpd.resource.ByteArrayResource;
import ikrs.httpd.response.GeneralPreparedResponse;
import ikrs.httpd.response.successful.OK;

import ikrs.typesystem.BasicStringType;
import ikrs.typesystem.BasicType;
import ikrs.typesystem.BasicTypeException;

import ikrs.util.MIMEType;
//import ikrs.yuccasrv.ConnectionUserID;


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
     * @param postData The actual sent non-header data (if available; may be null).
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
					PostDataWrapper postData,
					UUID socketID,
					Socket socket,
					UUID sessionID,   // ConnectionUserID userID,
					Map<String,BasicType> additionals
					) {

	this.getHTTPHandler().getLogger().log( Level.INFO,
					       getClass().getName(),
					       "socketID=" + socketID + ", sessionID=" + sessionID + ", additionalSettings=" + additionals 
					       );
	

	BasicType statusCode   = additionals.get( "statusCode" );
	BasicType reasonPhrase = additionals.get( "reasonPhrase" );
	BasicType errorMessage = additionals.get( "errorMessage" );

	try {
	    

	    if( statusCode == null || reasonPhrase == null || errorMessage == null ) {

		this.getHTTPHandler().getLogger().log( Level.WARNING,
						       getClass().getName(),
						       "Cannot build error reply. The passed additionals miss some essential params (statusCode="+statusCode+", reasonPhrase="+reasonPhrase+", errorMessage="+errorMessage+"). Senging INTERNAL_SERVER_ERROR. additionals=" + additionals.toString() 
						       );

	
		return buildPreparedErrorResponse( headers, 
						   socketID, 
						   socket, 
						   sessionID, // userID,
						   
						   Constants.HTTP_STATUS_SERVERERROR_INTERNAL_SERVER_ERROR,
						   "Internal Server Error",
						   "The system encountered a fatal error. Your request could not be processed.",

						   additionals
						   );
	    } else {

		return buildPreparedErrorResponse( headers, 
						   socketID, 
						   socket, 
						   sessionID,   // userID,

						   statusCode.getInt(),
						   reasonPhrase.getString(),
						   errorMessage.getString(),

						   additionals
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
					       sessionID, // userID,
					       
					       Constants.HTTP_STATUS_SERVERERROR_INTERNAL_SERVER_ERROR,
					       "Internal Server Error",
					       "The system encountered a fatal error. Your request could not be processed.",

					       additionals
					       );

	}
	

    }
    //---END-------------------------------- ReplyBuilder implementaion -----------------------


    protected PreparedHTTPResponse buildPreparedErrorResponse( HTTPHeaders headers, 
							       UUID socketID, 
							       Socket socket, 
							       UUID sessionID,   // ConnectionUserID userID,
							       int statusCode,
							       String reasonPhrase,
							       String errorMessage,
							       
							       Map<String,BasicType> additionalSettings
							       ) {

	try {
	    GeneralPreparedResponse response = new GeneralPreparedResponse( this.getHTTPHandler(),
									    headers,
									    socketID,
									    socket,
									    sessionID,   // userID,
									    
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
		resource = this.getHTTPHandler().getResourceAccessor().locate( uri,
									       headers,
									       null,  // postData
									       null,  // additionalSettings
									       null,  // optionalReturnSettings
									       sessionID
									       );


	    } catch( MissingResourceException e ) {
		
		// File not found -> use raw message
		this.getHTTPHandler().getLogger().log( Level.FINE,
						       getClass().getName() + ".buildPreparedErrorResponse(...)",
						       "Error file resource '"+errorFilePath+"' not found; using raw message."
						       );		
		resource = new ByteArrayResource( this.getHTTPHandler(),
						  this.getHTTPHandler().getLogger(),
						  errorMessage.getBytes(), 
						  true // useFairLocks?
						  );
		// Overwrite the MIME type (the resource think's it is raw binary data)
		resource.getMetaData().setMIMEType( new MIMEType("text/plain") );
	    }


	    // Switch some additional error headers (required)
	    if( statusCode == Constants.HTTP_STATUS_CLIENTERROR_METHOD_NOT_ALLOWED ) {

		// Error 405 requires "Allow" header!
		// (see: http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html)
		//   "10.4.6 405 Method Not Allowed
		//    The method specified in the Request-Line is not allowed for the resource identified by the Request-URI. 
		//    The response MUST include an Allow header containing a list of valid methods for the requested resource."

		//String[] supportedMethods = this.getHTTPHandler().getSupportedMethods();
		List<String> supportedMethods = this.getHTTPHandler().getSupportedMethods();
		String allowValue             = CustomUtil.implode( supportedMethods, ", " );
		response.addResponseHeader( HTTPHeaders.NAME_ALLOW, allowValue );
		

	    } else if( statusCode == Constants.HTTP_STATUS_CLIENTERROR_UNAUTHORIZED ) {

		// Due to http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html the 
		// "WWW-Authenticate" ":" 1#challenge  
		// header is required on 401 errors!

		// Was there a realm name specified in any underlying htaccess configuration file?
		BasicType realmName = additionalSettings.get( Constants.KEY_HTACCESS_AUTHNAME );
		BasicType authType  = additionalSettings.get( Constants.KEY_HTACCESS_AUTHTYPE );
		if( realmName == null )  // Use default realm name (no specified)
		    realmName = new BasicStringType("MyNiceRealmName");

		if( authType == null ) // Use default auth method: Basic authentication
		    authType = new BasicStringType("Basic");


		
		// Replace all slashes and quotes
		// ...




		if( authType.getString().equalsIgnoreCase("Digest") ) {

		    // If the underlying configuration requires a DIGEST authentication we need
		    // to send some additional challenge request information:

		    BasicType wrp_nonce     = additionalSettings.get( Constants.KEY_AUTHENTICATION_NONCE );   
		    BasicType wrp_domain    = additionalSettings.get( Constants.KEY_AUTHENTICATION_DOMAIN );
		    BasicType wrp_algorithm = additionalSettings.get( Constants.KEY_AUTHENTICATION_ALGORITHM );

		    String authValue        = 
			authType.getString() + 
			" realm=\"" + realmName.getString() + "\"";

		    if( wrp_nonce != null )
			authValue += ", nonce=\"" + wrp_nonce.getString() + "\""; 
		    if( wrp_domain != null )
			authValue += ", domain=\"" + wrp_domain.getString() + "\""; 
		    if( wrp_algorithm != null )
			authValue += ", algorithm=\"" + wrp_algorithm.getString() + "\""; 


		    response.addResponseHeader( HTTPHeaders.NAME_WWW_AUTHENTICATE, authValue );


		} else {
	       
		
		    response.addResponseHeader( HTTPHeaders.NAME_WWW_AUTHENTICATE, authType.getString() + " realm=\"" + realmName.getString() + "\"" );


		}

 
	    }


	    response.setResponseDataResource( resource );
	    response.prepare( null );  // optionalReturnSettings are not of interest here (this is already error handling)
	    
	    return response;

	} catch( RuntimeException e ) {
	    
	    this.getHTTPHandler().getLogger().log( Level.WARNING,
						   getClass().getName() + ".buildPreparedErrorResponse(...)",
						   "Cannot build error reply (statusCode="+statusCode+", reasonPhrase="+reasonPhrase+"). RuntimeException: " + e.toString()
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
