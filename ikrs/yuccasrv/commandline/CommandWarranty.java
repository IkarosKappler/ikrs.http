package ikrs.yuccasrv.commandline;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import ikrs.typesystem.BasicType;
import ikrs.typesystem.BasicTypeException;
import ikrs.util.AbstractCommand;
import ikrs.util.CaseInsensitiveComparator;
import ikrs.util.Command;
import ikrs.util.CommandStringIncompleteException;
import ikrs.yuccasrv.Constants;

/**
 * This is the default WARRANTY command implementation.
 *
 * It just prints the contents of the warranty.txt file into stdout.
 *
 *
 * @author Ikaros Kappler
 * @date 2012-12-17
 * @version 1.0.0
 **/


public class CommandWarranty
    extends YuccaCommand {

    public CommandWarranty() {

	super( "WARRANTY" );
    }

   
    //---BEGIN------------------------ AbstractCommand/YuccaCommand implementation ---------------------
    /**
     * This method handles the STATUS command's params.
     *
     *
     * Note that this method does _not_ throw any exceptions!
     * Its up to a stored internal command handler to handle exceptions.
     *
     * @return a return code that indicates the execution result. It depends
     *         on the context what the exact meaning of the return code is,
     *         but usually the value 0 (zero) implies success.
     **/
    public int execute() {
	
	// The LICENSE listen command is described as follows:
	// LICENSE
	
	this.getFactory().getServer().performPrintWarranty(); 
	return 0; // implies SUCCESS 	 
    }
    //---END-------------------------- AbstractCommand implementation ---------------------

}