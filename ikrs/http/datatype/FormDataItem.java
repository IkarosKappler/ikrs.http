package ikrs.http.datatype;

/**
 * 
 *
 * @author Ikaros Kappler
 * @date 2012-10-02
 * @version 1.0.0
 **/


import ikrs.http.HTTPHeaders;


public class FormDataItem {

    private String key;

    private String value;

    private HTTPHeaders headers;

    public FormDataItem( String key, String value ) {
	super();

	this.key    = key;
	this.value  = value;

	this.headers = new HTTPHeaders();
    }


    public String getKey() {
	return this.key;
    }

    public String getValue() {
	return this.value;
    }

    public HTTPHeaders getHeaders() {
	return this.headers;
    }

}