package ikrs.http.filehandler;

/**
 * The CGI handler is an abstract class implementing some basic methods for 
 * the Common Gateway Interface.
 *
 * Subclasses must implement these methods:
 *  - List<String> buildCGISystemCommand( ... )
 *  - void buildAdditionalCGIEnvironmentVars( ... )
 *  - Resource handleCGIOutput( ... )
 *
 *
 *
 * @author Ikaros Kappler
 * @date 2012-10-12
 * @version 1.0.0
 **/

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import ikrs.http.AbstractFileHandler;
import ikrs.http.Constants;
import ikrs.http.DataFormatException;
import ikrs.http.DefaultPostDataWrapper;
import ikrs.http.FileHandler;
import ikrs.http.HeaderFormatException;
import ikrs.http.HTTPHandler;
import ikrs.http.HTTPHeaderLine;
import ikrs.http.HTTPHeaders;
import ikrs.http.PostDataWrapper;
import ikrs.http.Resource;
import ikrs.http.UnsupportedFormatException;
import ikrs.http.resource.InterruptableResource;
import ikrs.http.resource.ProcessableResource;
import ikrs.http.datatype.FormData;
import ikrs.http.datatype.KeyValueStringPair;
import ikrs.io.BytePositionInputStream;
import ikrs.util.CustomLogger;
import ikrs.util.Environment;
import ikrs.util.session.Session;
import ikrs.typesystem.BasicType;

