package ikrs.http;

/**
 * @author  Ikaros Kappler
 * @date    2012-07-20
 * @version 1.0.0
 **/


import java.io.IOException;


public class ReadOnlyException
    extends IOException {


    public ReadOnlyException( String msg ) {
	super( msg );
    }

}