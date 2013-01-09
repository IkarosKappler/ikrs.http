package ikrs.httpd;

/**
 * @author Ikaros Kappler
 * @date 2013-01-09
 * @version 1.0.0
 **/


import ikrs.yuccasrv.Yucca;

public class Run {


    public static void main( String[] argv ) {

	LocalCommandFactory myCommandFactory = new LocalCommandFactory();

	Yucca yucca = Yucca.runYucca( argv );
	yucca.getCommandLine().getCommandFactory().setParentFactory( myCommandFactory );

	// Note: yucca is already running in a seperate thread here!
	

    }


}