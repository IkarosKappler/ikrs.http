package ikrs.yuccasrv.commandline;

/**
 * @author Henning Diesenberg
 * @date 2012-05-07
 * @version 1.0.0
 **/

import java.text.ParseException;


import ikrs.typesystem.BasicType;
import ikrs.util.AbstractCommandFactory;
import ikrs.util.Command;
import ikrs.util.CommandFactory;
import ikrs.util.CommandStringIncompleteException;
import ikrs.util.UnknownCommandException;
import ikrs.yuccasrv.Yucca;

public class YuccaCommandFactory
    extends AbstractCommandFactory<YuccaCommand>
    implements CommandFactory<YuccaCommand> {

    /* The actual yucca server */
    private Yucca
	server;

    /**
     * The constructor.
     *
     * The server instanced will be delegated to all commands that are made with this factory.
     *
     * @param server If available pass the server instance; if not available, just pass null.
     *
     **/
    public YuccaCommandFactory( Yucca server ) {
	super();

	this.server = server;
    }

    /**
     * Get the factories server instance.
     *
     * Warning: it is not guaranteed that the server is set; in this case the method returns null.
     *
     * @return The YuccaServer instance or null if not set.
     **/
    protected Yucca getServer() {
	return this.server;
    }

    //---BEGIN----------------------- AbstractCommandFactory implementation -----------------------
    /**
     * Make a new Command with the given name and params.
     *
     * @param name The command's name.
     * @param params The command's params - in BasicType representation.
     * 
     * @return The new command.
     **/
    public YuccaCommand make( String name,
			      BasicType[] params )
	throws UnknownCommandException,
	CommandStringIncompleteException {

	YuccaCommand cmd;
	if( name.equalsIgnoreCase("LISTEN") || name.equalsIgnoreCase("BIND") ) 
	    cmd = new CommandListen();
	else if( name.equalsIgnoreCase("HELP") || name.equalsIgnoreCase("?") ) 
	    cmd = new CommandHelp();
	else if( name.equalsIgnoreCase("QUIT") || name.equalsIgnoreCase("EXIT") ) 
	    cmd = new CommandQuit();
	else if( name.equalsIgnoreCase("UNLISTEN") || name.equalsIgnoreCase("RELEASE") || name.equalsIgnoreCase("UNBIND") ) 
	    cmd = new CommandUnlisten();
	else if( name.equalsIgnoreCase("STATUS") ) 
	    cmd = new CommandStatus();
	else 
	    throw new UnknownCommandException( "Unknown command: '"+name+"'.", name );


	cmd.setName( name );
	cmd.setParams( params );
	cmd.setFactory( this );
	
	/*
	return new YuccaCommand( this,    // This will make the server instance accessible to the created command
				 name, 
				 params );
	*/
	return cmd;
    }
    //---END------------------------- AbstractCommandFactory implementation -----------------------

    public static String[] getImplementedCommands() {
	return new String[] {
	      "HELP", "LISTEN", "STATUS", "UNLISTEN", "QUIT"
	};
    }
    
}