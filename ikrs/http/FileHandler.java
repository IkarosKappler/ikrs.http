package ikrs.http;

/**
 * This FileHandler interface is meant for HTTP resources that represent executable files (in any way)
 * inside the document root (such as CGI scripts, system commands, executables, ...).
 *
 * As configuration files such as .htaccess allow to define file handlers (AddHandler and SetHandler 
 * directives) there is the need to summarize those handlers together under one general interface.
 *
 *
 * @author Ikaros Kappler
 * @date 2012-09-29
 * @version 1.0.0
 **/

import java.io.File;
import java.io.IOException;


public interface FileHandler {

    /**
     * Is that a good idea?
     **/
    public Resource process( HTTPHeaders headers,
			     PostDataWrapper postData,
			     File file )
	throws IOException,
	       HeaderFormatException,
	       DataFormatException,
	       UnsupportedFormatException;;


}