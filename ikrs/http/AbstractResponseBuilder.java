package ikrs.http;

/**
 * @author Ikaros Kappler
 * @date 2012-07-16
 * @version 1.0.0
 **/

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;

import ikrs.typesystem.BasicType;


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
     * @param socket   The acutual socket.
     * @param additionals A map containing non-essential builder params. The expected 
     *                    map contents depends on the underlying implementation; some
     *                    builders even allow null-additionals.
     *
     * @return A new HTTPRequest built from the HTTP headers.
     *
     **/
    public abstract PreparedHTTPResponse create( HTTPHeaders headers,
						 UUID socketID,
						 Socket socket,

						 Map<String,BasicType> additionals
						 );
    //---END-------------------------------- ReplyBuilder implementaion -----------------------


}
