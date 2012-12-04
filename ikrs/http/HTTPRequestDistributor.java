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
import java.io.InputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.logging.Level;

import ikrs.io.BytePositionInputStream;
import ikrs.io.ReadLimitInputStream;
import ikrs.util.CustomLogger;
import ikrs.util.session.Session;
import ikrs.typesystem.BasicBooleanType;
import ikrs.typesystem.BasicNumberType;
import ikrs.typesystem.BasicType;
import ikrs.typesystem.BasicStringType;
import ikrs.yuccasrv.ConnectionUserID;


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
     * The connection's user ID.
     **/
    private HTTPConnectionUserID userID;

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
				   Socket socket,
				   HTTPConnectionUserID userID 
				   )  
	throws NullPointerException {
	super();

	this.handler = handler;
	this.logger  = logger;
	this.logger.log( Level.INFO,
			 getClass().getName(),
			 "New request handler created (userID="+userID+")."
			 );

	this.socketID = socketID;
	this.socket   = socket;
	this.userID   = userID;
    }
    

    public void run() {
	
	
	UUID sessionID = this.handleSession_connectionStart( this.userID );

	

	// Do not use the connectionUserID after this point any more! 
	// It's too strong bound to the socketmanager, to which the HTTP handler should not be bound at all (except as a handler).
	// Use the session ID instead to keep the underlying implementations disconnected from the yucca.* package.

	//UUID sessionID = session.getSessionID();
	

	try {

	    // Init HTTP connection ... 
	    //HTTPHeaderLine.read( sock.getInputStream() );
	    HTTPHeaders headers = HTTPHeaders.read( this.socket.getInputStream() );


	    PostDataWrapper postData = null;
	    if( headers.isPOSTRequest() ) {

		Long contentLength = headers.getLongValue( HTTPHeaders.NAME_CONTENT_LENGTH );  // This search is case-insensitive
		this.logger.log( Level.INFO,
				 getClass().getName() + ".run()",
				 "Handling POST data using 'Content-Length': '" + contentLength + "'." );
	    
	    
		// Is there a default value if not passed?
		InputStream postDataInputStream = null;
		if( contentLength == null ) {
		
		    postDataInputStream = new BytePositionInputStream( this.socket.getInputStream() );

		} else {
		
		    postDataInputStream = new ReadLimitInputStream( this.socket.getInputStream(),
								    contentLength.longValue() );


		}


		// Wrap the trailing data into a PostDataWrapper
		postData = new DefaultPostDataWrapper( this.logger,
						       headers,
						       postDataInputStream // this.socket.getInputStream() 
						       );

	    } // END if [is POST request]

	    
	    // Create the response. 
	    // Note that the returned response is ALREADY PREPARED!
	    PreparedHTTPResponse response = this.handler.getResponseBuilder().create( headers, 
										      postData,
										      this.socketID,
										      this.socket,
										      sessionID,    // this.userID,
										      null          // No additionals
										      );
	    // There are some very unlikely cases the server cannot send an error reply (broken error messages, ...).
	    // In these cases the builder returns null (this should not happen but one more check is more secure).
	    if( response == null ) {

		this.logger.log( Level.SEVERE,
				 getClass().getName(),
				 "Cannot handle client request. The ResponseBuilder returned a null-response. This usually indicates fatal runtime errors."
			     );

	    } else {
		
		// System.out.println( "Built response: " + response.toString() );
	    
		// Then execute (this MUST NOT throw any exceptions!)
		this.logger.log( Level.INFO,
				 getClass().getName(),
				 "Going to execute prepared response " + response.toString() );			     
		response.execute();
				
		
		// Dont't forget to clean-up and release the locks!
		this.logger.log( Level.INFO,
				 getClass().getName(),
				 "Going to dispose prepared response " + response.toString() );
		response.dispose();
	    
	    }
	    

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


	handleSession_connectionEnd( sessionID );

	

    }


    /**
     * This method is called when the distributor starts processing an incoming connection.
     * It initializes some essential session vars (if required).
     *
     * @param userID The connection's user ID.
     * @return The (possibly new) session.
     **/
    private UUID handleSession_connectionStart( HTTPConnectionUserID userID ) {

	// Bind the session.
	// If the sessions already exists, the session manager just returns it without modifying the session map.
	Session<String,BasicType,HTTPConnectionUserID> session = this.handler.getSessionManager().bind( userID );
	

	// Store basic connection information in the session. 
	// Some modules (such as the CGIHandler) require those fields.
	
	session.put( Constants.SKEY_REMOTE_ADDRESS, new BasicStringType(this.socket.getInetAddress().getHostAddress()) );
	session.put( Constants.SKEY_REMOTE_HOST,    new BasicStringType(this.socket.getInetAddress().getHostName()) );
	// session.put( Constants.SKEY_REMOTE_IDENT,   null ); ??? 
	// session.put( Constants.SKEY_REMOTE_USER, null );    ???
	
	//System.out.println( "session=" + session );



	this.logger.log( Level.INFO,
			 getClass().getName(),
			 "Request handler started (session="+session+")."
			 );


	// Init the session ALIVE value.
	// This flag will be automatically 'deleted' by the session manager on session timeout (better: the while session becomes invalid).


	// Is this the first call (no valid session available)?
	if( session.get(Constants.SKEY_ISALIVE) == null ) {

	    // Yes. Init session flag?
	    session.put( Constants.SKEY_ISALIVE,        new BasicBooleanType(true) );
	    session.put( Constants.SKEY_LASTACCESSTIME, new BasicNumberType(-1L) );

	    // NOOP!?

	} else {

	    // Session timed out?
	    BasicType wrp_lastAccessTime = session.get( Constants.SKEY_LASTACCESSTIME );
	    BasicType wrp_sessionTimeout = this.handler.getEnvironment().getChild( Constants.EKEY_GLOBALCONFIGURATION ).get( Constants.KEY_SESSIONTIMEOUT );

	    int sessionTimeout = 300;        // Default (hard coded) session timeout value (if not set)
	    if( wrp_sessionTimeout != null )
		sessionTimeout = wrp_sessionTimeout.getInt();


	    
	    if( wrp_lastAccessTime == null || wrp_lastAccessTime.getLong() <= (System.currentTimeMillis() - sessionTimeout) ) {

		// The session died!
		// All session vars must be invalidated!
		session.clear();   

		// Also remove (clear?) all children!
		session.removeAllChildren();
		

	    }
	    
	}

	return session.getSessionID();
    }


    /**
     * This method is called when the distributor finishes processing a connection.
     * It updates som important fields on the existing (!) session.
     *
     * @param sessionID The session's unique ID.
     **/
    private void handleSession_connectionEnd( UUID sessionID ) {
	Session<String,BasicType,HTTPConnectionUserID> session = this.handler.getSessionManager().get( sessionID );

	// Trace the session access time
	session.put( Constants.SKEY_LASTACCESSTIME, new BasicNumberType(System.currentTimeMillis()) );
	session.put( Constants.SKEY_ISALIVE, new BasicBooleanType(true) );
    }

}
