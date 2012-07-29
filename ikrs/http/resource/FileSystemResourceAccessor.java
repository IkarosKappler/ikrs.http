package ikrs.http.resource;

/**
 * @autor Ikaros Kappler
 * @date 2012-07-23
 * @version 1.0.0
 **/

import java.io.File;
import java.net.URI;
import java.util.MissingResourceException;
import java.util.logging.Level;

import ikrs.http.Resource;
import ikrs.http.ResourceAccessor;
import ikrs.http.HTTPHandler;

import ikrs.util.MIMEType;


public class FileSystemResourceAccessor
    extends AbstractResourceAccessor 
    implements ResourceAccessor {

    /**
     * ... 
     **/
    public FileSystemResourceAccessor( HTTPHandler handler ) {
	super( handler );
	
	
    }


    /**
     * This method locates the desired resource addressed by the given URI.
     *
     * @throws ResouceMissingException If the specified resource cannot be found.
     **/
    public Resource locate( URI uri )
	throws MissingResourceException {

	
	this.getHTTPHandler().getLogger().log( Level.INFO,
					       getClass().getName() + ".locate(...)",
					       "URI="+uri+", host="+uri.getHost()+", path="+uri.getPath()+", query="+uri.getQuery() );
	

	String path = uri.getPath();
	
	File file = new File( new File( "document_root_alpha" ), path );
	
	if( !file.exists() ) 
	    throw new MissingResourceException( "File '"+path+"' not found.",
						file.getClass().getName(),
						path 
						);


	Resource resource = new FileResource( file, 
					      true   // use fair locks?
					      );

	// Determine MIME type
	int index = file.getName().lastIndexOf(".");
	if( index != -1 && index+1 < file.getName().length() ) {
	    
	    String extension = file.getName().substring( index+1 ).trim().toLowerCase();
	    MIMEType mimeType = MIMEType.getByFileExtension( extension );
	    if( mimeType != null ) {

		resource.getMetaData().setMIMEType( mimeType );
		this.getHTTPHandler().getLogger().log( Level.FINEST,
						       getClass().getName() + ".locate(...)",
						       "Determined MIME type of file '"+file.getPath()+"': " + mimeType.getContentType() );

	    } else {

		resource.getMetaData().setMIMEType( new MIMEType("application/octet-stream") );
		this.getHTTPHandler().getLogger().log( Level.WARNING,
						       getClass().getName() + ".locate(...)",
						       "Cannot determine the MIME type of file '"+file.getPath()+"' (using default MIME type application/octet-stream)." );
		
	    }
	    

	}

	return resource;
    }


}

