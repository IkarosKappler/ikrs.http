package ikrs.httpd;

/**
 * @author Ikaros Kappler
 * @date 2013-01-09
 * @modified 2013-04-17 Ikaros Kappler (shared handler instance added).
 * @version 1.0.0
 **/

import ikrs.typesystem.BasicType;
import ikrs.util.DefaultCommand;


public class ModuleCommand
    extends DefaultCommand {

    private int offset;

    /**
     * Create a new LocalCommand (for the yucca/ikrs.httpd command line).
     *
     * @param name The command's name.
     * @param params The command's params.
     **/
    public ModuleCommand( String name,
			  BasicType[] params,
			  int offset ) {

	super( name, params );

	this.offset = offset;

    }

    public int getOffset() {
	return offset;
    }

    public int execute() {
	
	if( HTTPHandler.sharedInstance == null ) {
	    
	    System.out.println( "Cannot run this command because there is no shared HTTPHandler instance." );
	    return -1;
	}

	

	if( this.getName().equalsIgnoreCase("STATUS") ) {

	    //System.out.println( "No status information available yet (not yet implemented)." );
	    HTTPHandler.sharedInstance.performStatus();
	    
	    return 0;

	} else {

	    System.out.println( "Unknown argument '" + this.getName() +"'. Use 'STATUS' instead." );

	    return -1;

	}
    }



}