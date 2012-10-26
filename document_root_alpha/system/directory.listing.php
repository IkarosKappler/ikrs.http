<?php 
      echo "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
	  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Contents of /polygon_berlin gsdf</title>
<link rel="icon" href="/favicon.ico" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="/system/styles/directory.list.css" /></head>

<body>


<h2>Index of <?php echo $_GET["requestURI"]; ?><br/>
<br/>
</h2>

<?php

	$requestURI = $_GET["requestURI"];
	$index = strrpos( $requestURI, "/" );
	if( $index === FALSE )
	    $index = strrpos( $requestURI, "\\" );

	
	// Returns a '.' if not parent file exists.
	//$parentDir = dirname( $_GET["absoluteDirectoryPath"] );
	if( $index == 0 || $index === FALSE )
	    $parentDir = "/";
	else
		$parentDir = substr( $requestURI, 0, $index );
	echo "<a href=\"" . $parentDir . "\">parent dir</a><br/><br/>\n";

?>

<table>
<!--
<tr>
      <td><a href="/polygon_berlin/v0.2.2">v0.2.2</a></td>
      <td align="right">4.096 bytes</td>
      <td>dir</td>
      <td>2012-10-18 01:20:44</td>
</tr>
-->

<?php


	$filePath = $_GET["absoluteDirectoryPath"];
	echo "Dir=" .  $filePath . "\n";

	//$absoluteParentDir = dirname( $filePath );	
  
	$dir = opendir( $filePath );
	
	//$fileList = scandir( $dir );
	if( strrpos($filePath,"/") != strlen($filePath)-1 )
	    $filePath .= "/";
	
	
	while( ($filename = readdir($dir)) !== false ) {

	       //print_r( $filename );
	       //echo "<br/>\n";

	       $filesize = filesize( $filePath . $filename );
	       $filetype = "file";
	       if( is_dir($filePath.$filename) )
	       	   $filetype = "dir";
	       else if( is_file($filePath.$filename) )
	       	    $filetype = "file";
	       else
			continue;

	       echo "<tr>\n";
	       echo "	<td><a href=\"" . $filePath . $filename . "\">" . $filename . "</a></td>\n";
      	       echo "	<td align=\"right\">" . $filesize . " bytes</td>\n";
      	       echo "	<td>" . $filetype . "</td>\n";
      	       echo "	<td>2012-10-18 01:20:44</td>\n";
	       echo "</tr>\n";

	}
	

	closedir( $dir );

	echo "_GET=";
	print_r( $_GET );
	echo "<br/>\n";

?>

</table>



</body>
</html>

