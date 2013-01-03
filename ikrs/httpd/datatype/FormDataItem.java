package ikrs.httpd.datatype;

/**
 * 
 *
 * @author Ikaros Kappler
 * @date 2012-10-02
 * @version 1.0.0
 **/


import ikrs.httpd.HTTPHeaders;
import ikrs.util.KeyValueStringPair;


public class FormDataItem 
    extends KeyValueStringPair {

    // private HTTPHeaders headers;

    public FormDataItem( String key, String value ) {
	super( key, value );

	//this.key    = key;
	//this.value  = value;

	// this.headers = new HTTPHeaders();
    }

}