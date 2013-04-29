package ikrs.httpd.datatype;


import java.util.Set;

/**
 * FormData instances represent lists/maps of named items, each a FormDataItem.
 *
 * Each FormData instance has an (optional) successor; the successor is not null
 * if the form data was sent using 'multipart/form-data' and the current form data
 * is not the last one.
 *
 *
 * @author Ikaros Kappler
 * @date 2012-10-02
 * @modified 2013-03-20 Ikaros Kappler [Method keySet() added].
 * @version 1.0.1
 **/


public interface FormData {

    /**
     * This method returns the FormDataItem with the given map key.
     * 
     * @param name The item's name.
     * @return The item with the given name or null if no such key
     *         can be found.
     * @throws NullPointerException If the passed name is null.
     **/
    public FormDataItem get( String name )
	throws NullPointerException;

    public FormDataItem get( int index )
	throws ArrayIndexOutOfBoundsException;

    /**
     * Get the internal map's key set.
     * Note that the returned set will be immutable!
     *
     * @return The set of all keys mapped in the internal map.
     *         The returned set is never null.
     **/
    public Set<String> keySet();

    /**
     * Get the nunber of keys that are mapped inside this form data wrapper.
     *
     * @return The number of mapped keys.
     **/
    public int size();

    /**
     * Add a new FormDataItem.
     *
     * If a different item with the same key already exists it will be
     * overwritten.
     *
     * @param item The form data item to be added.
     * @throws NullPointerException If the passed item is null.
     **/
    public void add( FormDataItem item ) 
	throws NullPointerException;

    /**
     * If the post data was sent via Content-Type=multipart/form-data
     * there must be a multipart boundary.
     *
     * @return The multipart boundary, if present; null otherwise.
     **/
    //public String getMultipartBoundary();

    /**
     * If the whole post data was sent via Content-Type=multipart/form-data
     * there are several form data items available, all seperated by the
     * passed multipart boundady.
     *
     * If this is the case and the current (this) form data instance is NOT
     * the last one, this method will return its non-null successor (null
     * otherwise).
     *
     * @return The next form data successor if available.
     **/
    //public FormData getSuccessor();
}