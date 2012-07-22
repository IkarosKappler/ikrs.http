package ikrs.yuccasrv.commandline;

/**
 * This is the default HELP command implementation.
 *
 *
 * @author Henning Diesenberg
 * @date 2012-05-09
 * @version 1.0.0
 **/


import ikrs.typesystem.BasicType;
import ikrs.util.AbstractCommand;
import ikrs.util.Command;
import ikrs.util.CommandStringIncompleteException;

public class CommandHelp
    extends YuccaCommand {

    public CommandHelp() {

	super();
    }

   
    //---BEGIN------------------------ AbstractCommand/YuccaCommand implementation ---------------------
    /**
     * This is the final execution method - the default implementation has only one
     * simple effect: if the command name equals "QUIT" or "EXIT" (not case sensitive)
     * the method calls System.exit(0).
     *
     * Otherwise it returns always 1.
     *
     * Your subclasses should override this method.
     *
     * Note that this method does _not_ throw any exceptions!
     * Its up to a stored internal command handler to handle exceptions.
     *
     * @return a return code that indicates the execution result. It depends
     *         on the context what the exact meaning of the return code is,
     *         but usually the value 0 (zero) implies success.
     **/
    public int execute() {
	
	// Build info
	String[] commands = YuccaCommandFactory.getImplementedCommands();
	StringBuffer b = new StringBuffer( "Available commands: " );
	for( int i = 0; i < commands.length; i++ ) {
	    
	    if( i > 0 )
		b.append( ", " );
	    b.append( commands[i] );

	}
	
	System.out.println( b.toString() );
	return 0;
	
    }
    //---END-------------------------- AbstractCommand implementation ---------------------

}