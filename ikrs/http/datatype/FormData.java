package ikrs.http.datatype;

/**
 * 
 *
 * @author Ikaros Kappler
 * @date 2012-10-02
 * @version 1.0.0
 **/


import java.util.ArrayList;
import java.util.List;


public interface FormData {

    /**
     * Isn't this a normal map?
     **/
    public FormDataItem get( String name );

    public int size();

    public void add( FormDataItem item ) 
	throws NullPointerException;

}