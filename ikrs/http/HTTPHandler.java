package ikrs.http;

/**
 * @author Henning Diesenberg
 * @date 2012-05-15
 * @version 1.0.0
 **/


import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.DatagramSocket;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;


import ikrs.yuccasrv.TCPAdapter;
import ikrs.yuccasrv.socketmngr.BindManager;

import ikrs.util.CustomLogger;
import ikrs.util.DefaultCustomLogger;

public class HTTPHandler 
    extends TCPAdapter 
    implements RejectedExecutionHandler {

    private ThreadPoolExecutor threadPoolExecutor;

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


    public HTTPHandler() {
	super();
	
	this.logger = new DefaultCustomLogger( Constants.NAME_DEFAULT_LOGGER );
	this.logger.setLevel( Level.ALL );

	this.requestQueue = new ArrayBlockingQueue<Runnable>( 20 );
	this.threadFactory = new HTTPServerThreadFactory( this,  // HTTPHandler
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

	this.responseBuilder = new DefaultResponseBuilder( this );

	// Pre start core thread?
	// this.executorService.prestartCoreThread();
    }

    public ResponseBuilder getResponseBuilder() {
	return this.responseBuilder;
    }

    public CustomLogger getLogger() {
	return this.logger;
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
					     Socket sock ) {
	
	enqueue( source, socketID, sock );
	
	
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
			  Socket sock ) {

	this.logger.log( Level.INFO,
			 getClass().getName(),
			 "Incoming HTTP connection from " + sock.getInetAddress().getHostAddress() + "." 
			 );
	HTTPRequestDistributor requestDistributor = 
	    new HTTPRequestDistributor( this,
					this.logger,
					socketID,
					sock
					);
	this.threadPoolExecutor.execute( requestDistributor );

    }
    

}