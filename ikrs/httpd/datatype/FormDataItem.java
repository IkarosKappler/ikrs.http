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

import java.io.InputStream;

public class FormDataItem {
    //    extends KeyValueStringPair {

    private HTTPHeaders headers;

    private InputStream inputStream;

    /*public FormDataItem() { // String key, String value ) {
	super( null, null ); // key, value );

	//this.key    = key;
	//this.value  = value;

	this.headers = new HTTPHeaders();
    }
    */

    public FormDataItem( HTTPHeaders headers,
			 InputStream in ) {
	super();
	
	this.headers     = headers;
	this.inputStream = in;
    }

    public HTTPHeaders getHeaders() {
	return this.headers;
    }

    public InputStream getInputStream() {
	return this.inputStream;
    }

    //public void setInputStream( InputStream in ) {
    //	return this.inputStream;
    //}

    public String toString() {
	return this.toString( new StringBuffer() ).toString();
    }

    public StringBuffer toString( StringBuffer b ) {
	b.append( this.getClass().getName() ).append( "=[" ).
	    append( " headers: " );
	this.headers.toString( b );
	b.append( ", inputStream=" ). append( (this.inputStream==null?"null":this.inputStream.getClass().getName()) ).append( "<unknown length>" ).
	    append( " ]" );
	return b;
    }

}