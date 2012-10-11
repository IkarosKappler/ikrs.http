package ikrs.http;

/**
 * @author Ikaros Kappler
 * @date 2012-10-02
 * @version 1.0.0
 **/

import java.io.InputStream;
import java.io.IOException;

import ikrs.http.datatype.FormData;


public interface PostDataWrapper {

    
   /**
     * Get the HTTP headers assiciated with this post data.
     * The returned headers are never null (but may be empty).
     * 
     * @return The HTTP headers assiciated with this post data.
     **/
    public HTTPHeaders getRequestHeaders();

    /**
     * Get the input stream associated with this post data.
     * Use it to read the data you are interested in; the returned stream
     * is never null.
     *
     * @return The input stream to read the actual post data from.
     **/
    public InputStream getInputStream();

    /**
     * Get the current read position. 
     * The input stream is backed to a BytePositionInputStream that keeps track
     * of the read position inside the stream. 
     * The position is measured from the first byte of post data (header data 
     * not included).
     *
     * @return The current read position of the post data input stream.
     **/
    public long getBytePosition();


    /**
     * ??? ... !!!
     **/
    public FormData readFormData()
	throws IOException,
	       HeaderFormatException,
	       DataFormatException,
	       UnsupportedFormatException;

}