package ikrs.httpd;

/**
 * The ResourceMetaData is a lightweight class that holds different descriptional
 * resource meta data such as the date of last modification, length (if available)
 * or the resource's URI. 
 *
 *
 * @author Ikaros Kappler
 * @date 2012-07-29
 * @version 1.0.0
 **/

//import java.net.URI;
import java.util.Date;

import ikrs.httpd.HTTPHeaders;
import ikrs.util.MIMEType;

public class ResourceMetaData {

    private MIMEType mimeType;

    /**
     * Some resources (such as PHP resource) may produce customized HTTP headers.
     **/
    private HTTPHeaders overrideHeaders;

    /**
     * The resource's URI.
     **/
    //private URI relativeURI;

    /**
     * The resource's last modification date.
     **/
    private Date lastModified;

    /**
     * The resource's length (int bytes).
     **/
    //private long length;


    public ResourceMetaData() {
	super();

	this.overrideHeaders = new HTTPHeaders();
	
	//this.relativeURI     = relativeURI;
	//this.lastModified    = lastModified;
	//this.length          = length;
    }


    public void setMIMEType( MIMEType mimeType ) {
	this.mimeType = mimeType;
    }

    public MIMEType getMIMEType() {
	return this.mimeType;
    }

    /**
     * Get the resource's override-headers. 
     * The returned headers-object is never null (but may be empty).
     *
     * @return The resource's override-headers (is never null).
     **/
    public HTTPHeaders getOverrideHeaders() {
	return this.overrideHeaders;
    }

    public void setLastModified( Date lastModified ) {
	this.lastModified = lastModified;
    }

    public Date getLastModified() {
	return this.lastModified;
    }
    

}