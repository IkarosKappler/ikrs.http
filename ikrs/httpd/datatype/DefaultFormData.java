package ikrs.httpd.datatype;

/**
 * 
 *
 * @author Ikaros Kappler
 * @date 2012-10-04
 * @version 1.0.0
 **/


import java.util.ArrayList;
import java.util.List;


public class DefaultFormData 
    extends AbstractFormData {

    /**
     * An internal list to store the FormDataItems.
     **/
    private List<FormDataItem> list;


    /**
     * Constructs a new DefaultFormData instance.
     **/
    public DefaultFormData() {
	super();

	this.list = new ArrayList<FormDataItem>();
    }


    /**
     * Isn't this a normal map?
     **/
    public FormDataItem get( String name ) {
	
	// Search 
	// !!! (uuhhmmm ... eeehhmmm ... a linear search!?? this can be optimized!)
	
	for( int i = 0; i < this.list.size(); i++ ) {
	    
	    if( this.list.get(i).getKey().equals(name) )
		return this.list.get(i);

	}

	// Not found.
	return null;
    }

    public int size() {
	return this.list.size();
    }

    //public FormDataItem get( int index ) {
    //	return this.list.get( index );
    //}

    public void add( FormDataItem item ) 
	throws NullPointerException {

	if( item == null )
	    throw new NullPointerException( "Cannot add null-items to FormData instances." );

	this.list.add( item );
    }

}
