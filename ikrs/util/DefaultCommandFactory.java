package ikrs.util;

/**
 * @author Henning Diesenberg
 * @date 2012-05-07
 * @version 1.0.0
 **/

import java.text.ParseException;

import ikrs.typesystem.BasicStringType;
import ikrs.typesystem.BasicType;


public class DefaultCommandFactory
    extends AbstractCommandFactory<DefaultCommand>
    implements CommandFactory<DefaultCommand> {


    //---BEGIN----------------------- AbstractCommandFactory implementation -----------------------
    /**
     * Make a new Command with the given name and params.
     *
     * @param name The command's name.
     * @param params The command's params - in BasicType representation.
     * 
     * @return The new command.
     **/
    public DefaultCommand make( String name,
				BasicType[] params )
	throws UnknownCommandException,
	CommandStringIncompleteException {

	return new DefaultCommand( name, params );
    }
    //---END------------------------- AbstractCommandFactory implementation -----------------------

}