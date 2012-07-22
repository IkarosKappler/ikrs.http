package ikrs.http;

/**
 * @author  Ikaros Kappler
 * @date    2012-07-20
 * @version 1.0.0
 **/

import java.net.URI;
import java.util.MissingResourceException;

public interface ResourceAccessor {


    /**
     * This method locates the desired resource addressed by the given URI.
     *
     * @throws ResouceMissingException If the specified resource cannot be found.
     **/
    public Resource locate( URI uri )
	throws MissingResourceException;


}