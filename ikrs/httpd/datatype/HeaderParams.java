package ikrs.httpd.datatype;

/**
 * This is a simple parser class that splits several header line formats.
 *
 * Example:
 *  the header line (from the 'Accept-Charset' header)
 *    'ISO-8859-1,utf-8;q=0.7,*;q=0.3'
 *  would be split into this two-dimensional structure:
 *  { 
 *    { 'ISO-8859-1' },
 *
 *    { 'utf-8', 'q=0.7' },
 *
 *    { '*', 'q=0.3' }
 *  }
 *

 *
 * 
 *
 * @author Ikaros Kappler
 * @date 2012-10-04
 * @version 1.0.0
 **/

import java.util.ArrayList;
import java.util.List;

public class HeaderParams {

    /**
     * The header key.
     **/
    private String headerKey;
    
    /**
     * The original raw header value (unparsed).
     **/
    private String headerValue;

    /**
     * The parsed header value as a nested list.
     **/
    private List<List<String>> params;


    /**
     * Creates a new HeaderParams instance from the given key/value pair.
     *
     * @param headerKey   The header's key (must not be null).
     * @param headerValue The header's value (must not be null).
     * @throws NullPointerException If the the passed key or value is null.
     **/
    public HeaderParams( String headerKey,
			 String headerValue ) 
	throws NullPointerException {


	if( headerKey == null )
	    throw new NullPointerException( "Cannot create HeaderParams with a null-key." );
	if( headerValue == null )
	    throw new NullPointerException( "Cannot create HeaderParams with a null-value." );


	this.headerKey   = headerKey;
	this.headerValue = headerValue;

	this.params      = new ArrayList<List<String>>( 1 );

	HeaderParams.parse( headerValue,
			    this.params );
    }

    public String getMainToken() {	
	return this.getToken( 0, 0 );
    }

    public int getLevelCount() {
	return this.params.size();
    }

    public int getSublevelCount( int levelA ) {

	if( levelA < 0 || levelA >= this.getLevelCount() )
	    return -1;

	List<String> list = this.params.get( levelA );
	return list.size();
    }

    public String getTokenByPrefix( int levelA,
				    String prefix,
				    boolean caseSensitive ) {

	if( levelA < 0 || levelA >= this.getLevelCount() )
	    return null;

	List<String> list = this.params.get( levelA );

	System.out.println( "LevelA=" + list );

	for( int i = 0; i < list.size(); i++ ) {

	    String token = list.get(i);

	    System.out.println( "Current token: " + token );

	    if( caseSensitive && token.startsWith(prefix) )
		return token;
	    else if( !caseSensitive && token.toLowerCase().startsWith(prefix.toLowerCase()) )
		return token;

	}
	
	return null;
    }

    public String getToken( int levelA,
			    int levelB ) {

	if( levelA < 0 || levelA >= this.getLevelCount() || levelB < 0 )
	    return null;
	
	List<String> list = this.params.get( levelA );
	if( levelB >= list.size() )
	    return null;

	return list.get( levelB );
    }

    public static void parse( String value,
			      List<List<String>> destination ) 
	throws NullPointerException {

	if( value == null )
	    throw new NullPointerException( "Cannot parse HeaderParams from a null-value." );

	// The value might be 
	//  - a single token
	//  - or a list of tokens, separated by ','
	//  - or a nested list of tokens; first level separated by ',' and the second level separated by ';'

	String[] tokensA = value.split( "," );
	
	for( int a = 0; a < tokensA.length; a++ ) {

	    if( (tokensA[a] = tokensA[a].trim()).length() == 0 )
		continue;

	    String[] tokensB = tokensA[a].split( ";" );
	    List<String> sublist = new ArrayList<String>( tokensB.length );
	    for( int b = 0; b < tokensB.length; b++ )
		sublist.add( tokensB[b].trim() );

	    destination.add( sublist );
	}


    }



    public static void main( String[] argv ) {

	String str = "ISO-8859-1,utf-8;q=0.7,*;q=0.3";

	HeaderParams prms = new HeaderParams( "Accept-Charset",
					      str );

	System.out.println( prms.params );

    }

}