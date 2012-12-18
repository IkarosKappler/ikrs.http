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


public abstract class AbstractFormData 
    implements FormData {
   
    /**
     * Constructs a new AbstractFormDate instance.
     **/
    public AbstractFormData() {
	super();

    }


    /**
     * Isn't this a normal map?
     **/
    public abstract FormDataItem get( String name );

    public abstract int size();

    //public abstract FormDataItem get( int index );

    public abstract void add( FormDataItem item ) 
	throws NullPointerException;

}