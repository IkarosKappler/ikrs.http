package ikrs.http;

/**
 * @author Ikaros Kappler
 * @date 2012-07-29
 * @version 1.0.0
 **/

import ikrs.util.MIMEType;

public class ResourceMetaData {

    private MIMEType mimeType;

    public ResourceMetaData() {
	
    }


    public void setMIMEType( MIMEType mimeType ) {
	this.mimeType = mimeType;
    }

    public MIMEType getMIMEType() {
	return this.mimeType;
    }


}