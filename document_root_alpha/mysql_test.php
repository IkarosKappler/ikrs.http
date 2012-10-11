<?php

	header( "Content-Type: text/plain; charset=utf-8" );

	echo "Opening mysql connection ...\n";

	$mcon = mysql_connect( "127.0.0.1" );

	
	echo "Closing mysql connection ...\n";
	if( $mcon )
	    mysql_close( $mcon );

?>