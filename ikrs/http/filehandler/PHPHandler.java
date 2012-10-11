package ikrs.http.filehandler;

/**
 * This is a PHP file handler. It passed the requested file to the PHP (php-cgi) interpreter
 * and stores the generated data inside a buffered resource.
 *
 * Future implementations might send the generated data in runtime (without a buffer), but
 * that would require a HTTP handler that does not expect the given data length when the
 * network output starts.
 *
 *
 * @author Ikaros Kappler
 * @date 2012-09-29
 * @version 1.0.0
 **/

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;

import ikrs.http.AbstractFileHandler;
import ikrs.http.DataFormatException;
import ikrs.http.DefaultPostDataWrapper;
import ikrs.http.FileHandler;
import ikrs.http.HeaderFormatException;
import ikrs.http.HTTPHandler;
import ikrs.http.HTTPHeaderLine;
import ikrs.http.HTTPHeaders;
import ikrs.http.PostDataWrapper;
import ikrs.http.Resource;
import ikrs.http.UnsupportedFormatException;
import ikrs.http.resource.InterruptableResource;
import ikrs.http.resource.ProcessableResource;
import ikrs.http.datatype.FormData;
import ikrs.io.BytePositionInputStream;
import ikrs.util.CustomLogger;

public class PHPHandler
    extends AbstractFileHandler {

     /**
     * Create a new PHPHandler.
     * 
     * @param logger A logger to write log messages to (must not be null).
     **/
    public PHPHandler( HTTPHandler handler, CustomLogger logger ) 
	throws NullPointerException {

	super( handler, logger );

	
    }
    

    //--- BEGIN ------------------------ FileHandler implementation ------------------------------
    /**
     * Is that a good idea?
     **/
    public Resource process( HTTPHeaders headers,
			     PostDataWrapper postData,
			     File file )
	throws IOException,
	       HeaderFormatException,
	       DataFormatException,
               UnsupportedFormatException {


	// If the request method is POST there might be some data to be processed.
	if( headers.isPOSTRequest() ) {
	    
	    try {
		// Before building the system command, we have to read/parse the POST form data
		this.getLogger().log( Level.INFO,
				      getClass().getName() + ".process(...)",
				      "The request is a POST request. Reading sent form data ..." );

		FormData formData = postData.readFormData();
		
		this.getLogger().log( Level.INFO,
				      getClass().getName() + ".process(...)",
				      "POST data parsed to form-data: " + formData );

	    } catch( HeaderFormatException e ) {
		
		this.getLogger().log( Level.INFO,
				      getClass().getName() + ".process(...)",
				      "[HeaderFormatException] Failed to process POST data: " + e.getMessage() );
		throw e;

	    } catch( DataFormatException e ) {
		
		this.getLogger().log( Level.INFO,
				      getClass().getName() + ".process(...)",
				      "[DataFormatException] Failed to process POST data: " + e.getMessage() );
		throw e;

	    } catch( UnsupportedFormatException e ) {
		
		this.getLogger().log( Level.INFO,
				      getClass().getName() + ".process(...)",
				      "[UnsupportedFormatException] Failed to process POST data: " + e.getMessage() );
		throw e;

	    }
	}
	    
	    


	java.util.List<String> command = new java.util.LinkedList<String>();

	// WARNING: php-cgi must be installed on the system
	command.add( "php-cgi" );
	command.add( file.getAbsolutePath() );  // This is the file argument for the PHP interpreter :)
	ProcessBuilder pb = new ProcessBuilder( command );

	this.getLogger().log( Level.INFO,
			      getClass().getName() + ".process(...)",
			      "Creating a processable resource using the PHP file '" + file.getPath() + "'. System command: " + command.toString() );

	ProcessableResource pr = 
	    new ProcessableResource( this.getHTTPHandler(),
				     this.getLogger(),  // new ikrs.util.DefaultCustomLogger( "ProcessableResource.main()_TEST" ),
				     pb,
				     false   // useFairLocks not necessary here; there will be one more resource wrapper
				     );


	
	// The processable resource stores the system-process's output inside an internal buffer.
	// Now we can read the PHP's generated header data using an InterruptableResource.
	InterruptableResource ir = new InterruptableResource( this.getHTTPHandler(),
							      this.getLogger(),
							      pr,
							      true    // useFairLocks (this will be the returned instance)
							      );
	

	this.getLogger().log( Level.INFO,
			      getClass().getName() + ".process(...)",
			      "PHP output received. Reading generated HTTP headers from InterruptableResource ..." );

	// Note that the InterruptableResource allows to simulate the inputstream to be closed,
	// which is nothing more than a byte position reset.
	ir.open( true ); // open in read-only mode


	// Warning: even if the process was executed without any exceptions the system process may have failed!
	// -> check the return code
	if( pr.getExitValue() == 0 ) {
	    
	    // ???
	    // Store exit code in the resource's meta data?
	}




	// Continue ...

	BytePositionInputStream in = ir.getInputStream();
	HTTPHeaders phpHeaders = HTTPHeaders.read( in );

	try {

	    // Store the generated HTTP headers into the resource's MetaData object.
	    for( int i = 0; i < phpHeaders.size(); i++ ) {


		// PHP creates a special 'Status' line indictating the HTTP respones status.
		// This status has the format "<status_code> <reason_phrase>" (hopefully) and must be converted into the 
		// HTTP-conform header line "HTTP/1.x <status_code> <reason_phrase>".
		HTTPHeaderLine headerLine = phpHeaders.get(i);
		if( headerLine.getKey() != null && headerLine.getKey().equals("Status") ) {

		    // Convert the 'Status' line into the 'HTTP/1.1 <status> <reason_phrase>'
		    // Note: This might throw a HeaderFormatException!
		    HTTPHeaderLine newResponseLine = new HTTPHeaderLine( "HTTP/1.1 " + headerLine.getValue(), 
									 null 
									 );
		    this.getLogger().log( Level.INFO,
					  getClass().getName() + ".process(...)",
					  "Converting PHP-generated HTTPHeaders["+i+"] to a new status line and adding to the resource's meta data: " + phpHeaders.get(i) + ", status line replacement: " + newResponseLine );

		    ir.getMetaData().getOverrideHeaders().replaceResponseLine( newResponseLine );

		} else {

		    // A 'normal' key-value-tuple.
		    this.getLogger().log( Level.INFO,
					  getClass().getName() + ".process(...)",
					  "Adding PHP-generated HTTPHeaders["+i+"] to the resource's meta data: " + phpHeaders.get(i) );

		    ir.getMetaData().getOverrideHeaders().add( headerLine );
		    
		}
	    }

	} catch( HeaderFormatException e ) {

	    ir.close();
	    throw new IOException( "Cannot replace response status line due to HeaderFormatException: " + e.getMessage(),
				   e );
		

	} 

	
	// Now reset the input stream!
	// This will tell the next instance accessing the resource that it's still at the beginning of the 
	// input stream (and the second 'open()' call will not fail).
	ir.resetBytePosition();

	/*
	  System.out.println( "Reading from BytePositionInputStream after resetting ..." );
	  int b;
	  while( (b = in.read()) != -1 )
	  System.out.print( (char)b );
	*/


	    
	return ir;
	    
    }

    //--- END -------------------------- FileHandler implementation ------------------------------

}
