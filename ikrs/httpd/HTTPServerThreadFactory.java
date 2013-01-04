package ikrs.httpd;

/**
 * This ThreadFactory implementation creates new HTTPServerThreads.
 *
 * @author Ikaros Kappler
 * @date 2012-07-16
 * @version 1.0.0
 **/

import java.util.concurrent.ThreadFactory;

import ikrs.util.CustomLogger;


public class HTTPServerThreadFactory
    implements ThreadFactory {

    private HTTPHandler handler;
    private CustomLogger logger;

    //private Map<String,HTTPRequestHandler> handlerMap;

    public HTTPServerThreadFactory( HTTPHandler handler,
				    CustomLogger logger 
				    ) {
	this.handler = handler;
	this.logger = logger;
    }
    
    //--- BEGIN ---------------------- ThreadFactory implementation ----------------
    public HTTPServerThread newThread( Runnable r ) {
	return new HTTPServerThread( this.handler,
				     this.logger,
				     r );
    }
    //--- END ------------------------ ThreadFactory implementation ----------------

}