<?php

	// This displays the characters wrong (utf-8 encoded PHP file!)
	header( "Content-type: text/plain; charset=iso-8859-1" );

	// This display the characters right
	// header( "Content-type: text/plain; charset=utf-8" );

?>

These characters are sent with a wrong charset (iso-8859-1) and should look broken:


Ä
Ö
Ü

ä 
ö
ü

ß
