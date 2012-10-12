package ikrs.http;

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

    
    /**
     * Create a new AbstractFileHandler.
     * 
     * @param logger A logger to write log messages to (must not be null).
     **/
    public AbstractFileHandler( HTTPHandler handler,
				CustomLogger logger ) 
	throws NullPointerException {

	super();

	if( logger == null )
	    throw new NullPointerException( "Cannot create file handlers with a null-logger." );

	this.httpHandler = handler;
	this.logger = logger;
	
    }

    public HTTPHandler getHTTPHandler() {
	return this.httpHandler;
    }
    
    public CustomLogger getLogger() {
	return this.logger;
    }

    //--- BEGIN ------------------------ FileHandler implementation ------------------------------
    /**
     * Is that a good idea?
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
