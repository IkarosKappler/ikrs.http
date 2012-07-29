package ikrs.http.resource;

/**
 * @author  Ikaros Kappler
 * @date    2012-07-20
 * @version 1.0.0
 **/

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock; 


import ikrs.http.ReadOnlyException;
import ikrs.http.Resource;
import ikrs.http.ResourceMetaData;


public abstract class AbstractResource
    implements Resource {

    private ResourceMetaData metaData;


    /**
     * The read-write-lock.
     **/
    private ReentrantReadWriteLock rwLock;

    
    /**
     * Create a new AbstractResource.
     **/
    public AbstractResource( boolean useFairLocks ) {
	super();

	this.rwLock = new ReentrantReadWriteLock( useFairLocks );
	this.metaData = new ResourceMetaData();
    }


    //---BEGIN------------------- Resource implementation ----------------------------
    /**
     * Get the meta data for this resource.
     **/
    public ResourceMetaData getMetaData() {
	return this.metaData;
    }

    /**
     * This method returns the read lock for this resource.
     **/
    public ReentrantReadWriteLock.ReadLock getReadLock() {
	return this.rwLock.readLock();
    }

    /**
     * This method returns the write lock for this resource.
     **/
    public ReentrantReadWriteLock.WriteLock getWriteLock() {
	return this.rwLock.writeLock();
    }

    /**
     * This method opens the underlying resource. Don't forget to close.
     *
     * @param readOnly if set to true, the resource will be opned in read-only mode.
     *
     * @throws ReadOnlyException If the underlying resource is read-only in general.
     * @throws IOException If any other IO error occurs.
     * @see isReadOnly()
     **/
    public abstract void open( boolean readOnly )
	throws IOException;

    /**
     * This method determines if this resource was alerady opened or not.
     *
     * @throws IOException If any IO error occurs.
     **/
    public abstract boolean isOpen()
	throws IOException;


    /**
     * This method returns true if the underlying resource is read-only (in general).
     *
     * @throws IOException If any IO error occurs.
     **/
    public abstract boolean isReadOnly()
	throws IOException;

    /**
     * This method returns the *actual* length of the underlying resource. This length will
     * be used in the HTTP header fields to specify the transaction length.
     *
     * During read-process (you used the locks, didn't you?) the length MUST NOT change.
     *
     * @return the length of the resource's data in bytes.
     * @throws IOException If any IO error occurs.
     **/
    public abstract long getLength()
	throws IOException;


    /**
     * Get the output stream to this resource.
     *
     * @throws ReadOnlyException If this resource was opened with the read-only flag set.
     * @throws IOException If any other IO error occurs.
     **/
    public abstract OutputStream getOutputStream()
	throws ReadOnlyException,
	       IOException;

    /**
     * Get the input stream from this resource.
     *
     * @throws IOException If any IO error occurs.
     **/
    public abstract InputStream getInputStream()
	throws IOException;


    /**
     * Closes this resource.
     *
     * @return false if the resource was already closed, false otherwise.
     **/
    public abstract boolean close()
	throws IOException;
    //---END--------------------- Resource implementation ----------------------------


}
