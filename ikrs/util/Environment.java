package ikrs.util;

/**
 * The Environment interface prepares a data structure for nested data
 * environments. The full environment will be a tree like structure
 * with a base environment in the root.
 *
 * @author Henning Diesenberg
 * @date 2012-04-24
 * @version 1.0.0
 **/ 

import java.util.Map;
import java.util.List;

public interface Environment<K,V>
    extends Map<K,V> {

    /**
     * This method indicates if the environment allows multiple names
     * for child environments.
     *
     * If this method returns false the method createChild MUST NOT create
     * a new child if there already exists a child with that name.
     **/
    public boolean allowsMultipleChildNames();

    /**
     * Get the environments parent. If there is no parent the method will
     * return null.
     * 
     * Only the root environment has not parent.     * 
     **/
    public Environment<K,V> getParent();
  
    /**
     * Get the child environment with the given name; there might be different
     * children with the same name!
     * The method will return the first match then.
     * 
     * If no such child exists the method returns null.
     * 
     * @param name The child's name (if the child search is case sensitive
     *             depends on the actual implementation).
     * @see DefaultEnvironment
     **/
    public Environment<K,V> getChild( String name );

    /**
     * This method simply returns a list containing _all_ children.
     **/
    public List<Environment<K,V>> getAllChildren();

    /**
     * Get *all* children with the given name.
     *
     * @param name The child's name (if the child search is case sensitive
     *             depends on the actual implementation).
     * @see DefaultEnvironment
     **/
    public List<Environment<K,V>> getChildren( String name );

    /**
     * This method creates a new child environment and returns it.
     *
     * Note A: if allowsMultipleChildNames() returns true, this method
     *         always creates a new child.
     * Note B: if allowsMultipleChildNames() returns false, and if a child 
     *         with the given name already exists there will be
     *         no modifications to the environment. In this case the method
     *         return the old child.
     *
     * @param name The child's name.
     **/
    public Environment<K,V> createChild( String name );

    /**
     * This method removes the child with the given name and returns
     * its old value.
     *
     * If no such child can be found the method returns null.     *
     *
     * @param name The child's name.
     **/
    public Environment<K,V> removeChild( String name );

    /**
     * This method returns the number of all children.
     * 
     **/
    public int getChildCount();

}