package ikrs.yuccasrv.util;

/**
 * This is a custom Logger subclass that generate more verbose log messages than
 * the SimpleFormatter does.
 *
 *
 * @author Henning Diesenberg
 * @date 2012-05-09
 * @version 1.0.0
 **/

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;


public class YuccaLogFormatter
    extends SimpleFormatter {


    /*public String formatMessage( LogRecord record ) {
	
      }*/
    
    public String format( LogRecord record ) {
	String tmp = 
	    "" + record.getMillis() + 
	    " | " + record.getLevel() + 
	    " | " + record.getSourceClassName() + 
	    " | " + record.getSourceMethodName() + 
	    " | " + record.getMessage();

	if( record.getThrown() != null ) 
	    tmp += " | " + record.getThrown().getMessage();

	return tmp;
    }

}