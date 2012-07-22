package ikrs.http;

/**
 * @author Ikaros Kappler
 * @date 2012-07-16
 * @version 1.0.0
 **/

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;


public abstract class AbstractResponseBuilder
    implements ResponseBuilder {

    /**
     * The top level HTTP handler.
     **/
    protected HTTPHandler httpHandler;

    
    /**
     * Subclasses must call this constructor an pass the top level HTTP handler.
     **/
    public AbstractResponseBuilder( HTTPHandler handler ) {
	super();
	
	this.httpHandler = handler;
    }


    public HTTPHandler getHTTPHandler() {
	return this.httpHandler;
    }


    //---BEGIN------------------------------ ReplyBuilder implementaion -----------------------
    /**
     * This method translates the given headers and socket into an executable 
     * PreparedResponse object.
     *
     * The method does not throw any exceptions as the error reporting is part of HTTP
     * itself.
     *
     * @param headers  The previously processed headers.
     * @param socketID The unique socket ID.
     * @param socket   The acutual socket;
     *
     * @return A new HTTPRequest built from the HTTP headers.
     *
     **/
    public abstract PreparedHTTPResponse create( HTTPHeaders headers,
						 UUID socketID,
						 Socket socket
						 );
    //---END-------------------------------- ReplyBuilder implementaion -----------------------


}
