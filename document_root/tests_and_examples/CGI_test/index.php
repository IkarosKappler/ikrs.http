<?php

   header( "Content-Type: text/plain" );

   echo "This script simply prints out all CGI environment vars:\n";
   echo "\n";

   
   echo "_POST=";
   print_r( $_POST );
   echo "\n\n";

   echo "_FILES=";
   print_r( $_FILES );
   echo "\n\n";
   

   echo "getenv('AUTH_TYPE')=" .             getenv("AUTH_TYPE") . "\n";
   echo "getenv('CONTENT_LENGTH')=" .        getenv("CONTENT_LENGTH") . "\n";
   echo "getenv('CONTENT_TYPE')=" .          getenv("CONTENT_TYPE") . "\n";
   echo "getenv('DOCUMENT_ROOT')=" .         getenv("DOCUMENT_ROOT") . "\n";
   echo "getenv('GATEWAY_INTERFACE')=" .     getenv("GATEWAY_INTERFACE") . "\n";
   echo "getenv('PATH_INFO')=" .             getenv("PATH_INFO") . "\n";
   echo "getenv('PATH_TRANSLATED')=" .       getenv("PATH_TRANSLATED") . "\n";
   echo "getenv('QUERY_STRING')=" .          getenv("QUERY_STRING") . "\n";
   echo "getenv('REMOTE_ADDR')=" .           getenv("REMOTE_ADDR") . "\n";
   echo "getenv('REMOTE_HOST')=" .           getenv("REMOTE_HOST") . "\n";
   echo "getenv('REMOTE_IDENT')=" .          getenv("REMOTE_IDENT") . "\n";
   echo "getenv('REMOTE_USER')=" .           getenv("REMOTE_USER") . "\n";
   echo "getenv('REQUEST_METHOD')=" .        getenv("REQUEST_METHOD") . "\n";
   echo "getenv('REQUEST_URI')=" .           getenv("REQUEST_URI") . "\n";
   echo "getenv('SCRIPT_FILENAME')=" .       getenv("SCRIPT_FILENAME") . "\n";
   echo "getenv('SCRIPT_NAME')=" .           getenv("SCRIPT_NAME") . "\n";
   echo "getenv('SERVER_NAME')=" .           getenv("SERVER_NAME") . "\n";
   echo "getenv('SERVER_PORT')=" .           getenv("SERVER_PORT") . "\n";
   echo "getenv('SERVER_PROTOCOL')=" .       getenv("SERVER_PROTOCOL") . "\n";
   echo "getenv('SERVER_SOFTWARE')=" .       getenv("SERVER_SOFTWARE") . "\n";
   echo "getenv('HTTP_ACCEPT')=" .           getenv("HTTP_ACCEPT") . "\n";
   echo "getenv('HTTP_ACCEPT_CHARSET')=" .   getenv("HTTP_ACCEPT_CHARSET") . "\n";
   echo "getenv('HTTP_ACCEPT_ENCODING')=" .  getenv("HTTP_ACCEPT_ENCODING") . "\n";
   echo "getenv('HTTP_ACCEPT_LANGUAGE')=" .  getenv("HTTP_ACCEPT_LANGUAGE") . "\n";
   echo "getenv('HTTP_CONNECTION')=" .       getenv("HTTP_CONNECTION") . "\n";
   echo "getenv('HTTP_COOKIE')=" .           getenv("HTTP_COOKIE") . "\n";
   echo "getenv('HTTP_HOST')=" .             getenv("HTTP_HOST") . "\n";
   echo "getenv('HTTP_REFERER')=" .          getenv("HTTP_REFERER") . "\n";
   echo "getenv('HTTP_USER_AGENT')=" .       getenv("HTTP_USER_AGENT") . "\n";

   echo "\n";
   echo "\n";

   echo "_SERVER=";
   print_r( $_SERVER );
   echo "\n";
   
?>

