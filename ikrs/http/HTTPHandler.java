package ikrs.http;

/**
 * This is the main handler class that will be bound as a listener to the yucca server.
 *
 * @author Henning Diesenberg
 * @date 2012-05-15
 * @version 1.0.0
 **/


import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.DatagramSocket;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;


import ikrs.http.resource.FileSystemResourceAccessor;
import ikrs.http.resource.DefaultDirectoryResource;
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
import ikrs.util.MapFactory;
import ikrs.util.TreeMapFactory;

import ikrs.util.session.*;


public class HTTPHandler 
    extends TCPAdapter 
    implements RejectedExecutionHandler {

    protected static final String[] SUPPORTED_METHODS = new String[] {
	"GET",
	"POST"
    };

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
    private ResponseBuilder responseBuilder;

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
     **/
    private FileExtensionKeyMap<FileHandler> fileHandlerMap;



    public HTTPHandler() {
	super();
	
	this.logger = new DefaultCustomLogger( Constants.NAME_DEFAULT_LOGGER );
	this.logger.setLevel( Level.ALL );

	this.documentRoot = new File( "document_root_alpha" );
	if( !this.documentRoot.exists() && !this.documentRoot.mkdirs() ) {

	    this.logger.log( Level.SEVERE,
			     getClass().getName(),
			     "[Init] Failed to create document root '" + this.documentRoot.getPath() + "'! This will probably make your server un-usable. (continue though)" );

	}

	this.environment  = new DefaultEnvironment<String,BasicType>( new TreeMapFactory<String,BasicType>(),
								      true   // allowsMultipleChildNames
								      );
	this.environment.put( Constants.KEY_SOFTWARENAME, 
			      new BasicStringType("Yucca/" + Constants.VERSION + " " + 
						  "(" + System.getProperty("os.name") + ") " +
						  "Java/" + System.getProperty("java.version")) 
			      ); 

	this.initGlobalConfiguration();


	// Init the session manager.
	int sessionMaxAge = 30; // seconds

	this.logger.log( Level.INFO,
			 getClass().getName(),
			 "[Init SessionManager] Create a new map factory to use for the environment creation." );
	ikrs.util.MapFactory<String,BasicType> 
	    mapFactory                          = new TreeMapFactory<String,BasicType>(); // ModelBasedMapFactory<String,BasicType>( new TreeMap<String,BasicType>() );
	
	this.logger.log( Level.INFO,
			 getClass().getName(),
			 "[Init SessionManager] Create a new environment factory to use for the session creation." );
	EnvironmentFactory<String,BasicType>      
	    environmentFactory                  = new ikrs.util.DefaultEnvironmentFactory<String,BasicType>( mapFactory );

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
	this.fileHandlerMap     = new FileExtensionKeyMap<FileHandler>();
	this.initFileHandlers();
	/*this.fileHandlerMap.put( ".php", new DefaultDirectoryResource( this,
								       this.getLogger(),
								       this.getFileFilter(),
								       requestedFile,
								       uri,
								       sessionID,
								       outputFormat,   // HTML or TXT
								       true )
								       ); */
	
	

	// Pre start core thread?
	// this.executorService.prestartCoreThread();

	logger.log( Level.INFO,
		    getClass().getName(),
		    "Initialization done. System ready.");
    }

    /**
     * This method initializes the FileHandler map.
     **/
    private void initFileHandlers() {

	String handlerClassName = "ikrs.http.filehandler.PHPHandler";
	try {   
	    Class<?> handlerClass = Class.forName( handlerClassName );
	    // Implements the 'FileHandler' interface?
	    Class<?>[] ifs = handlerClass.getInterfaces();
	    boolean isFileHandler = false;
	    for( int i = 0; i < ifs.length && !isFileHandler; i++ ) {
		
		isFileHandler = ifs[i].getName().equals("ikrs.http.FileHandler");
		
	    }

	    if( !isFileHandler ) {

		logger.log( Level.SEVERE,
			getClass().getName(),
			"Failed to instantiate handler class '" + handlerClassName + "': this does not implement ikrs.http.FileHandler." );

	    } else {

		FileHandler fileHandler = (FileHandler)handlerClass.newInstance();
		fileHandler.setHTTPHandler( this );
		fileHandler.setLogger( this.getLogger() );
		this.fileHandlerMap.put( ".php", fileHandler ); // new ikrs.http.filehandler.PHPHandler(this, this.logger) );

	    }

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
    private void initGlobalConfiguration() {
	Environment<String,BasicType> gconfig = this.environment.createChild( Constants.EKEY_GLOBALCONFIGURATION );
	gconfig.put( Constants.KEY_SESSIONTIMEOUT, new BasicNumberType(10) ); // 300) );
	gconfig.put( Constants.KEY_DEFAULTCHARACTERSET, new BasicStringType("utf-8") ); // iso-8859-1") );
	//gconfig.put( Constants.KEY_SERVERNAME, new BasicStringType("booze.dyndns.org") ); // iso-8859-1") );
	// ...
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
	    if( HTTPHandler.SUPPORTED_METHODS[i].equals(method) )
		return true;
	}
	return false;
    }

    /**
     * Get a list of allowed (implemented) methods. The returned array is a new copy.
     **/
    public String[] getSupportedMethods() {
	return java.util.Arrays.copyOf( HTTPHandler.SUPPORTED_METHODS, HTTPHandler.SUPPORTED_METHODS.length );
    }

    /**
     * Get the server's software name, compatible with the 'Server' header field.
     *
     * 'Server' should have the format: 
     * Apache/1.3.29 (Unix) PHP/4.3.4
     *
     **/
    //public String getServerName() {
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
    public FileHandler getFileHandler( String fileExtension ) {

	if( fileExtension == null )
	    return null;

	return this.fileHandlerMap.get( fileExtension );
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
	    // ... how to handle? This must be fast because this method blocks the underlying
	    // socket manager to accept more connections! -> Threaded?
	    //distributor.sendErrorReply( );
	    
	} catch( ClassCastException e ) {

	    this.logger.log( Level.SEVERE,
			 getClass().getName(),
			 "The ThreadPoolExecutor rejected the passed Runnable '" + r + "' but it's NOT a HTTPRequestDistributor. Cannot send error reply." );

	}

    }
    //---END---------------------- RejectedExcecutionHandler Implementation -------------------------


    //---BEGIN-------------------- TCPHandler Implementation -------------------------
    /**
     * @param source The BindManager that reports the event.
     * @param sockedID The server socket's unique ID.
     * @param sock The accepted connection socket.
     **/
    public void serverAcceptedTCPConnection( BindManager source,
					     UUID socketID,
					     Socket sock,
					     ConnectionUserID<ConnectionUserID> userID ) {
	
	enqueue( source, socketID, sock, userID );
	
	
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