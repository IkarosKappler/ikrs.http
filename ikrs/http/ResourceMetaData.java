package ikrs.http;

/**
 * @author Ikaros Kappler
 * @date 2012-07-29
 * @version 1.0.0
 **/

import ikrs.http.HTTPHeaders;
import ikrs.util.MIMEType;

public class ResourceMetaData {

    private MIMEType mimeType;

    /**
     * Some resources (such as PHP resource) may produce customized HTTP headers.
     **/
    private HTTPHeaders overrideHeaders;


    public ResourceMetaData() {
	super();

	this.overrideHeaders = new HTTPHeaders();
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

}