/**
 * This class was inspired by 
 * http://kalanir.blogspot.com/2010/02/how-to-split-image-into-chunks-java.html
 *
 * Run this class calling
 *  java splitimg.SplitImg --help
 *
 * @author Henning Diesenberg
 * @date 2011-12-29
 * @version 1.0.0
 **/

package splitimg;

import javax.imageio.ImageIO;  
import java.awt.image.BufferedImage;
import java.io.*;  
import java.awt.*;  
  
public class SplitImg {  

    public static final int DEFAULT_COLUMN_COUNT = 4;
    public static final int DEFAULT_ROW_COUNT = 8;


    private File destinationDir;
    private File imageFile;
    private int cols;
    private int rows;
    private String outputType;
    private int scalingA;  // scaleFactor = (A:B)
    private int scalingB;
    private String outputNamePattern;

    /* An internal array the image splits will be stored to */
    private BufferedImage[] splits;


    public SplitImg( File image ) {
	this( image, 
	      SplitImg.DEFAULT_COLUMN_COUNT,
	      SplitImg.DEFAULT_ROW_COUNT );
    }

    public SplitImg( File image,
		     int cols,
		     int rows ) {
	super();

	if( cols <= 0 )
	    throw new IllegalArgumentException( "Column count must be a positive integer." );
	if( rows <= 0 )
	    throw new IllegalArgumentException( "Row count must be a positive integer." );
	
	this.imageFile = image;
	this.cols = cols;
	this.rows = rows;

	this.destinationDir = image.getParentFile();
	this.outputType = "jpg";
	
    }

    /**
     * Sets the destination dir.
     * The passed file must be a directory.
     **/
    public void setDestinationDir( File dir ) 
	throws IOException {

	if( !dir.isDirectory() )
	    throw new IOException( "The destinationDir file must be a directory." );

	this.destinationDir = dir;

    }

    /**
     * This method sets the output file type.
     *
     * Eeach string (except null) allowed here but it's
     * not guaranteed that writeSplits() can handle it;
     * an IOException will be thrown if the image/file type
     * is not supported by the system. 
     * Usual types are "jpg", "png" and "bmp".
     *
     * @param type the output image type.
     * @throws NullPointerException if type is null.
     **/
    public void setOutputType( String type ) 
	throws NullPointerException {

	if( type == null )
	    throw new NullPointerException( "Output file type must not be null" );

	this.outputType = type;

    }

    /**
     * This method sets the output filename pattern.
     *
     * If you want to use the input file's name base
     * just pass null.
     *
     * Otherwise (pattern != null) the given pattern
     * MUST contain "%x" and "%y" for the row and
     * the column index. 
     *
     * @param pattern The new pattern or null of the
     *                input name base shall be used.
     * @throws IllegalArgumentException if pattern != null
     *         and pattern does not contain "%x" or "%y".
     **/
    public void setOutputNamePattern( String pattern ) 
	throws IllegalArgumentException {


	if( pattern != null && pattern.indexOf("%x") == -1 )
	    throw new IllegalArgumentException( "The given pattern does not contain the \"%x\" placeholder." );
	if( pattern != null && pattern.indexOf("%y") == -1 )
	    throw new IllegalArgumentException( "The given pattern does not contain the \"%y\" placeholder." );


	this.outputNamePattern = pattern;

    }

    /**
     * This method sets the scaling ratio.
     * The passed string must have the format
     * "A/B" - where A and B must be non-negative
     * and non-zero integers.
     * 
     * It just splits the string into its components
     * A and B, parses them to integers ans calls
     * setRatio( int A, int B).
     *
     * @see setRatio( int, int )
     **/
    public void setRatio( String ratioStr ) 
	throws IllegalArgumentException {

	int p = ratioStr.indexOf("/");
	if( p <= 0 || p+1 >= ratioStr.length() )
	    throw new IllegalArgumentException( "This is not a properly formatted ratio string: "+ratioStr+"." );
	
	String strA = ratioStr.substring(0,p).trim();
	String strB = ratioStr.substring(p+1).trim();

	try {
	    setRatio( Integer.parseInt( strA ),
		      Integer.parseInt( strB )
		      );
	} catch( NumberFormatException e ) {
	    throw new IllegalArgumentException( "The ratio string '"+ratioStr+"' seems to contain non-integer values." );
	}

    }

