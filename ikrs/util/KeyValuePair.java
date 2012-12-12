package ikrs.util;

/**
 * A very simple key-value-pair implementation.
 *
 * @author Ikaros Kappler
 * @date 2012-12-12
 * @version 1.0.0
 **/

public class KeyValuePair<K,V> {

    /**
     * The key (may be null).
     **/
    private K key;
    
    /**
     * The value (may be null).
     **/
    private V value;

    /**
     * Construct a new key-value-pair. Both params may be null.
     *
     * @param key   The key (may be null).
     * @param value The value (may be null).
     **/
    public KeyValuePair( K key, V value ) {
	super();

	this.key   = key;
	this.value = value;
    }


    /**
     * Get the key from this pair.
     *
     * @return The key from this pair.
     **/
    public K getKey() {
	return this.key;
    }

    /**
     * Get the value from this pair.
     *
     * @return The value from this pair.
     **/
    public V getValue() {
	return this.value;
    }


    public static KeyValuePair<String,String> splitLine( String line,
							 String separator ) {

	if( line == null )
	    return null;


	KeyValuePair<String,String> pair = new KeyValuePair<String,String>( null, null );

	if( line.length() == 0 )
	    return pair;


	if( separator == null || separator.length() == 0 ) {

	    pair.key = line;
	    return pair;

	}


	int index = line.indexOf( separator );
	if( index == -1 ) {
	    pair.key = line;
	    return pair;
	}

	if( index == 0 ) {
	    
	    // separator at beginning   
	    pair.key = "";
	    if( line.length() > separator.length() )
		pair.value = line.substring(separator.length()).trim();
	    else
		pair.value = "";

	} else if( index >= line.length()-separator.length() ) {

	    // separator at end of line
	    pair.key = line.substring(0,index).trim();
	    pair.value = "";

	} else {
	    // separator anywhere in the middle
	    pair.key = line.substring(0, index).trim();
	    pair.value = line.substring(index+separator.length()).trim();

	}
	
	
	return pair;

    }


}