public abstract class CGIHandler
    extends AbstractFileHandler {

    

    public static final String CGI_ENV_AUTH_TYPE              = "AUTH_TYPE";
    public static final String CGI_ENV_CONTENT_LENGTH         = "CONTENT_LENGTH";
    public static final String CGI_ENV_CONTENT_TYPE           = "CONTENT_TYPE";
    public static final String CGI_ENV_GATEWAY_INTERFACE      = "GATEWAY_INTERFACE";
    public static final String CGI_ENV_PATH_INFO              = "PATH_INFO";
    public static final String CGI_ENV_PATH_TRANSLATED        = "PATH_TRANSLATED";
    public static final String CGI_ENV_QUERY_STRING           = "QUERY_STRING";
    public static final String CGI_ENV_REMOTE_ADDR            = "REMOTE_ADDR";
    public static final String CGI_ENV_REMOTE_HOST            = "REMOTE_HOST";
    public static final String CGI_ENV_REMOTE_IDENT           = "REMOTE_IDENT";
    public static final String CGI_ENV_REMOTE_USER            = "REMOTE_USER";
    public static final String CGI_ENV_REQUEST_METHOD         = "REQUEST_METHOD";
    public static final String CGI_ENV_SCRIPT_NAME            = "SCRIPT_NAME";
    public static final String CGI_ENV_SERVER_NAME            = "SERVER_NAME";
    public static final String CGI_ENV_SERVER_PORT            = "SERVER_PORT";
    public static final String CGI_ENV_SERVER_PROTOCOL        = "SERVER_PROTOCOL";
    public static final String CGI_ENV_SERVER_SOFTWARE        = "SERVER_SOFTWARE";

    public static final String CGI_ENV_HTTP_                  = "HTTP_";



     /**
     * Create a new CGIHandler.
     * 
     * @param handler The global HTTP handler.
     * @param logger  A logger to write log messages to (must not be null).
     **/
    public CGIHandler( HTTPHandler handler, 
		       CustomLogger logger ) 
	throws NullPointerException {

	super( handler, logger );

	
    }
    

    //--- BEGIN --------- These methods must be implemented by subclasses --------------------------
    /**
     * Subclasses implementing this method must return a valid system command that can be executed
     * directly using Java's ProcessBuilder.
     *
     * The first list element must be the command name itself, all following elements are the command
     * line arguments.
     *
     *
     * @param headers    The current request's HTTP headers.
     * @param postData   The current request's post data (a data wrapper holding the input stream).
     * @param file       The requested file (in the local file system).
     * @param requestURI The request's URI (from headers.getRequestURI()).
     *
     * @return A list representing the system command.
     **/
    public abstract List<String> buildCGISystemCommand( HTTPHeaders headers,
							PostDataWrapper postData,
							File file,
							URI requestURI );


    /**
     * Subclasses implementing the method may define additional/optional CGI environment settings.
     * Note: there is no need to define the standard CGI environment as it is already contained
     *       in the handler's default mapping.
     *
     *       The default vars are:
     *          - AUTH_TYPE
     *          - CONTENT_LENGTH
     *          - CONTENT_TYPE
     *          - GATEWAY_INTERFACE
     *          - HTTP_*
     *          - PATH_INFO
     *          - PATH_TRANSLATED
     *          - QUERY_STRING
     *          - REMOTE_ADDR
     *          - REMOTE_HOST
     *          - REMOTE_IDENT
     *          - REMOTE_USER
     *          - REQUEST_METHOD
     *          - SCRIPT_NAME
     *          - SERVER_NAME
     *          - SERVER_PORT
     *          - SERVER_PROTOCOL
     *          - SERVER_SOFTWARE
     *
     * See CGI specs or http://graphcomp.com/info/specs/cgi11.html for details.
     *
     *
     * If the handler requires to overwrite pre-defined environment vars the method may change/remove
     * the value in the given mapping. Handle with care.
     *
     * If the implementing handler has no additional environment vars the method may just do nothing.
     *
     * @param headers     The current request's HTTP headers.
     * @param file        The requested file (in the local file system).
     * @param requestURI  The request's URI (from headers.getRequestURI()).
     * @param environment The current environment settings and the target map.
     *
     **/
    public abstract void buildAdditionalCGIEnvironmentVars( HTTPHeaders headers,
							    File file,
							    URI requestURI,
							    
							    Map<String,String> environment );


    /**
     * After the CGI handler performed the system command the resulting resource must be handled.
     * The way the CGI output is handled differs from handler to handler as the underlying ran
     * command produces different types of output.
     *
     * So it's up the the handler to process the generated data.
     *
     * @param headers         The current request's HTTP headers.
     * @param file            The requested file (in the local file system).
     * @param requestURI      The request's URI (from headers.getRequestURI()).
     * @param cgiOutput       The actual CGI output; use cgiOutput.getEcitValue() to determine the
     *                        return code of the CGI program.
     * @param postDataWrapper The sent post data (a wrapper object containing the input stream).
     *
     * @return After the output was processed the returned resource should contain (optional)
     *         header replacements and returned script data.
     **/
    public abstract Resource handleCGIOutput( HTTPHeaders headers,
					      File file,
					      URI requestURI,
					      PostDataWrapper postData,
					      
					      ProcessableResource cgiOutput )
	throws IOException;					  
    //--- END ----------- These methods must be implemented by subclasses --------------------------


    //--- BEGIN ------------------------ FileHandler implementation ------------------------------
    /**
     * Is that a good idea?
     **/
    public Resource process( UUID sessionID,
			     HTTPHeaders headers,
			     PostDataWrapper postData,
			     File file,
			     URI requestURI )
	throws IOException,
	       HeaderFormatException,
	       DataFormatException,
               UnsupportedFormatException {
	    
     
	// Fetch the system command (specified and constructed in the sub-class).
	List<String> command =  this.buildCGISystemCommand( headers, postData, file, requestURI );
	ProcessBuilder pb = new ProcessBuilder( command );
	
	this.buildCGIEnvironment( pb, headers, file, requestURI, sessionID );

	Map<String,String> environment = pb.environment();
	this.buildAdditionalCGIEnvironmentVars( headers, file, requestURI, environment );



	this.getLogger().log( Level.INFO,
			      getClass().getName() + ".process(...)",
			      "Creating a processable resource using the CGI file '" + file.getPath() + "'. System command: " + command.toString() );

	ProcessableResource cgiOutput = 
	    new ProcessableResource( this.getHTTPHandler(),
				     this.getLogger(),  
				     pb,
				     postData,  // Should be null if HTTP method is not POST
				     false      // useFairLocks not necessary here; there will be one more resource wrapper
				     );


	return this.handleCGIOutput( headers,
				     file,
				     requestURI,
				     postData,
				     
				     cgiOutput );
		
    }

    //--- END -------------------------- FileHandler implementation ------------------------------


    private void buildCGIEnvironment( ProcessBuilder pb,
				      HTTPHeaders headers,
				      File file,
				      URI requestURI,
				      UUID sessionID ) {


	// Bind CGI environment settings
	// Note: some settings come from the current session.
	Environment<String,BasicType> session = this.getHTTPHandler().getSessionManager().get( sessionID );
	
	// Fetch the 'Host' header field.
	// Format: host [ ":" port ]
	KeyValueStringPair host_port_pair = KeyValueStringPair.split( headers.getStringValue( HTTPHeaders.NAME_HOST ),
								      false,  // don't tryo to remove quotes
								      ":"     // The separator
								      );


	BasicType wrp_remoteAddr  = session.get( Constants.SKEY_REMOTE_ADDRESS );
	BasicType wrp_remoteHost  = session.get( Constants.SKEY_REMOTE_HOST );
	BasicType wrp_remoteIdent = session.get( Constants.SKEY_REMOTE_IDENT );
	BasicType wrp_remoteUser  = session.get( Constants.SKEY_REMOTE_USER );


	this.bindCGIEnvironmentVar( headers, requestURI, pb, CGIHandler.CGI_ENV_AUTH_TYPE,         null );
	this.bindCGIEnvironmentVar( headers, requestURI, pb, CGIHandler.CGI_ENV_CONTENT_LENGTH,    headers.getStringValue(HTTPHeaders.NAME_CONTENT_LENGTH) );
	this.bindCGIEnvironmentVar( headers, requestURI, pb, CGIHandler.CGI_ENV_CONTENT_TYPE,      headers.getStringValue(HTTPHeaders.NAME_CONTENT_TYPE) );
	this.bindCGIEnvironmentVar( headers, requestURI, pb, CGIHandler.CGI_ENV_GATEWAY_INTERFACE, "CGI/1.1" );
	this.bindCGIEnvironmentVar( headers, requestURI, pb, CGIHandler.CGI_ENV_PATH_INFO,         file.getAbsolutePath() );
	this.bindCGIEnvironmentVar( headers, requestURI, pb, CGIHandler.CGI_ENV_PATH_TRANSLATED,   file.getAbsolutePath() );  // ??!
	this.bindCGIEnvironmentVar( headers, requestURI, pb, CGIHandler.CGI_ENV_QUERY_STRING,      requestURI.getRawQuery() ); // url-encoded!
	if( wrp_remoteAddr != null )
	    this.bindCGIEnvironmentVar( headers, requestURI, pb, CGIHandler.CGI_ENV_REMOTE_ADDR,       wrp_remoteAddr.getString() );

	if( wrp_remoteHost != null )
	    this.bindCGIEnvironmentVar( headers, requestURI, pb, CGIHandler.CGI_ENV_REMOTE_HOST,       wrp_remoteHost.getString() );

	if( wrp_remoteIdent != null )
	    this.bindCGIEnvironmentVar( headers, requestURI, pb, CGIHandler.CGI_ENV_REMOTE_IDENT,      wrp_remoteIdent.getString() );

	if( wrp_remoteUser != null )
	    this.bindCGIEnvironmentVar( headers, requestURI, pb, CGIHandler.CGI_ENV_REMOTE_USER,       wrp_remoteUser.getString() );


	this.bindCGIEnvironmentVar( headers, requestURI, pb, CGIHandler.CGI_ENV_REQUEST_METHOD,    headers.getRequestMethod() );
	this.bindCGIEnvironmentVar( headers, requestURI, pb, CGIHandler.CGI_ENV_SCRIPT_NAME,       file.getAbsolutePath() ); 


	if( host_port_pair != null && host_port_pair.getKey() != null )
	    this.bindCGIEnvironmentVar( headers, requestURI, pb, CGIHandler.CGI_ENV_SERVER_NAME,       host_port_pair.getKey() );

	if( host_port_pair != null && host_port_pair.getValue() != null )
	    this.bindCGIEnvironmentVar( headers, requestURI, pb, CGIHandler.CGI_ENV_SERVER_PORT,       host_port_pair.getValue() );

	this.bindCGIEnvironmentVar( headers, requestURI, pb, CGIHandler.CGI_ENV_SERVER_PROTOCOL,   "HTTP/1.1" );	
	this.bindCGIEnvironmentVar( headers, requestURI, pb, CGIHandler.CGI_ENV_SERVER_SOFTWARE,   this.getHTTPHandler().getSoftwareName() );
	

	// Add some additional header fields ...
	// CGI_ENV_HTTP_    




    }

    private void bindCGIEnvironmentVar( HTTPHeaders headers,
					URI requestURI,
					ProcessBuilder pb,
					String key,
					String value ) {

	if( value == null )
	    value = "";

	
	this.getLogger().log( Level.INFO,
			      getClass().getName() + ".bindCGIEnvironmentVar(...)",
			      "[requestedPath=" + requestURI.getPath() + "] Mapping '" + key +"' into CGI environment: " + value );
	pb.environment().put( key, value );

    }


}
