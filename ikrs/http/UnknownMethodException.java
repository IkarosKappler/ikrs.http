package ikrs.http;

/**
 * @author Ikaros Kappler
 * @date 2012-07-19
 * @version 1.0.0
 **/

public class UnknownMethodException 
    extends HTTPException  {

    /**
     * Creates a new UnknownMethodException witht the given error message.
     **/
    public UnknownMethodException( String msg ) {
	super( msg );
    }


}