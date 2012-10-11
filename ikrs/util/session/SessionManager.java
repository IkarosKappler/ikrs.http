package ikrs.util.session;

/**
 * This is a basic session handler interface.
 *
 * The actual handling policy is up to the individual implementation.
 *
 *
 * @author Ikaros Kappler
 * @date 2012-09-06
 * @version 1.0.0
 **/

import java.util.UUID;



public interface SessionManager<K,V,U> {
    
    /**
     * This method can be used to retrieve the manager's internal session factory.
     *
     * @return The manager's internal session factory.
     **/
    public SessionFactory<K,V,U> getSessionFactory();
    
    /**
     * Retrieve the session with the given SID.
     *
     * If the session cannot be found (does not exist or timed out) the method returns null.
     *
     * @param sessionID The desired session's unique ID.
     * @return The session with the given ID or null if no such session can be found.
     **/
    public Session<K,V,U> get( UUID sessionID );

    
    
    /**
     * Thie methos destroys the session with the specified SID. 
     * That means that all session data will be removed and the session itself becomes invalid. It
     * will not be accessible or retrievable any more using on of this interface's methods.
     *
     * @param sessionID The unique ID of the session you want to destroy.
     * @return True if the session was found (and so destroyed) or false otherwise.
     **/
    public boolean destroy( UUID sessionID );



    /**
     * This method tries to create a new session for the given user (ID). If there is already
     * a session for the given user no new session will be created but the existing one returned.
     *
     * @param userID The user (ID) to create the new session for.
     **/
    public Session<K,V,U> bind( U userID );


}