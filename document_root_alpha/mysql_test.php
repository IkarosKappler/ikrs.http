<?php

	header( "Content-Type: text/plain; charset=utf-8" );

	echo "Opening mysql connection ...\n";

	$mcon = mysql_connect( "127.0.0.1" );

	if( !$mcon ) {

	    echo "Error: " . mysql_error() . "\n";

	} else {
	
		echo "Closing mysql connection ...\n";
		mysql_close( $mcon );

	}

?>