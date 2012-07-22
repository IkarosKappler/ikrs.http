package ikrs.typesystem;

/**
 * This is a simple type interface for representing the basic Java types
 * byte, short, int, long, float, double, boolean, char, string - and for 
 * usage purposes: UUID.
 *
 *
 * @author Henning Diesenberg
 * @date 2012-04-22
 * @version 1.0.0
 **/


import java.util.UUID;


public interface BasicType {

    public static final int TYPE_BOOLEAN  = 0;
    public static final int TYPE_BYTE     = 1;
    public static final int TYPE_SHORT    = 2;
    public static final int TYPE_INT      = 3;
    public static final int TYPE_LONG     = 4;
    public static final int TYPE_FLOAT    = 5;
    public static final int TYPE_DOUBLE   = 6;
    public static final int TYPE_CHAR     = 7;
    public static final int TYPE_STRING   = 8;
    public static final int TYPE_UUID     = 9;
       

    public int getType();

    public boolean getBoolean()
	throws BasicTypeException;

    public boolean getBoolean( boolean defaultValue );
	
    public byte getByte()
	throws BasicTypeException;

    public byte getByte( byte defaultValue );

    public short getShort()
	throws BasicTypeException;

    public short getShort( short defaultValue );
    
    public int getInt()
	throws BasicTypeException;

    public int getInt( int defaultValue );

    public long getLong()
	throws BasicTypeException;

    public long getLong( long defaultValue );
    
    public float getFloat()
	throws BasicTypeException;

    public float getFloat( float defaultValue );

    public double getDouble()
	throws BasicTypeException;

    public double getDouble( double defaultValue );

    public char getChar()
	throws BasicTypeException;

    public char getChar( char defaultValue );

    public String getString() 
	throws BasicTypeException;

    public String getString( String defaultValue );

    public UUID getUUID()
	throws BasicTypeException;

    public UUID getUUID( UUID defaultValue );


    public boolean equals( BasicType t );

    public boolean equals( boolean b );

    public boolean equals( byte b );

    public boolean equals( short s );

    public boolean equals( int i );

    public boolean equals( long l );

    public boolean equals( float f );
    
    public boolean equals( double d );

    public boolean equals( char c );

    public boolean equals( String s );

    public boolean equals( UUID id );

}