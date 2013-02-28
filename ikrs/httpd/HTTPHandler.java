/**
 *   ikrs.httpd - A free java http server based on ikrs.yucca.
 *   Copyright (C) 2012 Ikaros Kappler
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/


package ikrs.httpd;

/**
 * This is the main handler class that will be bound as a listener to the yucca server.
 *
 * @author Henning Diesenberg
 * @date 2012-05-15
 * @version 1.0.0
 **/


import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.URI;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;



import ikrs.httpd.resource.FileSystemResourceAccessor;
import ikrs.httpd.resource.DefaultDirectoryResource;
import ikrs.yuccasrv.ConnectionUserID;
import ikrs.yuccasrv.TCPAdapter;
import ikrs.yuccasrv.socketmngr.BindManager;

import ikrs.typesystem.*;

import ikrs.util.CustomLogger;
import ikrs.util.DefaultCustomLogger;
import ikrs.util.DefaultEnvironment;
import ikrs.util.Environment;
import ikrs.util.EnvironmentFactory;
import ikrs.util.FileExtensionKeyMap;
import ikrs.util.KeyValueStringPair;
import ikrs.util.MapFactory;
import ikrs.util.TreeMapFactory;

import ikrs.util.session.*;


public class HTTPHandler 
    extends TCPAdapter 
    implements RejectedExecutionHandler {

    //    private boolean inited = false;


    protected static final String[] SUPPORTED_METHODS = new String[] {
	Constants.HTTP_METHOD_GET,
	Constants.HTTP_METHOD_POST,
	Constants.HTTP_METHOD_OPTIONS,
	Constants.HTTP_METHOD_HEAD,

	// Note that TRACE is not secure
	// See: http://www.cgisecurity.com/whitehat-mirror/WH-WhitePaper_XST_ebook.pdf
	Constants.HTTP_METHOD_TRACE
    };

    /**
     * The document root file (will be initialized when the config is loaded).
     **/
    private File documentRoot;

    /**
     * The ThreadPoolExecutor that handles the thread pool.
     **/
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * A thread factory for the ThreadPoolExecutor.
     **/
    private HTTPServerThreadFactory threadFactory;

    /**
     * This queue will hold the HTTPRequestHandlers.
     **/
    private ArrayBlockingQueue<Runnable> requestQueue;

    /**
     * The global custom logger.
     **/
    private CustomLogger logger;


    /**
     * The response builder.
     **/
    private DefaultResponseBuilder responseBuilder;

    /**
     * The resource accessor.
     **/ 
    private ResourceAccessor resourceAccessor;


    /**
     * The global environment.
     **/
    private Environment<String,BasicType> environment;


    /**
     * The session manager.
     **/
    private SessionManager<String,BasicType,HTTPConnectionUserID> sessionManager;

    /**
     * The file filter for HTTP access.
     **/
    private HTTPFileFilter fileFilter;

    /**
     * A map for the file handlers (by file extension).
     * 
     * This will be an instance of FileExtensionKeyMap<FileHandler>.
     **/
    private Map<String,FileHandler> fileHandlerExtensionMap;

    /**
     * A map for the file handler names.
     **/
    private Map<String,FileHandler> fileHandlerNameMap;


    /**
     * A map containing pre-defined error document URIs (mapped to their HTTP status codes).
     **/
    private Map<Integer,URI> errorDocumentMap;


    /**
     * A DateFormat instance for getting Date object in the required HTTP Date
     * representation. The date format is like this:
     *  Tue, 15 Nov 1994 08:12:31 GMT
     * 
     * For details see
     * http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html 
     * section 14.18 (Date).
     **/
    private DateFormat httpDateFormat;

    public HTTPHandler() {
	super();
	
	this.logger = new DefaultCustomLogger( Constants.NAME_DEFAULT_LOGGER );
	this.logger.setLevel( Level.ALL );

	this.environment  = new DefaultEnvironment<String,BasicType>( new TreeMapFactory<String,BasicType>(),
								      true   // allowsMultipleChildNames
								      );
	this.environment.put( Constants.KEY_SOFTWARENAME, 
			      new BasicStringType("Yucca/" + Constants.VERSION + " " + 
						  "(" + System.getProperty("os.name") + ") " +
						  "Java/" + System.getProperty("java.version")) 
			      ); 

	int sessionMaxAge = 300; // seconds (default value; migght be overridden by config)
	this.initGlobalConfiguration( sessionMaxAge );


	// Init the session manager.	
	this.logger.log( Level.INFO,
			 getClass().getName(),
			 "[Init SessionManager] Create a new map factory to use for the environment creation." );
	ikrs.util.MapFactory<String,BasicType> 
	    mapFactory                          = new TreeMapFactory<String,BasicType>(); 
	
	this.logger.log( Level.INFO,
			 getClass().getName(),
			 "[Init SessionManager] Create a new environment factory to use for the session creation." );
	EnvironmentFactory<String,BasicType>      
	    environmentFactory                  = new ikrs.util.DefaultEnvironmentFactory<String,BasicType>( mapFactory, 
													     false       // allowsMultipleChildNames
													     );

	logger.log( Level.INFO,
		    getClass().getName(),
		    "[Init SessionManager] Create a new session factory to use for the session manager." );
	SessionFactory<String,BasicType,HTTPConnectionUserID> 
	    sessionFactory                      = new DefaultSessionFactory<String,BasicType,HTTPConnectionUserID>( environmentFactory );

	logger.log( Level.INFO,
		    getClass().getName(),
		    "[Init SessionManager] Creating the session manager (sessionMaxAge=" + sessionMaxAge + ").");
	this.sessionManager                     = new DefaultSessionManager<String,BasicType,HTTPConnectionUserID>( sessionFactory, 
														    sessionMaxAge  // max-age for sessions in seconds
														    );

	logger.log( Level.INFO,
		    getClass().getName(),
		    "[Init FileFilter] Initializing the file filter.");
	this.fileFilter         = new DefaultFileFilter();


	// Init the HTTP Date format
	this.httpDateFormat     = new SimpleDateFormat( "EEE, dd MMM yyyy HH:mm:ss z", Locale.US ); // Locale.US?
	this.httpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));


	logger.log( Level.INFO,
		    getClass().getName(),
		    "[Init ThreadPoolExecutor]");
	this.requestQueue       = new ArrayBlockingQueue<Runnable>( 20 );
	this.threadFactory      = new HTTPServerThreadFactory( this,  // HTTPHandler
							       this.logger
							       );
	this.threadPoolExecutor = new ThreadPoolExecutor( 10,    // corePoolSize
							  20,    // maximumPoolSize
							  300L,  // keepAliveTime (for waiting threads)
							  TimeUnit.SECONDS,
							  this.requestQueue,
							  (ThreadFactory)this.threadFactory,
							  (RejectedExecutionHandler)this   // RejectedExcecutionHandler
							  );

	this.responseBuilder    = new DefaultResponseBuilder( this );
	this.resourceAccessor   = new FileSystemResourceAccessor( this, this.logger );
	
	this.errorDocumentMap   = Collections.synchronizedMap( new TreeMap<Integer,URI>() );
	

	// Pre start core thread?
	// this.executorService.prestartCoreThread();

	logger.log( Level.INFO,
		    getClass().getName() + "{init}",
		    "Initialization done. System ready.");
    }

    private void initDefaultDocumentRoot() {
	this.setDocumentRoot( new File("document_root") );

    }

    protected void setDocumentRoot( File newDocumentRoot ) 
	throws NullPointerException {
	if( newDocumentRoot == null ) {

	    this.logger.log( Level.SEVERE,
			     getClass().getName(),
			     "[Init] Cannot set DOCUMENT_ROOT to null!" );
	    throw new NullPointerException( "Cannot set DOCUMENT_ROOT to null!" );
	}

	this.documentRoot = newDocumentRoot; 
	if( !this.documentRoot.exists() && !this.documentRoot.mkdirs() ) {

	    this.logger.log( Level.SEVERE,
			     getClass().getName(),
			     "[Init] Failed to create document root '" + this.documentRoot.getPath() + "'! This will probably make your server un-usable. (continue though)" );

	}
    }

    /**
     * This method checks whether the passed file-system file is 'inside' the configured document root.
     * The passed file should be rooted at the global file system root.
     *
     * Note that the document root directory is considered to be inside itself, as it is
     * accessible by the URI '/'.
     *
     * @param file A file inside the local system's file system; must not be null.
     * @return True if - and only if - the passed file is a file or a directory inside the document root
     *              directory or inside any subdirectory inside the document root or the passed file
     *              is document root itself. Or in other words: if the passed file is inside the configured 
     *              HTTP document tree.
     * @throws NullPointerException If the passed file is null.
     **/ 
    public boolean isInsideDocumentRoot( File file ) 
	throws NullPointerException {

	while( file != null ) {
	    
	    if( this.getDocumentRoot().equals(file) )
		return true;

	    file = file.getParentFile();
	}

	return false;
    }
    

    /**
     * This method initializes the FileHandler map.
     **/
    protected void initFileHandlers( File fileHandlersFile ) {

	this.fileHandlerExtensionMap = Collections.synchronizedMap( new FileExtensionKeyMap<FileHandler>() );
	this.fileHandlerNameMap      = Collections.synchronizedMap( new TreeMap<String,FileHandler>() );

	//String fileHandlersFileName = "filehandlers.ini";
	String handlerClassName = null; // "ikrs.http.filehandler.PHPHandler";
	try {   

	    //BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream(new File(fileHandlersFileName)) ) );
	    BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream(fileHandlersFile) ) );
	    String line;
	    int lineNumber = 0;
	    while( (line = reader.readLine()) != null ) {

		lineNumber++;

		// Ignore empty lines
		if( (line = line.trim()).length() == 0 )
		    continue;

		// Ignore comments
		if( line.startsWith("#") )
		    continue;



		KeyValueStringPair pair = KeyValueStringPair.split( line,
								    false,  // No need to remove quotes?
								    "=" );
		// The key cannot be null as the string is not null and not empty.
		// But the value can
		if( pair.getValue() == null || pair.getValue().length() == 0 ) {

		    logger.log( Level.WARNING,
				getClass().getName(),
				"Invalid entry in file '" + fileHandlersFile.getPath() + "' at line "+lineNumber+". Missing key part in: " + line );

		}
		    
		String handlerName = pair.getKey();
		String[] split = pair.getValue().split( "(\\s)+" );

		// At the first position is the handler CLASS NAME
		handlerClassName = split[0];
		
		

		Class<?> handlerClass = Class.forName( handlerClassName );
		boolean isFileHandler = CustomUtil.classImplementsInterface( handlerClass, 
									     "ikrs.httpd.FileHandler", 
									     true   // includeSuperClasses 
									     );

		if( !isFileHandler ) {

		    logger.log( Level.SEVERE,
				getClass().getName(),
				"Failed to instantiate handler class '" + handlerClassName + "': this does not implement ikrs.httpd.FileHandler." );

		} else {

		    FileHandler fileHandler = (FileHandler)handlerClass.newInstance();
		    fileHandler.setHTTPHandler( this );
		    fileHandler.setLogger( this.getLogger() );
		    
		    // Associate all file extensions from the config file with the created handlers.
		    // Note: at index 0 is the classname itself!
		    for( int i = 1; i < split.length; i++ ) {
			logger.log( Level.INFO,
				    getClass().getName(),
				    "Mapping file handler '"+ handlerName + "' to extension: " + split[i] );
			// Split[i] contains the file extension
			this.fileHandlerExtensionMap.put( split[i], fileHandler );
		    }

		    logger.log( Level.INFO,
				getClass().getName(),
				"Mapping file handler '"+ handlerName + "' to handler class: " + handlerClassName );
		    this.fileHandlerNameMap.put( handlerName, fileHandler );
		}
	    }


	} catch( IOException e ) {
	    logger.log( Level.SEVERE,
			getClass().getName(),
			"Failed to read file handler config from file '" + fileHandlersFile.getPath() + "': " + e.getMessage() );
	} catch( ClassNotFoundException e ) {
	    logger.log( Level.SEVERE,
			getClass().getName(),
			"Failed to load handler class (not found): " + handlerClassName );
	} catch( InstantiationException e ) {
	    logger.log( Level.SEVERE,
			getClass().getName(),
			"[InstantiationException] Failed to instantiate '" + handlerClassName + "': " + e.getMessage() );
	} catch( IllegalAccessException e ) {
	    logger.log( Level.SEVERE,
			getClass().getName(),
			"[IllegalAccessException] Failed to instantiate '" + handlerClassName + "': " + e.getMessage() );
	}

    }

    /**
     * This inits the global configuration.
     * Currently the conf is hard coded. It should come from a configuration file soon ...
     *
     **/
    private void initGlobalConfiguration( int sessionTimeout_seconds) {
	Environment<String,BasicType> gconfig = this.environment.createChild( Constants.EKEY_GLOBALCONFIGURATION );
	gconfig.put( Constants.KEY_SESSIONTIMEOUT, new BasicNumberType(sessionTimeout_seconds) );
	gconfig.put( Constants.KEY_DEFAULTCHARACTERSET, new BasicStringType("utf-8") ); // iso-8859-1") );
	// ...

	if( this.getSessionManager() != null )
	    this.getSessionManager().setSessionTimeout( sessionTimeout_seconds );
    }

    public Environment<String,BasicType> getGlobalConfiguration() {
	return this.environment.getChild( Constants.EKEY_GLOBALCONFIGURATION );
    }


    public File getDocumentRoot() {
	return this.documentRoot;
    }
    
    public boolean isDirectoryListingAllowed() {
	return true;
    }

    public boolean isSupportedMethod( String method ) {
	for( int i = 0; i <  HTTPHandler.SUPPORTED_METHODS.length; i++ ) {
	    if( HTTPHandler.SUPPORTED_METHODS[i].equals(method) ) {

		// The method is generally implemented and supported.
		// Now check if the method was disabled by the admin!
		return !this.isDisabledMethod( method );

	    }
	}
	return false;
    }

    private boolean isDisabledMethod( String method ) {

	String keyName = Constants.CKEY_HTTPCONFIG_DISABLE_METHOD_BASE.replaceAll( "\\{HTTP_METHOD\\}", method );
	BasicType wrp_methodDisabled = this.getGlobalConfiguration().get( keyName );

	// The HTTP method is NOT disabled                                                                                                
	//  -    if the DISABLE* flag is not present                                                                                      
	//  - OR if the DISABLE* flag is set to false.                                                                                    
	try {
	    
	    return ( wrp_methodDisabled != null && wrp_methodDisabled.getBoolean() );
	    
	} catch( BasicTypeException e ) {
	    
	    // Ooops. This is configuration error. The flag has no valid boolean value.                                                   
	    this.getLogger().log( Level.SEVERE,
				  getClass().getName() + ".isDisabledMethod(...)",
				  "Configuration error: the entity '" + keyName + "' is not a valid boolean value: " + wrp_methodDisabled.getString() );
	    // Consider method 'disabled'.                                                                                                
	    return true;
	}

    }

    /**
     * Get a list of allowed (implemented) methods. The returned array is a new copy.
     **/
    //public String[] getSupportedMethods() {
    //	return java.util.Arrays.copyOf( HTTPHandler.SUPPORTED_METHODS, HTTPHandler.SUPPORTED_METHODS.length );
    //}

    public List<String> getSupportedMethods() {
	List<String> list = new ArrayList<String>( Math.max( 1, HTTPHandler.SUPPORTED_METHODS.length ) );

	for( int i = 0; i < HTTPHandler.SUPPORTED_METHODS.length; i++ ) {

	    String method = HTTPHandler.SUPPORTED_METHODS[i];
	    if( !this.isDisabledMethod(method) )
		list.add( method );

	}

	return list;
    }

    public DateFormat getHTTPDateFormat() {
	return this.httpDateFormat;
    }

    /**
     * Get the server's software name, compatible with the 'Server' header field.
     *
     * 'Server' should have the format: 
     * Apache/1.3.29 (Unix) PHP/4.3.4
     *
     **/
    public String getSoftwareName() {
	BasicType name = this.getEnvironment().get( Constants.KEY_SOFTWARENAME );

	if( name == null )
	    return getClass().getName();
	else
	    return name.getString();
    }


    /**
     * Get the global response builder.
     **/
    public ResponseBuilder getResponseBuilder() {
	return this.responseBuilder;
    }

    /**
     * Get the global response builder with dynamic type 'DefaultResponseBuilder'.
     **/
    protected DefaultResponseBuilder getDefaultResponseBuilder() {
	return this.responseBuilder;
    }

    /**
     * Get the global resource accessor.
     **/
    public ResourceAccessor getResourceAccessor() {
	return this.resourceAccessor;
    }

    /**
     * Get the global logger.
     **/
    public CustomLogger getLogger() {
	return this.logger;
    }

    public Environment<String,BasicType> getEnvironment() {
	return this.environment;
    }

    /**
     * Get this handler's session manager.
     **/
    public SessionManager<String,BasicType,HTTPConnectionUserID> getSessionManager() {
	return this.sessionManager;
    }

    public HTTPFileFilter getFileFilter() {
	return this.fileFilter;
    }

    /**
     * This method resolves the FileHandler matching the given file extension.
     **/
    public FileHandler getFileHandlerByExtension( String fileExtension ) {

	if( fileExtension == null )
	    return null;

	//System.out.println( "Locating file handler by extension '" + fileExtension + "'. handlerExtensionMap=" + this.fileHandlerExtensionMap.toString() );

	return this.fileHandlerExtensionMap.get( fileExtension );
    }

    /**
     * This method resolves the FileHandler matching the given file extension.
     **/
    public FileHandler getFileHandlerByName( String handlerName ) {

	if( handlerName == null )
	    return null;

	//System.out.println( "Locating file handler by name '" + handlerName + "'. handlerExtensionMap=" + this.fileHandlerExtensionMap.toString() );

	return this.fileHandlerNameMap.get( handlerName );
    }

    /**
     * Get the default configured error document (URI relative to document root) by the given 
     * status code.
     *
     * It is possible that not all error documents are defined; the method will return null
     * in this case.
     *
     * @param statusCode The error code (usually a 30*, 40* or 50* status) you want to get
     *                   the error document for.
     * @return The error document (URI) for the passed error code.
     **/
    public URI getDefaultErrorDocumentURI( Integer statusCode ) {
	return this.errorDocumentMap.get( statusCode );
    }
    

    /**
     * This method returns the internal error document map which will be configured by
     * the HTTPConfigurator.
     *
     * The returned map is never null.
     **/
    protected Map<Integer,URI> getErrorDocumentMap() {
	return this.errorDocumentMap;
    }

    //---BEGIN-------------------- RejectedExcecutionHandler Implementation -------------------------
    /**
     * The RejectedExecutionHandler.rejectedExecution(...) is called by the ThreadPoolExecutor if the request queue
     * is full and all threads are busy.
     *
     * This server should send a "Service Unavailable" error reply then.
     *
     * @param r        The Runnable object that was rejected to be executed.
     * @param executor The executor that rejected the request.
     **/
    public void rejectedExecution( Runnable r,             
				   ThreadPoolExecutor executor ) {

	if( this.threadPoolExecutor != executor ) {

	    this.logger.log( Level.SEVERE,
			     getClass().getName(),
			     "A ThreadPoolExecutor rejected a Runnable to be executed but the executor is unknown. Returning." );
	    return; // Not our concern
	}

	
	this.logger.log( Level.WARNING,
			 getClass().getName(),
			 "Execution rejected for Runnable: " + r );

	try {

	    HTTPRequestDistributor distributor = (HTTPRequestDistributor)r;
	    // This must be handled fast because this method blocks the underlying
	    // socket manager to accept more connections! -> Threaded?
	    PreparedHTTPResponse errorResponse = this.getDefaultResponseBuilder().buildPreparedErrorResponse( 
							 null,                        // no HTTP headers read -> none available!  
							 null,                        // no POST data
							 distributor.getSocketID(),   // UUID socketID, 
							 distributor.getSocket(),     // Socket socket, 
							 null,                        // no session id UUID sessionID,   
							 null,                        // No exception
							 Constants.HTTP_STATUS_SERVERERROR_SERVICE_UNAVAILABLE,  // int statusCode,
							 null,                        // String reasonPhrase,
							 null,                        // String errorMessage,
							 
							 null,                        // Map<String,BasicType> additionalSettings
							 null                         //Map<String,BasicType> optionalReturnSettings
														    );
	    errorResponse.prepare( null ); // No optionalReturnSettings
	    errorResponse.execute();
	    errorResponse.dispose();
														    
	    
	} catch( ClassCastException e ) {

	    this.logger.log( Level.SEVERE,
			     getClass().getName() + ".rejectedExecution(...)",
			     "The ThreadPoolExecutor rejected the passed Runnable '" + r + "' but it's NOT a HTTPRequestDistributor. Cannot send error reply." );

	} catch( Exception e ) {

	    // This will be ANY exception throw new the prepared error response (during preparation, execution or cleanup).
	    // As this is a pertty risky process (the request was not meant to be handled at all) we should simply ignore
	    // all errors and exceptions.
	    this.getLogger().log( Level.SEVERE,
				  getClass().getName() + ".rejectedExecution(...)",
				  "[" + e.getClass().getName() + "] Failed to process rejected (!) request (ignoring): " + e.getMessage() );

	}

    }
    //---END---------------------- RejectedExcecutionHandler Implementation -------------------------


    //---BEGIN-------------------- TCPHandler Implementation -------------------------

    /**
     * This method will be called after the connection handler was instantiated (usually using the
     * Class.newInstance() method).
     *
     * Both params might be null or empty depending on the underlying interface implementation.
     *
     * This method must throw an InstantiationException if any required params are missing.
     *
     * @param additionalSettings   An environment containing additional initialization params. Might be null or empty.
     * @param optionalReturnValues An environment the method may use to store (optional) return values in. May be null.
     **/
    public void init( Environment<String,BasicType> additionalSettings,
		      Environment<String,BasicType> optionalReturnValues  )
	throws InstantiationException {
	

	synchronized( this ) {

	    // Since version 0.9.9 it is possible to re-use the handler instance ('sharedHandlerInstance' in
	    // the server config). So yucca might try to initialize the handler multiple times. 
	    // Avoid this here:
	
	    if( this.documentRoot != null ) 
		return; // Already initialized

	
	    HTTPConfiguration config = new HTTPConfiguration( this, this.getLogger() );
	
	    try {

		config.applyConfiguration( additionalSettings );

		// After all verify the configuration so all essentials are present
		if( this.documentRoot == null ) 
		    this.initDefaultDocumentRoot();
	    

	    } catch( IOException e ) {   
	    
		this.getLogger().log( Level.SEVERE,
				      getClass().getName() + ".init(...)",
				      "[IOException] " + e.getMessage() );
		throw new InstantiationException( "[IOException] " + e.getMessage() );
	    
	    } catch( ConfigurationException e ) {
	    
		this.getLogger().log( Level.SEVERE,
				      getClass().getName() + ".init(...)",
				      "[ConfigurationException] " + e.getMessage() );
		throw new InstantiationException( "[ConfigurationException] " + e.getMessage() );
	    }

	} // END synchronized

    }

    /**
     * @param source The BindManager that reports the event.
     * @param sockedID The server socket's unique ID.
     * @param sock The accepted connection socket.
     **/
    public void serverAcceptedTCPConnection( BindManager source,
					     UUID socketID,
					     Socket sock,
					     ConnectionUserID<ConnectionUserID> userID ) {
	
	this.enqueue( source, socketID, sock, userID );
	
	
    }

    /**
     * This method will be called if the SocketManager is going to terminate.
     * All associated BindListener MUST terminate within the given time.
     *
     * @param time The time value all dependent child threads have to terminate in.
     * @param unit The time unit.
     **/
    public void finalize( long time,
			  java.util.concurrent.TimeUnit unit ) {

	this.logger.log( Level.INFO,
			 getClass().getName(),
			 "Going to stop ThreadPoolExecutor ..." );
	

	// Set new time interval; all thread (already) waiting longer than this time will be dismissed.
	this.threadPoolExecutor.setKeepAliveTime( time, unit );

	// This will prevent the executor to accept more waiting threads.
	this.threadPoolExecutor.shutdown();
	
    }
    //---END-------------------- TCPHandler Implementation -------------------------

    /**
     * This method is used by this handler to add new HTTP distributors to the internal thread pool.
     * The thread pool stores new requests into a blocking queue.
     *
     **/
    private void enqueue( BindManager source,
			  UUID socketID,
			  Socket sock,
			  ConnectionUserID<ConnectionUserID> userID ) {

	this.logger.log( Level.INFO,
			 getClass().getName(),
			 "Incoming HTTP connection from " + sock.getInetAddress().getHostAddress() + "." 
			 );
	HTTPRequestDistributor requestDistributor = 
	    new HTTPRequestDistributor( this,
					this.logger,
					socketID,
					sock,
					new HTTPConnectionUserID(userID)
					);
	this.threadPoolExecutor.execute( requestDistributor );

    }

    

}