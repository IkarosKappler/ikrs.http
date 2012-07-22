package ikrs.yuccasrv.socketmngr;

/**
 * The ServerSocketThread class implements a Runnable object to wrap the
 * basic server into a concurrent thread.
 *
 *
 * @author Henning Diesenberg
 * @date 2012-04-22
 * @version 1.0.0
 **/

import java.io.IOException;
import java.net.*;
import java.nio.channels.IllegalBlockingModeException;
import java.util.Map;
import java.util.UUID;

import ikrs.typesystem.BasicType;
import ikrs.typesystem.BasicNumberType;
import ikrs.yuccasrv.Constants;


public class ServerSocketThread
    extends Thread
    implements Runnable {

    public static final int DEFAULT_BACKLOG = 10;

    private InetAddress bindAddress;
    private int bindPort;
    private Map<String,BasicType> bindSettings;

    private ServerSocket serverSocket;

    private UUID uuid;

    private ServerSocketThreadObserver observer;

    /**
     * The constructor.
     **/
    protected ServerSocketThread( InetAddress bindAddress,
				  int bindPort,
				  Map<String,BasicType> bindSettings,
				  ServerSocketThreadObserver observer 				  
				  ) 
	throws IOException {	

	super();

	this.bindAddress = bindAddress;
	this.bindPort = bindPort;
	this.bindSettings = bindSettings;
	
	this.observer = observer;

	int backlog = DEFAULT_BACKLOG;
	if( bindSettings.get("BACKLOG") != null )
	    backlog = bindSettings.get("BACKLOG").getInt( DEFAULT_BACKLOG );

	this.serverSocket = new ServerSocket( bindPort,
					      backlog,
					      bindAddress 
					      );

	this.uuid = UUID.randomUUID();

    }

    public UUID getUUID() {
	return this.uuid;
    }

    public Map<String,BasicType> getServerSettings() {
	return this.bindSettings;
    }
    

    public void run() {
	
	try {
	    while( !this.isInterrupted() && !this.serverSocket.isClosed() ) {

		Socket sock = this.serverSocket.accept();

		// Increase connection counter
		BasicType count = this.bindSettings.get(Constants.KEY_CONNECTION_COUNT);
		if( count == null )
		    count = new BasicNumberType( 1 );
		else
		    count = new BasicNumberType( count.getInt() + 1 );
		this.bindSettings.put( Constants.KEY_CONNECTION_COUNT, count );

		this.observer.incomingTCPConnection( this, sock );
		
	    }
	    // END while: server was closed or thread was interrupted

	    if( !this.serverSocket.isClosed() ) 
		closeServerSocket();
	    else //if( !this.isInterrupted() )
		this.observer.serverSocketClosed( this );

	} catch( SocketTimeoutException e ) {

	    if( !this.isInterrupted() )
		this.observer.serverSocketException( this, e );

	} catch( IOException e ) {
	    
	    if( !this.isInterrupted() )
		this.observer.serverSocketException( this, e );

	} catch( SecurityException e ) {
	    
	    if( !this.isInterrupted() )
		this.observer.serverSocketException( this, e );

	} catch( IllegalBlockingModeException e ) {

	    if( !this.isInterrupted() )
		this.observer.serverSocketException( this, e );
	    
	}
	
    }

    public void interrupt() {

	super.interrupt();

	try {
	    closeServerSocket();
	} catch( IOException e ) {
	    // Ignore
	}

    }

    private void closeServerSocket() 
	throws IOException {
	if( this.serverSocket.isClosed() )
	    return; // false;

	//try {

	    this.serverSocket.close();
	    //this.observer.serverSocketClosed( this );
	    //return true;
	    
	    //} catch( IOException e ) {
	    
	    //this.observer.serverSocketException( this, e );
	    //return false;

	    //}
    }

    public void finalize() {
	// ..!
    }


}