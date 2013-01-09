package ikrs.httpd;

/**
 * @author Ikaros Kappler
 * @date 2013-01-09
 * @version 1.0.0
 **/

import ikrs.typesystem.BasicType;
import ikrs.util.AbstractCommand;


public class LocalCommand
    extends AbstractCommand {

    public LocalCommand( String name,
			 BasicType[] params ) {

	super( name, params );

    }

    public int execute() {
	return -1;
    }



}