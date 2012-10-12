<html>
<head>
<title>POST test</title>

</head>


<body>



This form sends a POST request.
<form method="post" action="HTTP_POST.php" >
  Text A: <input type="text" name="my_text_A" value="This is my very nice test (B)." /><br/>
  Text_B: 
  <textarea name="my_text_B">
             A real nice Test with special &Auml; chars other &szlig; international stuff.
	     There are even line breaks!
	     And &-chars.
  </textarea><br/>
  File: <input name="my_file" type="file" size="50" maxlength="100000" accept="text/*" /><br/>
  <input type="submit" /><br/>
</form>

<code>
<?php
   
   echo "_POST=";
   print_r( $_POST );
   echo "<br/><br/>\n";
   

   echo "getenv('AUTH_TYPE')=" .         getenv("AUTH_TYPE") . "<br/>\n";
   echo "getenv('CONTENT_LENGTH')=" .    getenv("CONTENT_LENGTH") . "<br/>\n";
   echo "getenv('CONTENT_TYPE')=" .      getenv("CONTENT_TYPE") . "<br/>\n";
   echo "getenv('GATEWAY_INTERFACE')=" . getenv("GATEWAY_INTERFACE") . "<br/>\n";
   echo "getenv('PATH_INFO')=" .         getenv("PATH_INFO") . "<br/>\n";
   echo "getenv('PATH_TRANSLATED')=" .   getenv("PATH_TRANSLATED") . "<br/>\n";
   echo "getenv('QUERY_STRING')=" .      getenv("QUERY_STRING") . "<br/>\n";
   echo "getenv('REMOTE_ADDR')=" .       getenv("REMOTE_ADDR") . "<br/>\n";
   echo "getenv('REMOTE_HOST')=" .       getenv("REMOTE_HOST") . "<br/>\n";
   echo "getenv('REMOTE_IDENT')=" .      getenv("REMOTE_IDENT") . "<br/>\n";
   echo "getenv('REMOTE_USER')=" .       getenv("REMOTE_USER") . "<br/>\n";
   echo "getenv('REQUEST_METHOD')=" .    getenv("REQUEST_METHOD") . "<br/>\n";
   echo "getenv('SCRIPT_NAME')=" .       getenv("SCRIPT_NAME") . "<br/>\n";
   echo "getenv('SERVER_NAME')=" .       getenv("SERVER_NAME") . "<br/>\n";
   echo "getenv('SERVER_PORT')=" .       getenv("SERVER_PORT") . "<br/>\n";
   echo "getenv('SERVER_PROTOCOL')=" .   getenv("SERVER_PROTOCOL") . "<br/>\n";
   echo "getenv('SERVER_SOFTWARE')=" .   getenv("SERVER_SOFTWARE") . "<br/>\n";
   
?>
</code>

</body>
</html>
