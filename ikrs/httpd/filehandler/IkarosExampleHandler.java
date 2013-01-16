package ikrs.httpd.filehandler;

/**
 * A small example handler.
 *
 *
 * @author Ikaros Kappler
 * @date 2013-01-06
 * @version 1.0.0
 **/

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import ikrs.httpd.AbstractFileHandler;
import ikrs.httpd.Constants;
import ikrs.httpd.DataFormatException;
import ikrs.httpd.DefaultPostDataWrapper;
import ikrs.httpd.FileHandler;
import ikrs.httpd.HeaderFormatException;
import ikrs.httpd.HTTPHandler;
import ikrs.httpd.HTTPHeaderLine;
import ikrs.httpd.HTTPHeaders;
import ikrs.httpd.PostDataWrapper;
import ikrs.httpd.Resource;
import ikrs.httpd.UnsupportedFormatException;
import ikrs.httpd.resource.ByteArrayResource;
//import ikrs.httpd.resource.InterruptableResource;
//import ikrs.httpd.resource.ProcessableResource;
import ikrs.httpd.datatype.FormData;
import ikrs.io.BytePositionInputStream;
import ikrs.util.CustomLogger;
import ikrs.util.Environment;
import ikrs.util.KeyValueStringPair;
import ikrs.util.MIMEType;
import ikrs.util.session.Session;
import ikrs.typesystem.BasicType;

public class IkarosExampleHandler
    extends AbstractFileHandler {



    public IkarosExampleHandler() {
	super();

	
    }


    //--- BEGIN ------------------------ FileHandler implementation ------------------------------
    /**
     * Most file handlers operate on existing files that are located inside the local file 
     * system (such as the default handler does for simple file delivery).
     *
     * Some file handlers operate on virtual file systems, which means that the request URI does
     * not necessarily address an existing file but a symbol only the handler may know. 
     *
     * The global HTTP handler needs to know if to throw a MissingResourceException (resulting
     * in a 404) if a requested file does not exists --- or if to ignore that fact and simply 
     * continue.
     *
     * This method tells how to proceed.
     *
     * If your implementation returns true this handler will not be called at all; the request
     * processing will directly stop raising an HTTP status 404.
     *
     * @return true if this file handler definitely requires existing files. The process(...) 
     *              method will never be called if the requested file does not exist in that case.
     **/
    public boolean requiresExistingFile() {

	// Requested files do not necessarily have to exist :)
	return false;
    }

    /**
     * The 'process' method is very generic. It depends on the underlying implementation how the passed
     * file should be processed.
     *
     * @param sessionID   The current session's ID.
     * @param headers     The HTTP request headers.
     * @param postData    The HTTP post data; if the method is not HTTP POST the 'postData' should be null
     *                    (or empty).
     * @param file        The requested file itself (inside the local file system).
     * @param requestURI  The requested URI (relative to DOCUMENT_ROOT).
     **/
    public Resource process( UUID sessionID,
			     HTTPHeaders headers,
			     PostDataWrapper postData,
			     File file,
			     URI requestURI )
	throws IOException,
	       HeaderFormatException,
	       DataFormatException,
               UnsupportedFormatException {
	    

	getLogger().log( Level.INFO,
			 getClass().getName() + ".process(...)",
			 "Processing. requestURI=" + requestURI + ". file=" + file.getAbsolutePath() );
	
	
	/*
	ProcessableResource cgiOutput = 
	    new ProcessableResource( getHTTPHandler(),
				     getLogger(),
				     pb,
				     postData,  // Should be null if HTTP method is not POST
				     false      // useFairLocks not necessary here; there will be one more resource wrapper
				     );
	*/


	Session<String,BasicType,ikrs.httpd.HTTPConnectionUserID> session = this.getHTTPHandler().getSessionManager().get( sessionID );
	

	String data = 
	    "This is a test\n" +
	    "--------------\n" +
	    "\n" +
	    "This is the example output of a customized java written file-/directory- handler:\n" +
	    "Class=" + this.getClass().getName() + "\n" +
	    "\n" +
	    "\n" +
	    "You want to access URI: " + requestURI.toString() + "\n" +
	    "File exists in local FS: " + file.exists() +"\n" +
	    "Request Headers: \n";
	for( int i = 0; i < headers.size(); i++ )
	    data += "   " + headers.get(i) + "\n";

	data +=
	    "Request Method:      " + headers.getRequestMethod() + "\n" +
	    "Request Version:     " + headers.getRequestVersion() + "\n" +
	    "Request Protocol:    " + headers.getRequestProtocol() + "\n" +
	    "Request URI:         " + headers.getRequestURI() + "\n" +
	    "POST data available: " + (postData!=null) + "\n" +
	    "\n" +
	    "Session:             " + session + "\n" +
	    "Testvalue:           " + session.get("TEST") + "\n" +
	    "                     (this value is null on first call or if your session expired)\n" +
	    "\n" +
	    "\n";

	session.put( "TEST", new ikrs.typesystem.BasicStringType("123456") );


	ByteArrayResource resource = new ByteArrayResource( this.getHTTPHandler(),
							    this.getLogger(),
							    data.getBytes(),
							    false   // no need to use fair locks
							    );
	MIMEType mimeType = MIMEType.getByFileExtension( "txt" );
	resource.getMetaData().setMIMEType( mimeType );
		
	return resource;
    }

    //--- END -------------------------- FileHandler implementation ------------------------------



}
