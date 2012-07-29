package ikrs.http;

/**
 * This class wraps HTTPHeaderLines together into a list like searchable structure.
 *
 * @author Henning Diesenberg
 * @date 2012-05-21
 * @version 1.0.0
 **/


import java.io.EOFException;
import java.io.InputStream;
import java.io.IOException;


import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


//import ikrs.util.CaseInsensitiveComparator;


public class HTTPHeaders {
    //extends ArrayList<HTTPHeaderLine> {

    /**
     * The method field ot the request line.
     **/
    private String requestMethod;
    
    /**
     * The protocol field ot the request line.
     **/
    private String requestProtocol;
    
    /**
     * The version field of the request line.
     **/
    private String requestVersion;
    
    /**
     * The URI field of the request line.
     **/
    private String requestURI;

    private Map<String,Set<HTTPHeaderLine>> map;
    private ArrayList<HTTPHeaderLine> list;


    public HTTPHeaders() {
	super();


	this.list = new ArrayList<HTTPHeaderLine>(4);
	this.map = new HashMap<String,Set<HTTPHeaderLine>>(); // CaseInsensitiveComparator.sharedInstance );
    }

    /**
     * This method registers the given HTTPHeader line into the search map.
     **/
    private void registerToMap( HTTPHeaderLine element ) {
	
	Set<HTTPHeaderLine> set = this.map.get( element.getKey() );
	if( set == null ) {
	    // Container not found -> create new
	    set = new TreeSet<HTTPHeaderLine>();
	    this.map.put( element.getKey(), set );
	}
	
	set.add( element );
    }

    /**
     * This method releases the given HTTPHeaderLine from the search map.
     **/
    private boolean releaseFromMap( HTTPHeaderLine element ) {
	
	Set<HTTPHeaderLine> set = this.map.get( element.getKey() );
	if( set == null ) {
	    // Container not found
	    return false;
	}

	boolean removed = set.remove( element );
	if( !removed ) {
	    // Not found in container
	    return false;
	}

	// Fully remove container?
	if( set.size() == 0 ) {

	    this.map.remove( element.getKey() );

 	} 

	return true;
    }

    public Set<HTTPHeaderLine> getAll( String name ) {
	// Get the set widht the elements
	Set<HTTPHeaderLine> set = this.map.get( name );
       

	// make a copy
	Set<HTTPHeaderLine> result = new TreeSet<HTTPHeaderLine>();

	if( set == null )
	    return result;


	Iterator<HTTPHeaderLine> iter = set.iterator();
	while( iter.hasNext() ) {
	    
	    // Hint: HTTPHeaderLines are immutable -> no need to clone
	    result.add( iter.next() );

	}
	
	return result;
    }

    /**
     * This method returns a random header line with the given key.
     **/
    public HTTPHeaderLine get( String name ) {
	// Get the set widht the elements
	Set<HTTPHeaderLine> set = this.map.get( name );
       
	if( set == null || set.size() == 0 )
	    return null;

	Iterator<HTTPHeaderLine> iter = set.iterator();

	return iter.next();
    }

    /**
     * This method adds a new header line to this headers object.
     *
     * This method just calls add( new HTTPHeaderLine(key,value) ).
     *
     * @param key The new line's key.
     * @param value Thw new line's value.
     * @throws NullPointerException If the key is null.
     **/
    public boolean add( String key, String value ) 
	throws NullPointerException {

	if( key == null )
	    throw new NullPointerException( "Cannot add header with null-key." );

	return this.add( new HTTPHeaderLine(key,value) );
    }

    /**
     * This method adds a new header line to this headers object.
     *
     *
     * @param key The new line's key.
     * @throws NullPointerException If the line is null.
     **/
    public boolean add( HTTPHeaderLine e ) 
	throws NullPointerException {

	if( e == null )
	    throw new NullPointerException( "Cannot add null line to HTTP headers." );

	boolean b = this.list.add( e );
	if( b )
	    this.registerToMap( e );

	return b;
    }
        
    /*public void add ( int index, HTTPHeaderLine element ) 
	throws IndexOutOfBoundsException {

	this.list.add( index, element );
	this.registerToMap( element );
	}*/
    
    /*
    public void	clear() {
	this.list.clear();
	this.map.clear();
    }
    */
      
    /*
    public Object clone() {
	return null;
    }
    */
     
    public HTTPHeaderLine get( int index ) 
	throws IndexOutOfBoundsException {
	return this.list.get( index );
    }

    public Iterator<HTTPHeaderLine> iterator() {
	return this.list.iterator();
    }
   
    /*
    public HTTPHeaderLine remove(int index)  
	throws IndexOutOfBoundsException {

	HTTPHeaderLine h = this.list.remove(index);
	if( h != null )
	    this.releaseFromMap( h );

	return h;
    }
    */
              
    /*
    public HTTPHeaderLine set( int index, HTTPHeaderLine element )  
	throws IndexOutOfBoundsException {
	
	// Find current element
	HTTPHeaderLine old = this.list.get( index );
	this.registerToMap( element );
	
	return old;
    } 
    */
    //---END--------------------------- Override List's access methods ------------------

    public int size() {
	return this.list.size();
    }

    public String getRequestMethod() {
	if( this.requestMethod == null )
	    this.parseRequestLine();

	return this.requestMethod;
    }

    public String getRequestProtocol() {
	if( this.requestProtocol == null )
	    this.parseRequestLine();

	return this.requestProtocol;
    }

    public String getRequestVersion() {
	if( this.requestVersion == null )
	    this.parseRequestLine();

	return this.requestVersion;
    }

    public String getRequestURI() {
	if( this.requestURI == null )
	    this.parseRequestLine();

	return this.requestURI;
    }


    private boolean parseRequestLine() {

	if( this.list.size() == 0 )
	    return false;
	
	HTTPHeaderLine first = this.list.get(0);
	String key = first.getKey();
	if( key == null )
	    return false;


	// The first line should have this format (or like that):
	// GET / HTTP/1.1
	String[] split = key.split( "(\\s)++" );
	//print( split );
	if( split.length < 3 )
	    return false;
	

	this.requestMethod = split[0];
	this.requestURI = split[1];

	// Parse request HTTP-Version
	String[] split2 = split[2].split("(/)");
	this.requestVersion = split2[0];	
	if( split2.length >= 2 )
	    this.requestProtocol = split2[1];


	return true;
    }

    public static HTTPHeaders read( InputStream in )
	throws EOFException,
	IOException {

	
	HTTPHeaders headers = new HTTPHeaders();


	HTTPHeaderLine header = null;
	// Read line by line; the first occurence of an empty line indicates the end-of-headers, which means
	// the HTTPHeaderLine.read(...) returns null.
	while( (header = HTTPHeaderLine.read(in)) != null ) {
	    System.out.println( "HTTPHeaders.read(...) HTTPHeaders.read(..) header line: " + header );
	    
	    headers.add( header );
	}

	
	return headers;
    }

    /*private static void print( String[] str ) {

	for( int i = 0; i < str.length; i++ ) {
	    System.out.print( str[i] );
	    System.out.print( " | " );
	}

	}*/

}