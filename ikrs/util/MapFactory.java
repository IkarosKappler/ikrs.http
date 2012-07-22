package ikrs.util;

/**
 * This is a simple map factory interface.
 *
 * @author Henning Diesenberg
 * @date 2012-04-24
 * @version 1.0.0
 **/ 

import java.util.Map;

public interface MapFactory<K,V> {

    /**
     * Creates a new empty map.
     **/
    public Map<K,V> createMap();
    

}
