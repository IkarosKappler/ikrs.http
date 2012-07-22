package ikrs.http;

/**
 * This HTTPRequestDistributor is a wrapper class that processes HTTPRequests on a top level.
 * It reads the HTTP header data and tries to find a suitable handler class to forward the request to.
 *
 * @author Ikaros Kappler
 * @date 2012-07-16
 * @version 1.0.0
 **/

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.logging.Level;

import ikrs.util.CustomLogger;


public class HTTPRequestDistributor 
    implements Runnable {

    /**
     * The HTTP handler that passed the request to this distributor.
     **/
    private HTTPHandler handler;

    /**
     * The socket ID the request came from (can be used to fetch additional
     * connection information from the underlying BindManager).
     **/
    private UUID socketID;
    
    /**
     * The socket the request came from.
     **/
    private Socket socket;

    /**
     * A logger to write log messages to.
     **/
    private CustomLogger logger;


    /**
     * The constructor to create a new HTTPRequestDistributor.
     *
     * @param handler  The http handler that passes the request to this new distributor.
     * @param logger   A logger to write log messages to.
     * @param socketID The socket's unique ID.
     * @param socket   The originated socket itself.
     **/
    public HTTPRequestDistributor( HTTPHandler handler,
				   CustomLogger logger,
				   UUID socketID,
				   Socket socket 
				   ) 
	throws NullPointerException {
	super();

	this.handler = handler;
	this.logger = logger;
	this.logger.log( Level.INFO,
			 getClass().getName(),
			 "New request handler created."
			 );

	this.socketID = socketID;
	this.socket = socket;
    }
    

    public void run() {
	
	this.logger.log( Level.INFO,
			 getClass().getName(),
			 "Request handler started."
			 );


	try {

	    /* Init HTTP connection ... */
	    //HTTPHeaderLine.read( sock.getInputStream() );
	    HTTPHeaders headers = HTTPHeaders.read( this.socket.getInputStream() );

	    
	    // Create the response
	    PreparedHTTPResponse response = this.handler.getResponseBuilder().create( headers, 
										      this.socketID,
										      this.socket );
	    
	    // Then execute
	    response.execute();


	    // Dont't forget to clean-up and release the locks!
	    response.dispose();
	    
	    
	    
	    this.socket.close();

	} catch( EOFException e ) {

	    //e.printStackTrace();
	    this.logger.log( Level.FINER,
			     getClass().getName(),
			     "Cannot handle client request. EOF reached before reaching end-of-headers: " + e.getMessage()
			     );
  
	} catch( IOException e ) {
	    
	    //e.printStackTrace();
	    this.logger.log( Level.FINER,
			     getClass().getName(),
			     "Cannot handle client request. IO error: " + e.getMessage()
			     );
  
	    
	}

    }


}
