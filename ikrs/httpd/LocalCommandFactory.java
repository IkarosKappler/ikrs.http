package ikrs.httpd;

/**
 * @author Ikaros Kappler
 * @date 2013-01-09
 * @version 1.0.0
 **/

import ikrs.typesystem.BasicType;
import ikrs.util.AbstractCommand;
import ikrs.util.DefaultCommandFactory;
import ikrs.util.Command;
import ikrs.util.UnknownCommandException;
import ikrs.util.CommandStringIncompleteException;


public class LocalCommandFactory
    extends DefaultCommandFactory {

    
    public LocalCommandFactory() {
	
	super();

	this.addSupportedCommand( new LocalCommand( "HTTPD",
						    null,  // params
						    0      // offset
						    )
				  );
						   

    }


    //--- BEGIN --------------------- AbstractCommandFactory implementation ------------------
    /**
     * Make a new Command with the given name and params.
     *
     * @param name The command's name.
     * @param params The command's params - in BasicType representation.
     * 
     * @return The new command.
     **/
    public LocalCommand make( String name,
			      BasicType[] params )
	throws UnknownCommandException,
	       CommandStringIncompleteException {

	
	//return null;
	if( name == null )
	    throw new NullPointerException( "Command must not be null." );
	
	if( name.equalsIgnoreCase("HTTP") || name.equalsIgnoreCase("HTTPD") ) {

	    if( params.length == 0 )
		throw new CommandStringIncompleteException( "Command " + name + " requires at least one argument: STATUS" );

	    return new LocalCommand( params[0].getString(),
				     params,
				     1 );

	} else {
	
	    throw new UnknownCommandException( "Unknown yucca/httpd command." );

	}
    }
    //--- END ----------------------- AbstractCommandFactory implementation ------------------



}