package ikrs.httpd;

/**
 * @author Ikaros Kappler
 * @date 2013-01-09
 * @version 1.0.0
 **/

import ikrs.typesystem.BasicType;
import ikrs.util.AbstractCommand;
import ikrs.util.AbstractCommandFactory;
import ikrs.util.Command;
import ikrs.util.UnknownCommandException;
import ikrs.util.CommandStringIncompleteException;


public class LocalCommandFactory
    extends AbstractCommandFactory<Command> {



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
	throw new UnknownCommandException( "Unknown yucca/httpd command." );
    }
    //--- END ----------------------- AbstractCommandFactory implementation ------------------



}