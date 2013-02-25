<?php

/**
 * This is just a test script to check if binary (here: image) data
 * is processed and displayed correctly.
 *
 * @author Ikaros Kappler
 * @date 2013-02-25
 **/

header( "Content-Type: image/jpg" );

$bytes = readfile( "testimage.jpg" );



?>