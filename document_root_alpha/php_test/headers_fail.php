<?php

	header( "Content-Type: text/plain; charset=utf-8" );
	header( "Content-Type: text/plain; charset=iso-8859-1", TRUE );
	header( "Content-Type: text/plain; charset=utf-8" );

	// This works with 'php'/'php-cli' only! 
	// (tell the docs, but it also seems to work with php-cgi)
	header( "HTTP/1.0 404 Not Found");

	// Works with 'php-gci', tooi
	//header("Status: 404 Not Found");


	echo "Test\n";
	echo "\n";

	echo mal nen laufzeitfehler erzeugen ....	

	echo "Das hier ein ein ganz toller PHP-Test.\n";



?>