


TO DO:
[2012-10-11]
 - The DefaultDirectoryResource should be splitted into two subclasses: one for TEXT listing and one for XHTML.
 - Specify a general DirectoryResource interface and make it configurable through the handler class.
 - Move class 'FileResource' from package 'ikrs.http' to 'ikrs.http.resource'.
 - Build the CGI interface (for PHP).
 - Make HTTPHandler.documentRoot configurable.
 - Make MIME type list configurable by a config file (currently the list is hard coded).
 - .htgroups file has to be processed (if present). The current implementation only handles .htaccess and .htpasswd.
   (remember: 'Require' directive).
 - [DONE]
   .htaccess: 'DirectoryIndex' directive must be handled.

[2012-10-10]
 - Build method: OPTIONS ('Supported Methods').
 - Build method: HEAD.
 - Build method: TRACE.
 - The ErrorResponseBuilder should process all quotes and slashes on client error 'Unauthorized'.

[2012-10-08]
 - [DONE 2012-10-10]
   ikrs.http.DefaultPostDataWrapper uses the 'AcceptHeaderParams' class to parse the 'Content-Type' header data.
   The should be a more general parser class ('HeaderParams'?).


[2012-10-06]
 - [DONE 2012-10-11]
   Error 405 requires "Allow" header!
   (see: http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html)
   "10.4.6 405 Method Not Allowed
    The method specified in the Request-Line is not allowed for the resource identified by the Request-URI. 
    The response MUST include an Allow header containing a list of valid methods for the requested resource."

[2012-10-04]
 - Check if with 'Content-Type: application/x-www-form-urlencoded' really the correct charset from the 
  'Accept-Charset' header is used! (default is ISO-8859-1 --- is that really the correct default value?).
 - The session handling needs to be built (internal SESSION.START and SESSION.END).

[2012-10-01]
 - [DONE 2012-10-10; formdata/multipart handler implemented] 
   Currently the server does not fully understand HTTP POST.

[2012-09-29]
 - finish the ikrs.io.BytePositionInputStream implemenation (mark() and reset()).

 - the FileSystemResourceAccessor has a hard-coded PHP check (replace by global handler-map).

 - [DONE 2012-09-30] 
   If the PHP execution fails (exitValue != 0) the system still returns a "200 OK" response;
   There seems to be a special generated header field ('Status') indicating such errors, too;
   The resource.getMetaData().getOverrideHeaders() must be applied in the actual HTTP response.

 - Generating the directory listing is just a dirty hack so far ... replace by anything neat ...
   ... some HTML template stuff or a configurable HTML list generator or something like that.

 - YUCCA's security manager must be configured (I think anyone can flood me right now).




Important Notes
===============

 - To enable PHP install the 'php-cgi' package (not the commandline interface 'php-cli' or just 'php').
   Example:
   sudo apt-get install php5-cgi




Files
=====

_.yuccasrv/server.xml
---------------------
 This file should be moved to {USER_HOME}/.yuccasrv/server.xml in future versions.


{DOCUMENT_ROOT}/system/errors/Error.{STATUS_CODE}.html
------------------------------------------------------
 The error response files. If the file for a given error code does not exist the server will
 send an auto-generated response content.


{DOCUMENT_ROOT}/system/styles/directory.list.css
------------------------------------------------
 The stylesheet for HTML/XHTML directory listings.



Tools
=====

ikrs.http.MD5
-------------
 A password hash generator for htaccess 'Digest' authorization.
 Usage: java ikrs.http.MD5 -r <realm> [-s <salt>] -u <user> [-p <password>]
 	- salt must be 8 characters long.