    /**
     * This method sets the scaling ratio.
     *
     * The params A and B make up a rational number A/B which
     * specifies the scaling factor.
     *
     * @param A the numerator.
     * @param B the denominator.
     * @throws IllegalArgumentException if A <= 0 or B <= 0.
     **/
    public void setRatio( int A, int B ) 
	throws IllegalArgumentException {

	if( A <= 0 || B <= 0 )
	    throw new IllegalArgumentException( "Both ratio params must be positive integers." );

	this.scalingA = A;
	this.scalingB = B;
    }

    /**
     * This method splits the imaged into chunks and stores them into the
     * 'splits' array.
     **/
    public void performSplit()  
	throws IOException {  
  
        
        FileInputStream fin = new FileInputStream(imageFile);  
        BufferedImage bufferedImage = ImageIO.read(fin); // read the input image file 
	/* The input stream resource is no longer needed */
	fin.close();

	/* perform scaling? */
	Image image;
	if( scalingA != scalingB )  {
	   
	    float ratio = (scalingA+0.0f) / (scalingB+0.0f);
	    int desiredWidth = (int)(bufferedImage.getWidth() * ratio);
	    int desiredHeight = (int)(bufferedImage.getHeight() * ratio);
	    
	    /* Scale image */
	    image = bufferedImage.getScaledInstance( desiredWidth,
						     desiredHeight,
						     Image.SCALE_SMOOTH );
	    
	} else {
	    /* Continue with un-scaled image */
	    image = bufferedImage;

	}
  
        int chunks = this.rows * this.cols;  
  
        int chunkWidth = image.getWidth( null ) / this.cols; // determines the chunk width and height  
        int chunkHeight = image.getHeight( null ) / this.rows;  
        int count = 0;  
        this.splits = new BufferedImage[chunks]; //Image array to hold image chunks  
        for (int x = 0; x < this.rows; x++) {  
            for (int y = 0; y < this.cols; y++) {  
                //Initialize the image array with image chunks  
                splits[count] = new BufferedImage( chunkWidth, chunkHeight, bufferedImage.getType() );  
  
                // draws the image chunk  
                Graphics2D gr = splits[count].createGraphics();  
                gr.drawImage( image, 
			      0, 0,  // destination x1, y1
			      chunkWidth, chunkHeight, // destintation x2, y2
			      chunkWidth * y, // source x1
			      chunkHeight * x, // source x2
			      chunkWidth * y + chunkWidth, // source y1
			      chunkHeight * x + chunkHeight, // source y2
			      null  // no observer
			      );  
                gr.dispose();  

		count++;
            }  
        }  
        // image was split
  
    }

    /**
     * This method writes all chunks in the 'splits' array into files.
     **/
    public void writeSplits() 
	throws IOException {

	String baseName;

	if( this.outputNamePattern == null ) {
	    baseName = imageFile.getName();
	    if( baseName.lastIndexOf(".") != -1 )
		baseName = baseName.substring( 0, baseName.lastIndexOf(".") );

	    baseName += "_(%x,%y)."+this.outputType;
	} else {

	    baseName = this.outputNamePattern;

	}

	//System.out.println( "baseName="+this.outputNamePattern );

        // write output files
	int x = 0;
	int y = 0;
        for (int i = 0; i < splits.length; i++) {  
	    String outputFilename = baseName;
	    outputFilename = outputFilename.replaceAll( "%x", ""+x );
	    outputFilename = outputFilename.replaceAll( "%y", ""+y );
	    System.out.println( " outputFilename["+i+"]="+outputFilename );
	    
	    
            ImageIO.write( splits[i], 
			   this.outputType, 
			   new File( this.destinationDir, outputFilename )
			   );  
	    

	    x++;
	    if( x >= this.cols ) {
		x = 0;
		y++;
	    }
        }  
        
    }  


    public static void printHelp() {

	System.out.println( "Arguments: ");
	System.out.println( "\t[ <optional_arguments> ] <image_name>" );

	System.out.println( "Optional arguments:" );
	System.out.println( "\t{ -h | --help }" );
	System.out.println( "\t{ -r | --rows } <row_count>" );
	System.out.println( "\t{ -c | --cols } <column_count>" );
	System.out.println( "\t{ -s | --scaling } A/B (a ratio A:B; A and B must be integers, default is 1/1)" );
	System.out.println( "\t{ -d | --directory } <destination_directory>" );
	System.out.println( "\t{ -t | --type } <outpt_type> (jpg, png, bmp, ...)>" );
	System.out.println( "\t{ -p | --pattern } <outpt_name_pattern> (must contain %x and %y)>" );
    }


