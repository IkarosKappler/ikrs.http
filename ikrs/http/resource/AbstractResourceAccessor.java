package ikrs.http.resource;

/**
 * @autor Ikaros Kappler
 * @date 2012-07-23
 * @version 1.0.0
 **/

import java.net.URI;
import java.util.MissingResourceException;

import ikrs.http.Resource;
import ikrs.http.ResourceAccessor;
import ikrs.http.HTTPHandler;

public abstract class AbstractResourceAccessor 
    implements ResourceAccessor {


    /**
     * The top level HTTP handler.
     **/
    private HTTPHandler httpHandler;


    /**
     * ... 
     **/
    public AbstractResourceAccessor( HTTPHandler handler ) {
	super();
	
	this.httpHandler = handler;
    }


    /**
     * Get the top level http handler.
     **/
    public HTTPHandler getHTTPHandler() {
	return this.httpHandler;
    }


    /**
     * This method locates the desired resource addressed by the given URI.
     *
     * @throws ResouceMissingException If the specified resource cannot be found.
     **/
    public abstract Resource locate( URI uri )
	throws MissingResourceException;


}

