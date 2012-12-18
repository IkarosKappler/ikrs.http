package ikrs.httpd.response.successful;

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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Level;

import ikrs.httpd.AbstractPreparedResponse;
import ikrs.httpd.AuthorizationException;
import ikrs.httpd.ConfigurationException;
import ikrs.httpd.Constants;
import ikrs.httpd.CustomUtil;
import ikrs.httpd.DataFormatException;
import ikrs.httpd.HeaderFormatException;
import ikrs.httpd.HTTPHandler;
import ikrs.httpd.HTTPHeaderLine;
import ikrs.httpd.HTTPHeaders;
import ikrs.httpd.MalformedRequestException;
import ikrs.httpd.PostDataWrapper;
import ikrs.httpd.Resource;
import ikrs.httpd.UnsupportedFormatException;
import ikrs.httpd.UnsupportedMethodException;
import ikrs.httpd.UnsupportedVersionException;
import ikrs.httpd.UnknownMethodException;
import ikrs.httpd.resource.ByteArrayResource;
import ikrs.httpd.response.GeneralPreparedResponse;

import ikrs.typesystem.*;


public class OK
    extends GeneralPreparedResponse {


    private PostDataWrapper postData;


    public OK( HTTPHandler handler,
	       HTTPHeaders headers,
	       PostDataWrapper postData,
	       UUID socketID,
	       Socket socket,
	       UUID sessionID ) {
	super( handler, headers, socketID, socket, sessionID, Constants.HTTP_STATUS_SUCCESSFUL_OK, "OK" );

	    
	this.postData = postData;
    }

    /**
     * Get the wrapped POST data from the current request.
     *
     **/
    protected PostDataWrapper getPostData() {
	return this.postData;
    }
    

    //---BEGIN------------------------- AbstractPreparedResponse implementation ------------------------------
    /**
     * This method must be implemented by all subclasses. It must prepare the HTTP response.
     * This means that all required ressources must be acquired (use locks), all headers prepared (by the use
     * of addResponseHeader(String,String) or getResponseHeaders()) and perform all necessary security checks.
     *
     * Subclasses implementing this method should call the setPrepared() method when ready.
     *
     * @param optionalReturnSettings This (optional, means may be null) map can be used to retrieve internal values
     *                               for error recovery.
     *
     * @throws MalformRequestException If the passed HTTP request headers are malformed and cannot be processed.
     * @throws UnsupportedVersionException If the headers' HTTP version is not supported (supported versions are
     *                                     1.0 and 1.1).
     * @throws UnsupportedMethodException If the request method is valid but not supported (status code 405).
     * @throws UnknownMethodException If the headers' method (from the request line) is unknown.
     * @throws ConfigurationException If the was a server configuration issue the server cannot work properly with.
     * @throws MissingResourceException If the requested resource cannot be found.
     * @throws AuthorizationException If the requested resource requires authorization.
     * @throws HeaderFormatException If the passed headers are malformed.
     * @throws DataFormatException If the passed data is malformed.
     * @throws SecurityException If the request cannot be processed due to security reasons.
     * @throws IOException If any IO errors occur.
     **/
    public void prepare( Map<String,BasicType> optionalReturnSettings ) 
	throws MalformedRequestException,
	       UnsupportedVersionException,
	       UnsupportedMethodException,
	       UnknownMethodException,
	       ConfigurationException,
	       MissingResourceException,
	       AuthorizationException,
	       HeaderFormatException,
	       DataFormatException,
	       UnsupportedFormatException,
	       SecurityException,
	       IOException {

	// The current implemenation of tiny server only supports HTTP-GET, -POST and -OPTIONS.
	String httpMethod = this.checkValidHTTPMethod();

	if( this.getRequestHeaders().isGETRequest() 
	    || this.getRequestHeaders().isPOSTRequest() 
	    || this.getRequestHeaders().isHEADRequest() ) {
	 
	    this.prepareGETorPOSTorHEAD( optionalReturnSettings );

	} else if( this.getRequestHeaders().isOPTIONSRequest() ) {

	    this.prepareOPTIONS( optionalReturnSettings );

	} else if( this.getRequestHeaders().isTRACERequest() ) {

	    // Note that TRACE is not secure.
	    // http://www.cgisecurity.com/whitehat-mirror/WH-WhitePaper_XST_ebook.pdf
	    this.prepareTRACE( optionalReturnSettings );  

	} else {

	    throw new UnsupportedMethodException( httpMethod, "Unsupported HTTP method ('" + httpMethod + "')" );

	}

    }


    /**
     * This method prepares GET and POST requests.
     *
     * @param optionalReturnSettings An (optional) map for error retrieval and internal settings retrieval.
     **/
    private void prepareGETorPOSTorHEAD( Map<String,BasicType> optionalReturnSettings ) 
	throws MalformedRequestException,
	       UnsupportedVersionException,
	       UnsupportedMethodException,
	       UnknownMethodException,
	       ConfigurationException,
	       MissingResourceException,
	       AuthorizationException,
	       HeaderFormatException,
	       DataFormatException,
	       UnsupportedFormatException,
	       SecurityException,
	       IOException {


	String httpMethod = this.getRequestHeaders().getRequestMethod();

	// Fetch the request URI
	String requestURI = this.getRequestHeaders().getRequestURI();
	if( requestURI == null )
	    throw new MalformedRequestException( "Malformed request line. URI missing." );


	
	Map<String,BasicType> additionalResourceSettings = new TreeMap<String,BasicType>();
	try {
	    
	    // Warp the URI into an object.
	    URI uri = new URI( requestURI ); 


	    // Fetch the authorization field (if present).
	    // Note that there are different possible methods to perform authorization (Basic, Digest, ...)!
	    HTTPHeaderLine authLine = this.getRequestHeaders().get( "Authorization" );
	    if( authLine != null ) {

		this.getHTTPHandler().getLogger().log( Level.INFO,
						       getClass().getName() + ".prepare(...)",
						       "Going to parse authorization line ..." );
		this.parseAuthorizationLine( authLine, additionalResourceSettings );

	    }

	    // Store session ID
	    additionalResourceSettings.put( Constants.KEY_SESSIONID, new BasicUUIDType(this.getSessionID()) );
	    

	    this.getHTTPHandler().getLogger().log( Level.INFO,
						   getClass().getName() + ".prepare(...)",
						   "Passing requested URI ("+uri+") to the resource accessor." );
	    // This might throw one of these exceptions:
	    //  - ResourceNotFoundException
	    //  - AuthorizationException
	    //  - HeaderFormatException
	    //  - DataFormatException
	    //  - MissingResourceException
	    //  - ConfigurationException
	    //  - SecurityException
	    //  - IOException
	    Resource resource = this.getHTTPHandler().getResourceAccessor().locate( uri,
										    this.getRequestHeaders(),
										    this.getPostData(),
										    additionalResourceSettings,
										    optionalReturnSettings,
										    this.getSessionID()
										    );
	    this.getHTTPHandler().getLogger().log( Level.INFO,
						   getClass().getName() + ".prepare(...)",
						   "Resource accessor granted access to the file '" + uri.getPath() + "'. optionalReuturnSettings=" + optionalReturnSettings );


	    resource.getReadLock().lock();
	    resource.open( true ); // Open in read-only mode 


	    // Has the resource a customized statusCode?
	    if( resource.getMetaData().getOverrideHeaders().getResponseStatus() != null 
		&& resource.getMetaData().getOverrideHeaders().getResponseReasonPhrase() != null ) {
		
		this.setStatusCode( resource.getMetaData().getOverrideHeaders().getResponseStatus() );
		this.setReasonPhrase( resource.getMetaData().getOverrideHeaders().getResponseReasonPhrase() );
		this.getHTTPHandler().getLogger().log( Level.INFO,
						       getClass().getName() + ".prepare(...)",
						       "Resource's override header: statusCode=" + this.getStatusCode() + ", reasonPhrase=" + this.getReasonPhrase() ); 
	    }
	    
	    

       
	    String response_mimeType    = resource.getMetaData().getMIMEType().getContentType();
	    BasicType response_charSet  = this.getHTTPHandler().getGlobalConfiguration().get( Constants.KEY_DEFAULTCHARACTERSET );

	    // Check if the content type is overridden by the htaccess file
	    BasicType wrp_addedType     = optionalReturnSettings.get( Constants.KEY_HTACCESS_ADDEDTYPE );
	    if( wrp_addedType != null ) {
			     
		response_mimeType = wrp_addedType.getString();
		this.getHTTPHandler().getLogger().log( Level.FINE,
						       getClass().getName() + ".prepare(...)",
						       "There is a .htaccess configuration that overrides the resource's MIME type to '" + response_mimeType + "'." );

	    }




	    if( response_charSet == null ) {
		
		// Default charset would be "iso-8859-1" :/
		// ... but iso-8859-* is sh%%
		response_charSet = new BasicStringType( "utf-8" ); 
		this.getHTTPHandler().getLogger().log( Level.FINE,
						       getClass().getName() + ".prepare(...)",
						       "There is no character encoding specified for the resource. Using default value '" + response_charSet.getString() + "'." );

	    }

	
	    // Add default headers (might be overwritter later, see below).
	    super.addResponseHeader( "Server",            this.getHTTPHandler().getSoftwareName() ); 
	    super.addResponseHeader( "Content-Length",    Long.toString(resource.getLength()) );
	    super.addResponseHeader( "Content-Language",  "en" );
	    super.addResponseHeader( "Connection",        "close" );
	    super.addResponseHeader( "Content-Type",      response_mimeType + "; charset=" + response_charSet.getString() );

	    if( authLine != null ) {
		int max_age  = 0; // 60?
		int s_maxage = 0; // 30?
		
		super.addResponseHeader( "Cache-Control", "max-age=" + max_age + ", s-maxage=" + s_maxage + ", must-revalidate" );  // Values must be configurable!
	    }


	    this.getHTTPHandler().getLogger().log( Level.INFO,
						   getClass().getName() + ".prepare(...)",
						   "Current response headers: " + this.getResponseHeaders().toString() );

	    // Add/replace generated response headers
	    for( int i = 0; i < resource.getMetaData().getOverrideHeaders().size(); i++ ) {
		this.getResponseHeaders().add( resource.getMetaData().getOverrideHeaders().get(i), 
					       true  // replaceIfExists
					       );
	    }
					 

	

	    // "The HEAD method is identical to GET except that the server MUST NOT return a message-body in the response."
	    // See http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html
	    if( !this.getRequestHeaders().isHEADRequest() ) {

		super.setResponseDataResource( resource );

	    }


	} catch( URISyntaxException e ) {

	    this.getHTTPHandler().getLogger().log( Level.INFO,
						   getClass().getName() + ".prepare(...)",
						   "[URISyntaxException] " + e.getMessage() );
	    throw new MalformedRequestException( "Malformed request URI '"+requestURI+"': " + e.getMessage() );

	} catch( BasicTypeException e ) {

	    this.getHTTPHandler().getLogger().log( Level.INFO,
						   getClass().getName() + ".prepare(...)",
						   "[BasicTypeException] " + e.getMessage() );
	    throw new ConfigurationException( e.getMessage() );

	}
    }


    /**
     * This method prepares OPTIONS requests.
     *
     * @param optionalReturnSettings An (optional) map for error retrieval and internal settings retrieval.
     **/
    private void prepareOPTIONS( Map<String,BasicType> optionalReturnSettings ) 
	throws MalformedRequestException,
	       UnsupportedVersionException,
	       UnsupportedMethodException,
	       UnknownMethodException,
	       ConfigurationException,
	       MissingResourceException,
	       AuthorizationException,
	       HeaderFormatException,
	       DataFormatException,
	       UnsupportedFormatException,
	       SecurityException,
	       IOException {


	//String[] supportedMethods = this.getHTTPHandler().getSupportedMethods();
	List<String> supportedMethods = this.getHTTPHandler().getSupportedMethods();
	String optionsCSV = CustomUtil.implode( supportedMethods, "," );
	
	
	
	// Add default headers
	super.addResponseHeader( "Server",            this.getHTTPHandler().getSoftwareName() ); 
	super.addResponseHeader( "Content-Length",    "0" ); 
	super.addResponseHeader( "Connection",        "close" );


	// This is the actual OPTIONS response
	super.addResponseHeader( "Allow", optionsCSV );
	 
    }


    /**
     * This method prepares OPTIONS requests.
     *
     * @param optionalReturnSettings An (optional) map for error retrieval and internal settings retrieval.
     **/
    private void prepareTRACE( Map<String,BasicType> optionalReturnSettings ) 
	throws MalformedRequestException,
	       UnsupportedVersionException,
	       UnsupportedMethodException,
	       UnknownMethodException,
	       ConfigurationException,
	       MissingResourceException,
	       AuthorizationException,
	       HeaderFormatException,
	       DataFormatException,
	       UnsupportedFormatException,
	       SecurityException,
	       IOException {


	// "The TRACE method is used to invoke a remote, application-layer loop- back of the request message. 
	//  The final recipient of the request SHOULD reflect the message received back to the client as the 
	// entity-body of a 200 (OK) response."
	// See http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html
	
	StringBuffer buffer = new StringBuffer();
	Iterator<HTTPHeaderLine> iter = this.getRequestHeaders().iterator();
	// Add all header lines to the buffer
	//int CR = 0xD;  // 13 decimal
	//int LF = 0xA;  // 10 decimal
	while( iter.hasNext() ) {

	    HTTPHeaderLine line = iter.next();
	    buffer.append( line.getKey() );
	    if( line.getValue() != null ) 
		buffer.append( ": " ).append( line.getValue() );
	    buffer.append( (char)Constants.CR ).append( (char)Constants.LF );
	    
	    
	    //buffer.append( line.getRawBytes( CharSet  ...) );
	}

	Resource resource = new ByteArrayResource( this.getHTTPHandler(),
						   this.getHTTPHandler().getLogger(),
						   buffer.toString().getBytes(),
						   false       // no need to use fair locks
						   );

	resource.getReadLock().lock();
	resource.open( true ); // Open in read-only mode
	
	
	// Add default headers
	super.addResponseHeader( "Server",            this.getHTTPHandler().getSoftwareName() ); 
	super.addResponseHeader( "Content-Length",    Long.toString(resource.getLength()) ); 
	super.addResponseHeader( "Connection",        "close" );
	super.addResponseHeader( "Content-Language",  "en" );      // HTTP is in english somehow ... I think
	super.addResponseHeader( "Content-Type",      "text/plain; charset=utf8" );

	



	// Store generated header response resource
	super.setResponseDataResource( resource );
	 
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
						   "Failed to close resource: " + e.getMessage() );

	} finally {

	    // Release the read-lock
	    this.getResponseDataResource().getReadLock().unlock();

	}

	

    }
    //---END--------------------------- AbstractPreparedResponse implementation ------------------------------


    /**
     * This method parses the 'Authorization' header and adds three fields to the destination
     * map:
     *  - "Authorization.Method"   (String; currently only "Basic" is supported).
     *  - "Authorization.User"     (String)
     *  - "Authorization.password" (String)
     **/
    private void parseAuthorizationLine( HTTPHeaderLine authLine,
					 Map<String,BasicType> destination ) 
	throws MalformedRequestException,
	       UnknownMethodException {

	String value = authLine.getValue();
	if( value == null || value.length() == 0 )
	    throw new MalformedRequestException( "The 'Authorization' header field value is empty." );
	

	// Split a the first space
	int index = value.indexOf(" ");
	if( index == -1 || index == value.length()-1 )
	    throw new MalformedRequestException( "The 'Authorization' header field is malformed (two tokens separated by whitespace expected)." );

	String authMethod = value.substring(0,index);
	destination.put( Constants.KEY_AUTHORIZATION_METHOD, new BasicStringType(authMethod) );
	
	if( authMethod.equals("Digest") ) {

	    // Is this the correct?
	    // throw new UnknownMethodException( "Unsupported Authentication method: '"+authMethod+"'." );

	    // Extract the DIGEST challenge data
	    if( index+1 >= value.length() )
		destination.put( Constants.KEY_AUTHORIZATION_CHALLENGE, new BasicStringType("") );
	    else
		destination.put( Constants.KEY_AUTHORIZATION_CHALLENGE, new BasicStringType(value.substring(index+1).trim()) );
	    

	} else if( authMethod.equals("Basic") ) {

	    // Fetch the base64 encoded auth data
	    String base64     = value.substring(index+1,value.length());
	    String authString = null;
	    try {

		// It is expected that the auth data is base64 encoded, but NOT REQUIRED 
		// -> try to convert.
		byte[] authBytes  = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64);
		authString        = new String(authBytes);

	    } catch( IllegalArgumentException e ) {
		
		// If conversion failed -> was probably not base64 
		// -> use raw text data
		authString        = base64;

	    }
	    index = authString.indexOf(":");
	    if( index == -1 )
		throw new UnknownMethodException( "Invalid Authentication credentials. ':' missing to seperate username from password." );
	    String user = authString.substring(0,index);
	    String pass = (index+1 < authString.length() ? authString.substring(index+1) : "" );
	    
	    //destination.put( Constants.KEY_AUTHORIZATION_METHOD, new BasicStringType(authMethod) );
	    destination.put( Constants.KEY_AUTHORIZATION_USER,   new BasicStringType(user) );
	    destination.put( Constants.KEY_AUTHORIZATION_PASS,   new BasicStringType(pass) );

	} else {

	    // Is this the correct?
	    throw new UnknownMethodException( "Unknown Authentication method: '"+authMethod+"'." );

	}

	
	
    }


    /**
     * This method just validate the request's HTTP method and - if valid - returns its String value.
     * Otherwise it throws a respective exception.
     **/
    private String checkValidHTTPMethod() 
	throws MalformedRequestException,
	       UnknownMethodException,
	       UnsupportedMethodException {

	

	String httpMethod = this.getRequestHeaders().getRequestMethod();
	if( httpMethod == null )
	    throw new MalformedRequestException( "The request misses the HTTP method (GET, POST, ...)." );

	if( !this.getHTTPHandler().isSupportedMethod(httpMethod) ) { 


	    // Validate HTTP METHOD
	    // Make a clear decision later?
	    if( httpMethod.equals(Constants.HTTP_METHOD_GET) 
		|| httpMethod.equals(Constants.HTTP_METHOD_POST) 
		|| httpMethod.equals(Constants.HTTP_METHOD_HEAD) 
		|| httpMethod.equals(Constants.HTTP_METHOD_PUT) 
		|| httpMethod.equals(Constants.HTTP_METHOD_DELETE)
		|| httpMethod.equals(Constants.HTTP_METHOD_TRACE) 
		|| httpMethod.equals(Constants.HTTP_METHOD_OPTIONS)
		|| httpMethod.equals(Constants.HTTP_METHOD_CONNECT) ) {


		// Better throw code 405 (Method Not Allowed) ... for future versions
		//throw new UnknownMethodException( "The HTTP " + httpMethod + " method is not yet supported by this server implementation." );
		throw new UnsupportedMethodException( httpMethod, "Unsupported HTTP method ('" + httpMethod + "')" );

	    } else {
		
		// throw new MalformedRequestException( "Unknown HTTP request method: '" + httpMethod +"'." );
		throw new UnknownMethodException( "Unknown HTTP request method: '" + httpMethod +"'." );

		
	    }

	}

	// Everything is fine.
	return httpMethod;
    }


}
