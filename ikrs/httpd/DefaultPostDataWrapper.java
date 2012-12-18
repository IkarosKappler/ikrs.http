package ikrs.httpd;

/**
 * @author Ikaros Kappler
 * @date 2012-10-02
 * @version 1.0.0
 **/


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.logging.Level;

import ikrs.httpd.datatype.FormData;
import ikrs.httpd.datatype.HeaderParams;
import ikrs.httpd.datatype.KeyValueStringPair;
import ikrs.httpd.datatype.Query;
import ikrs.httpd.datatype.QueryFormDataDelegation;
//import ikrs.io.ByteSequenceTokenizer;
import ikrs.io.MultipartMIMETokenizer;
import ikrs.util.CustomLogger;


public class DefaultPostDataWrapper
    extends AbstractPostDataWrapper {

    
    
    /**
     * Creates a new DefaultPostDataWrapper.
     *
     * @param headers The HTTPHeaders (usually read from the input stream before; must not be null).
     * @param in      The input stream to read the post data from (must not be null).
     * @throws NullPointerException If headers or in is null.
     **/
    public DefaultPostDataWrapper( CustomLogger logger,
				   HTTPHeaders headers,
				   InputStream in ) 
	throws NullPointerException {

	super( logger, headers, in );


    }

    //--- BEGIN ---------------------- AbstractPostDataWrapper implementation -----------------------
    /**
     * ??? ... !!!
     **/
    public FormData readFormData() 
	throws IOException,
	       HeaderFormatException,
	       DataFormatException,
	       UnsupportedFormatException {


	
	


	// Switch the request's 'Content-Type' field.
	HTTPHeaderLine hl_contentType = this.getRequestHeaders().get( HTTPHeaders.NAME_CONTENT_TYPE );  // This search is case-insensitive
	this.getLogger().log( Level.INFO,
			      getClass().getName() + ".readFormData()",
			      "Parsing POST data as MIME type '" + hl_contentType + "'." );

	
	// Is there a default value if not passed?
	if( hl_contentType == null 
	    || hl_contentType.getValue() == null
	    || hl_contentType.getValue().length() == 0 ) {

	    throw new HeaderFormatException( "Cannot parse post data. There is no 'Content-Type' present in the request header list." );
	}





	String hdr_contentType = hl_contentType.getValue(); 

	// This should be adapted to some general HeaderParams instance
	HeaderParams headerParams = new HeaderParams( HTTPHeaders.NAME_CONTENT_TYPE, hdr_contentType ); 
	String contentType = headerParams.getToken( 0, 0 );





	// My supported content types are:
	//  - multipart/form-data; boundary=----WebKitFormBoundaryeANQMKoBwsmwQrYZ
	//  - application/x-www-form-urlencoded  (default?)
	
	if( hl_contentType.getValue().equals("application/x-www-form-urlencoded") ) {

	    return readFormData_wwwFormURLEncoded( contentType, headerParams );

	} else if( hl_contentType.getValue().startsWith("multipart/form-data") ) {

	    return readFormData_multiPartFormData( contentType, headerParams );

	} else {

	    throw new UnsupportedFormatException( "The POST data's content type is '" + hl_contentType.getValue() + "'. The server does not recognize this format." );

	}

    }
    //--- END ------------------------ AbstractPostDataWrapper implementation -----------------------


    /**
     * This method handles the 'application/x-www-form-urlencoded' POST data.
     * That data format is very, very simple; it's just a basic query-string as handled
     * the usual request-URI's query part.
     *
     * Example:
     *  my_text=This+is+my+very+nice+test+%28B%29.&my_file=favicon.ico
     *
     * Note that this transfer mode does NOT allow to send binary files!
     **/
    private FormData readFormData_wwwFormURLEncoded( String contentType,
						     HeaderParams headerParams ) 
	throws IOException,
	       HeaderFormatException,
	       DataFormatException,
	       UnsupportedFormatException {


	// Retrieve the Content-Length header from the request to determine the number of bytes expected to read.
	Long contentLength = this.getRequestHeaders().getLongValue( HTTPHeaders.NAME_CONTENT_LENGTH );
	if( contentLength == null )
	    throw new HeaderFormatException( "Cannot read form data. No (valid) 'Content-Length' header present." );
	


	ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream(); 
	this.getLogger().log( Level.FINE,
			      getClass().getName() + ".readFormData_wwwFormURLEncoded()",
			      "Going to read " + contentLength + " bytes from POST data input stream ..." );
	long totalLength = CustomUtil.transfer( this.getInputStream(),
						byteArrayOutput,
						contentLength.longValue(),                // maxReadLength
						Math.min(1024, contentLength.intValue())  // bufferSize
						);
						



	String urlEncoded = new String( byteArrayOutput.toByteArray() );
	this.getLogger().log( Level.FINE,
			      getClass().getName() + ".readFormData_wwwFormURLEncoded()",
			      "POST data read, url-encoded: " + urlEncoded );


	String acceptEncoding = this.getRequestHeaders().getStringValue( HTTPHeaders.NAME_ACCEPT_CHARSET );
	if( acceptEncoding == null ) {

	    // Default charset is ISO-8859-1.
	    // This is ugly. I want to use UTF-8 :((
	    acceptEncoding = "ISO-8859-1";  
	    this.getLogger().log( Level.INFO,
				  getClass().getName() + ".readFormData_wwwFormURLEncoded()",
				  "The header" + HTTPHeaders.NAME_ACCEPT_CHARSET + " is not present. Using default value '" + acceptEncoding + "'." );

	}



	HeaderParams acceptParams = new HeaderParams( HTTPHeaders.NAME_ACCEPT_CHARSET, acceptEncoding ); 
	String charset = acceptParams.getToken( 0, 0 );
	try {

	    this.getLogger().log( Level.INFO,
				  getClass().getName() + ".readFormData_wwwFormURLEncoded()",
				  "Processing POST data using charset '" + charset + "' ..." );
	    // Decode and parse the form data  
	    Query query = new Query( urlEncoded, 
				     charset,    // The url's charset
				     false       // no case sensitive mapping
				     );


	    // Build form data directly from query
	    FormData formData = new QueryFormDataDelegation( query );


	    this.getLogger().log( Level.FINE,
				  getClass().getName() + ".readFormData_wwwFormURLEncoded()",
				  "POST form data retrieved: " + formData );

	    return formData;
	
	} catch( UnsupportedEncodingException e ) {

	    this.getLogger().log( Level.INFO,
				  getClass().getName() + ".readFormData_wwwFormURLEncoded()",
				  "Going to read " + contentLength + " bytes from POST data input stream ..." );
	    throw new UnsupportedFormatException( "Unsupported encoding for POST data '" + charset + "' (" + e.getMessage() + ")." );

	}

    }


    private FormData readFormData_multiPartFormData( String contentType,
						     HeaderParams headerParams ) 
	throws IOException,
	       HeaderFormatException,
	       DataFormatException,
	       UnsupportedFormatException {


	// The content type header should look like something like this:
	// Content-Type: multipart/form-data; boundary=----WebKitFormBoundaryeANQMKoBwsmwQrYZ


	try {

	    String boundary = headerParams.getTokenByPrefix( 0,           // on first level
							     "boundary=",  // the token name - so to say
							     false        // not case sensitive?
							     );  
	    KeyValueStringPair kv_boundary = KeyValueStringPair.split( boundary );

	    if( boundary == null 
		|| kv_boundary == null
		|| kv_boundary.getValue() == null
		|| kv_boundary.getValue().length() == 0 ) {

		 this.getLogger().log( Level.INFO,
				   getClass().getName() + ".readFormData_multiPartFormData()",
				   "Cannot split multi part form data. Required 'boundary' not present or empty in header field 'Content-Type'." );
		 throw new HeaderFormatException( "Cannot split multi part form data. Required 'boundary' not present or empty in header field 'Content-Type'." );

	    }


	    // Explode boundary (later: use a common parser class here for parsing header lines containing
	    // tokens with '=').
	    String boundary_value          = kv_boundary.getValue();

	    
	    this.getLogger().log( Level.INFO,
				  getClass().getName() + ".readFormData_multiPartFormData()",
				  "Processing POST data using boundary '" + boundary_value + "' ..." );

	    // The format of the multipart-body is:
	    // ------------------------------------
	    //  boundary        := 0*69<bchars> bcharsnospace
	    //  bchars          := bcharsnospace / " "
	    //  bcharsnospace   := DIGIT / ALPHA / "'" / "(" / ")" /
	    //                     "+" / "_" / "," / "-" / "." /
	    //                     "/" / ":" / "=" / "?"
	    //
	    // --- with --------------------------
	    //
	    //  multipart-body  := [preamble CRLF]
            //                     dash-boundary transport-padding CRLF
            //                     body-part *encapsulation
            //                     close-delimiter transport-padding
            //                     [CRLF epilogue]
	    //  dash-boundary   := "--" boundary
	    //  encapsulation   := delimiter transport-padding
	    //                     CRLF body-part
	    //  delimiter       := CRLF dash-boundary
	    //  close-delimiter := delimiter "--"
	    //
	    // --- Example: ----------------------
	    //  --boundary
	    //  1. body-part
	    //  --boundary
	    //  2. body-part
	    //  --boundary
	    //  3. body-part
	    //  --boundary--
	    //
	    // (See http://stackoverflow.com/questions/4656287/what-rules-apply-to-mime-boundary)

	    // So to detect if the last boundary is reached we need to check if the next
	    // body-party begins with 
	    // '--' CRLF
	    //
	    // This can only be done using a pushback input stream.
	    //
	    
	    MultipartMIMETokenizer mmt = new MultipartMIMETokenizer( this.getInputStream(),   
								     boundary_value   // Use the raw boundary from the headers here!
								     );

	    InputStream token;
	    int  tokenIndex  = 0;
	    long tokenLength;
	    long totalLength = 0;
	    byte[] endBuffer = new byte[4];
	    while( (token = mmt.getNextToken()) != null ) {

		this.getLogger().log( Level.INFO,
				      getClass().getName() + ".readFormData_multiPartFormData()",
				      "Processing multiform part " + tokenIndex + " by reading from token/input: " + token.toString() + ", globalInputStream=" + this.getInputStream() );

		int b;
		tokenLength = 0;
		while( (b = token.read()) != - 1 )  {

		    System.out.print( (char)b );
		    tokenLength++;
		    totalLength++;
		}


		this.getLogger().log( Level.INFO,
				      getClass().getName() + ".readFormData_multiPartFormData()",
				      "Processed multiform part " + tokenIndex + ": " + tokenLength + " bytes." );
		
		tokenIndex++;
	    }
	    

	    /*java.io.FileOutputStream fout = new java.io.FileOutputStream( "document_root_alpha/multipartFormData_A.dat" );
	    byte[] buf = new byte[256];
	    int len;
	    while( (len = this.getInputStream().read(buf)) > 0 )
		fout.write(buf,0,len);
	    fout.close();
	    */
	    
	    return null; // ??? 

	} catch( IOException e ) {

	     this.getLogger().log( Level.INFO,
				   getClass().getName() + ".readFormData_multiPartFormData()",
				   "[IOException] Failed to read/split multi part form data: " + e.getMessage() );
	     throw e;

	}

    }

}