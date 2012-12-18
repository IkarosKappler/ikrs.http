<?php


	header( "Content-Type: text/plain; charset=utf-8" );

	echo "Opening psql connection ...\n";

	$pcon = pg_connect( "host=127.0.0.1 user=testuser password=xxxxxxxx dbname=my_db" );

	if( !$pcon ) {

	    echo "Error: " . pg_last_error() . "\n";

	} else {
	
		echo "Closing pg connection ...\n";
		pg_close( $pcon );

	}



?>