package ikrs.httpd;

/**
 * This is a general abstract FileHandler implementation. Classes implementing the FileHandler 
 * interface should extend this class.
 *
 *
 * @author Ikaros Kappler
 * @date 2012-09-29
 * @version 1.0.0
 **/

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;


import ikrs.util.CustomLogger;

public abstract class AbstractFileHandler
    implements FileHandler {

    private HTTPHandler httpHandler;

    /**
     * A custom logger to write log messages to.
     **/
    private CustomLogger logger;


    public AbstractFileHandler() 
	throws NullPointerException {

	super();
	
    }
    
    /**
     * Create a new AbstractFileHandler.
     * 
     * @param logger A logger to write log messages to (must not be null).
     **/
    /*public AbstractFileHandler( HTTPHandler handler,
				CustomLogger logger ) 
	throws NullPointerException {

	super();

	if( logger == null )
	    throw new NullPointerException( "Cannot create file handlers with a null-logger." );

	this.httpHandler = handler;
	this.logger = logger;
	
    }
    */

    //--- BEGIN ------------------------ FileHandler implementation ------------------------------
    /**
     * Get get FileHandler's global HTTPHandler.
     *
     * Warning: as subclasses might be instantiated using the Class.newInstance() method
     *          in some very unusual cases the returned handler might be null. In that case
     *          be sure you have the handler set before by the use of setHTTPHandler(...).
     *
     * @return The global HTTP handler (if available).
     **/
    public HTTPHandler getHTTPHandler() {
	return this.httpHandler;
    }

    /**
     * Set the global HTTP handler.
     *
     * @param handler The new handler (must not be null).
     * @throws NullPointerException If handler is null.
     **/
    public void setHTTPHandler( HTTPHandler handler )
	throws NullPointerException {

	if( handler == null )
	    throw new NullPointerException( "Cannot apply null handler." );

	this.httpHandler = handler;
    }


    /**
     * Get the custom logger to use to write log messages.
     *
     * Warning: as subclasses might be instantiated using the Class.newInstance() method
     *          in some very unusual cases the returned logger might be null. In that case
     *          be sure you have the logger set before by the use of setLogger(...).
     *
     * @return The logger to use (if available).
     **/
    public CustomLogger getLogger() {
	if( this.logger == null && this.httpHandler != null )
	    return this.httpHandler.getLogger();
	else
	    return this.logger;
    }


    /**
     * Set the logger to use.
     *
     * @param logger The new logger (must not be null).
     * @throws NullPointerException If logger is null.
     **/
    public void setLogger( CustomLogger logger )
	throws NullPointerException {

	if( logger == null )
	    throw new NullPointerException( "Cannt apply null loggers." );

	this.logger = logger;

    }
    
    /**
     * The 'process' method is very generic. It depends on the underlying implementation how the passed
     * file should be processed.
     *
     * @param sessionID   The current session's ID.
     * @param headers     The HTTP request headers.
     * @param postData    The HTTP post data; if the method is not HTTP POST the 'postData' should be null
     *                    (or empty).
     * @param file        The requested file itself (inside the local file system).
     * @param requestURI  The requested URI (relative to DOCUMENT_ROOT).
     **/
    public abstract Resource process( UUID sessionID,
				      HTTPHeaders headers,
				      PostDataWrapper postData,
				      File file,
				      URI requestURI )
	throws IOException,
	       HeaderFormatException,
	       DataFormatException,
	       UnsupportedFormatException;

    //--- END -------------------------- FileHandler implementation ------------------------------

}
