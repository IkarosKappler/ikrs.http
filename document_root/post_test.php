<html>
<head>
<title>Post test</title>

</head>


<body>


This form sends the data in 'multipart/form-data' format.
<form method="post" action="post_test.php" enctype="multipart/form-data">
  Text_A: <input type="text" name="my_text_A" value="This is my very nice test (A)." /><br/>
  Text_B: 
  <textarea name="my_text_B">
             Ein ganz toller Text mit &Auml; Umlauten und &szlig;onderzeichen.
	     Sogar Zeilenumbr&uuml;che gibt's.
	     Und &-Zeichen.
  </textarea><br/>
  File: <input name="my_file" type="file" size="50" maxlength="100000" accept="text/*" /><br/>
  <input type="submit" /><br/>
</form>
<br/>
<br/>


This form sends the data in the DEFAULT format.
<form method="post" action="post_test.php" >
  Text A: <input type="text" name="my_text_A" value="This is my very nice test (B)." /><br/>
  Text_B: 
  <textarea name="my_text_B">
             Ein ganz toller Text mit &Auml; Umlauten und &szlig;onderzeichen.
	     Sogar Zeilenumbr&uuml;che gibt's.
	     Und &-Zeichen.
  </textarea><br/>
  File: <input name="my_file" type="file" size="50" maxlength="100000" accept="text/*" /><br/>
  <input type="submit" /><br/>
</form>

<?php

	echo "_GET=";
	print_r( $_GET );
	echo "<br/>\n";


	echo "_POST=";
	print_r($_POST);
	echo "<br/>\n";
	

?>


</body>
</html>
