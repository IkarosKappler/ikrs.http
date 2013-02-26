package ikrs.httpd;

/**
 * @author Ikaros Kappler
 * @date 2013-01-09
 * @version 1.0.0
 **/

import ikrs.typesystem.BasicType;
import ikrs.util.DefaultCommand;


public class LocalCommand
    extends DefaultCommand {

    private int offset;

    /**
     * Create a new LocalCommand (for the yucca/ikrs.httpd command line).
     *
     * @param name The command's name.
     * @param params The command's params.
     **/
    public LocalCommand( String name,
			 BasicType[] params,
			 int offset ) {

	super( name, params );

	this.offset = offset;

    }

    public int getOffset() {
	return offset;
    }

    public int execute() {
	
	if( this.getName() == null )
	    return -1;



	if( this.getName().equalsIgnoreCase("STATUS") ) {

	    System.out.println( "No status information available yet (not yet implemented)." );
	    return 0;

	} else {

	    return -1;

	}
    }



}