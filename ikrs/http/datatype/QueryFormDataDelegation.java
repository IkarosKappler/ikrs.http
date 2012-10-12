package ikrs.http.datatype;

/**
 * The QueryFormDataDelegation is a wrapper class that wraps a single Query object (a key-value-map)
 * into a FormData instance.
 *
 * @author Ikaros Kappler
 * @date 2012-10-04
 * @version 1.0.0
 **/


import java.util.ArrayList;
import java.util.List;


public class QueryFormDataDelegation
    extends AbstractFormData {

    /**
     * The query to be mapped to the FormData specification.
     **/
    private Query query;

    
    /**
     * Constructs a new QueryFormDataDelegation.
     **/
    public QueryFormDataDelegation( Query query ) 
	throws NullPointerException {

	super();

	if( query == null )
	    throw new NullPointerException( "Cannot create a QueryFormDataDelegation from a null-query." );

	this.query = query;
    }


    /**
     * Isn't this a normal map?
     **/
    public FormDataItem get( String name ) {
	
	String value = this.query.getParam( name );
	if( value == null )
	    return null;

	return new FormDataItem( name, value );
    }

    public int size() {
	return this.query.size();
    }

    public void add( FormDataItem item ) 
	throws NullPointerException {

	if( item == null )
	    throw new NullPointerException( "Cannot add null-items to QueryFormDataDelegation instances." );

	this.query.addParam( item.getKey(),
			     item.getValue() );	
    }


    public String toString() {
	return this.query.toString();
    }

}