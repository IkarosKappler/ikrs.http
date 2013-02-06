<?php

	header( "Content-Type: text/plain; charset=utf-8" );
	
	// This would be the simple way
	// readfile( "The Raven.txt" );



	// This is the 'hard' way :)
	$fh = fopen( "The Raven.txt", "r" );	
	while( ($line = fgets($fh)) ) 
	       echo $line;

	fclose( $fh );
	



?>