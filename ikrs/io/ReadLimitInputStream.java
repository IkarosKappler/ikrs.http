package ikrs.io;

/**
 * This input stream is an extension of the BytePositionInputStream.
 * 
 * It stores a read limit (long) which, when reached, causes EOI.
 *
 *
 * @author Ikaros Kappler
 * @date 2012-09-29
 * @version 1.0.0
 **/

import java.io.InputStream;
import java.io.IOException;

public class ReadLimitInputStream 
    extends BytePositionInputStream {

    /**
     * The actual underlying InputStream to read from.
     **/
    private BytePositionInputStream in;

    /**
     * The actual (absolute) read limit.
     **/
    private long readLimit;


    public ReadLimitInputStream( InputStream in,
				 long readLimit ) 
	throws NullPointerException,
	       IllegalArgumentException {

	super( in );

	if( in == null )
	    throw new NullPointerException( "Cannot create ReadLimitInputStreams from null-streams." );
	if( readLimit < 0 )
	    throw new IllegalArgumentException( "Cannot create ReadLimitInputStreams with readLimit=" + readLimit );

	this.in = new BytePositionInputStream( in );
	this.readLimit = readLimit;
    }


    protected long getMaxAvailable() {
	return this.readLimit - this.in.getAbsoluteBytePosition();	
    }


    //--- BEGIN ------------------------- Override Inputstream ------------------------
    public synchronized int available() 
	throws IOException {

	if( this.in.available() > this.readLimit )
	    return (int)(this.available() - this.readLimit);
	else
	    return this.in.available();

    }

    public synchronized void close() 
	throws IOException {
	this.in.close();
    }

    /**
     * The mark-method is a bit tricky! This implementation does NOT support marks and
     * throws an IOException if called.
     **/
    public void mark( int readlimit ) {
	
	// IF you want to implement it, don't forget to change the markSupported() method and the reset() method!
	//throw new IOException( "The class '" + getClass().getName() +"' does not support the mark-method yet. Feel free to implement it." );
	
    }

    public boolean markSupported() {
	return false;	
    }
    
    public synchronized int read() 
	throws IOException {

	if( this.in.getAbsoluteBytePosition() >= this.readLimit )
	    return -1; // implies EOI

	return this.in.read();
    }

    public synchronized int read(byte[] b) 
	throws IOException {

	return this.read( b, 0, b.length );
    }

    public synchronized int read(byte[] b, int off, int len)
	throws IOException {

 
	return this.in.read( b, 
			     off, 
			     Math.min( (int)(this.readLimit - this.in.getAbsoluteBytePosition()),
				       len )
			     );


    }

    public void reset()
	throws IOException {

	throw new IOException( "The class '" + getClass().getName() +"' does not support the mark- nor the reset-method yet. Feel free to implement them." );

    }

    public synchronized long skip( long n ) 
	throws IOException {


	return this.in.skip( Math.min( this.readLimit - this.in.getAbsoluteBytePosition(),
				       n )
			     );
	
	
    }
    //--- BEGIN ------------------------- Override Inputstream ------------------------


    public String toString() {
	return "ReadLimitInputStream=[readLimit="+ this.readLimit + ", maxAvailable=" + this.getMaxAvailable() + ", coreStream="+ this.in +" ]";
    }

}