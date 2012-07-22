package ikrs.util;

/**
 * This is a very small interface for all objects that are associated with an UUID.
 * The final(!) UUID is expected to be part of the object.
 *
 * @author Henning Diesenberg
 * @date 2012-05-23
 * @version 1.0.0
 **/

import java.util.UUID;

public interface ObjectWithUUID {

    /**
     * Get the unique and final UUID for this object.
     *
     * The returned UUID must never change!
     * 
     * @return The UUID of this object.
     **/
    public UUID getUUID();

}