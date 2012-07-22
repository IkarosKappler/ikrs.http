package ikrs.http;

/**
 * This interface is meant to wrap prepared HTTP reply objects.
 *
 * @author Ikaros Kappler
 * @date 2012-07-16
 * @version 1.0.0
 **/

import java.io.IOException;

public interface PreparedHTTPResponse {


    /**
     * This method must fully prepare the HTTP response. It is not acceptable that any steps that can be done
     * during preparation are made during the execute()-process!
     *
     * This means that all required ressources must be acquired (use locks), all headers prepared (by the use
     * of addResponseHeader(String,String) or getResponseHeaders()) and perform all necessary security checks.
     *
     * @throws MalformRequestException If the passed HTTP request headers are malformed and cannot be processed.
     * @throws UnsupportedVersionException If the headers' HTTP version is not supported (supported versions are
     *                                     1.0 and 1.1).
     * @throws UnknownMethodException If the headers' method (from the request line) is unknown.
     * @throws SecurityException If the request cannot be processed due to security reasons.
     * @throws IOException If any IO errors occur.
     **/
    public void prepare() 
	throws MalformedRequestException,
	       UnsupportedVersionException,
	       UnknownMethodException,
	       SecurityException,
	       IOException; 

    /**
     * This method executes the prepared response; this means that all necessary resources will be accessed,
     * the actual reply built and sent back to the client.
     *
     * Note that all resources already need to be aquired and all security checks to be done _before_ this
     * method is called.
     *
     * @throws IOException If any IO errors occur.
     **/
    public void execute()
	throws IOException;


    /**
     * This method will be called in the final end - even if the execute() method failed.
     *
     * It has to clean up, release resources and all locks!
     **/
    public void dispose();

}