    public static void main( String[] argv ) {

	/* command line params available? */
	if( argv.length == 0 ) {
	    System.out.println( "Use -h or --help for help." );
	    System.exit(1);
	}

	/* parse command line input */
	int cols = SplitImg.DEFAULT_COLUMN_COUNT;
	int rows = SplitImg.DEFAULT_ROW_COUNT;
	String destDir = "map_splits";
	String imgFile = null;
	String type = "jpg";
	String ratio = "1/1";
	String pattern = null; // This may be left null

	int i = 0;
	while( i < argv.length ) {

	    String key = argv[i].toLowerCase();
	    if( key.equals("-h") || key.equals("--help") ) {

		printHelp();
		System.exit(1);

	    } else if( key.equals("-r") || key.equals("-rows") ) {

		if( i+1 >= argv.length ) {
		    System.out.println("Too few arguments for "+key+"." );
		    System.exit(1);
		}
		try { 
		    rows = Integer.parseInt(argv[i+1]);
		} catch( NumberFormatException e ) {
		    System.out.println( "This is not an integer: "+argv[i+1]+"." );
		    System.exit(1);
		}
		i+=2;

	    } else if( key.equals("-c") || key.equals("-cols") ) {

		if( i+1 >= argv.length ) {
		    System.out.println("Too few arguments for "+key+"." );
		    System.exit(1);
		}
		try { 
		    cols = Integer.parseInt(argv[i+1]);
		} catch( NumberFormatException e ) {
		    System.out.println( "This is not an integer: "+argv[i+1]+"." );
		    System.exit(1);
		}
		i+=2;

	    } else if( key.equals("-d") || key.equals("--directory") ) {
		
		if( i+1 >= argv.length ) {
		    System.out.println( "Too few arguments for key "+key+"." );
		    System.exit( 1 );
		}
		destDir = argv[i+1];
		i+=2;

	    } else if( key.equals("-t") || key.equals("--type") ) {
		
		if( i+1 >= argv.length ) {
		    System.out.println( "Too few arguments for key "+key+"." );
		    System.exit( 1 );
		}
		type = argv[i+1];
		i+=2;

	    } else if( key.equals("-s") || key.equals("--scaling") ) {
		
		if( i+1 >= argv.length ) {
		    System.out.println( "Too few arguments for key "+key+"." );
		    System.exit( 1 );
		}
		ratio = argv[i+1];
		i+=2;
		
	    } else if( key.equals("-p") || key.equals("--pattern") ) {
		
		if( i+1 >= argv.length ) {
		    System.out.println( "Too few arguments for key "+key+"." );
		    System.exit( 1 );
		}
		pattern = argv[i+1];
		i+=2;

	    } else if( i+1 >= argv.length ) {

		imgFile = argv[i];
		i++;

	    } else {

		System.out.println( "Unrecognized option: "+key+"." );
		System.exit(1);

	    }

	} // END while

	if( imgFile == null ) {
	    
	    System.out.println( "Please pass an image file." );
	    System.exit(1);

	}

	System.out.println( "Prepare to split ..." );
	System.out.println( "\timageFile="+imgFile );
	System.out.println( "\tcols="+cols+", rows="+rows );
	System.out.println( "\tdestinationDir="+destDir );
	System.out.println( "\tratio="+ratio );
	System.out.println( "\ttype="+type );
	System.out.println( "\tpattern="+pattern );

	try {

	    SplitImg split = new SplitImg( new File(imgFile), cols, rows );
	    split.setDestinationDir( new File(destDir) );
	    split.setRatio( ratio );
	    split.setOutputType( type );
	    split.setOutputNamePattern( pattern ); // may be null
	    split.performSplit();
	    split.writeSplits();

	    System.out.println( "Done." );

	} catch( IllegalArgumentException e ) {

	    e.printStackTrace();
	    System.exit( 1 );

	} catch( IOException e ) {

	    e.printStackTrace();
	    System.exit( 1 );
	    
	}
    }
}  
