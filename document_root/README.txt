



TO DO:
[2013-01-09]
 - The session timeout must be configurable. It is currently hard coded to 300 seconds (5 minutes).

[2013-01-05]
 - Run some tests to check whether rejected requests really result into an 503 Server Error.
 - Yucca.performQuit() still lacks some cleanup. Release listeners and handlers.
 - The ikrs.http.HTTPHeaderLine is someting like a key-value-pair. It should extend
   ikrs.util.KeyValueStringPair.
 - [DONE 2013-01-04]
   Build a nested htaccess evaluation. Currently the application just searches up the document
   tree until the first htaccess file is found. That is not correct! *All* htaccess files
   upon the request path must be used; later occurrences override earlier stated stettings.
 - [DONE 2013-01-07]
   Currently the system writes a hexdump of requested files on stdout. The output MUST be limited to 
   a max mark, such as 1MB or so ... otherwise the dump may take minutes to hours on large files
   before the actual download can start.
 - [DONE 2013-01-07; log output moved to ikrs.httpd.HTTPRequestDistributor]
   ikrs.httpd.HTTPHeaders.read(...) still writes debug data into stdout. This should be configurable
   and (if enabled) written through the log manager.

[2012-12-17]
 - [DONE 2012-12-18]
   The configuration directory was moved to {USER_HOME}/.yuccasrv.
   Build a directory check/create/copy on system start.

[2012-12-15]
 - [DONE 2013-01-03; moved to ikrs.util.KeyValueStringPair]
   There is semantic clash: 
   ikrs.util.KeyValuePair <=> ikrs.httpd.datatype.KeyVauleStringPair
   Idea: move KeyValueStringPair to ikrs.util?
 - [DONE 2012-12-16]
   Change the ikrs.http package name into 'ikrs.httpd' (it's a DAEMON!).
 - [DONE 2012-12-17; note: the commands are 'warranty' and 'license']
   Add the 'show w' and 'show c' commands to the command line.
 - Add the GPL application note to *all* source code files (example is in
   gpl-3.0_howto_apply.txt).
 - Separate the *.java and the *.class files into different directories
   /src/ and /bin/
 - Make different JAR archives from the packages:
    - ikrs.util
    - ikrs.typesystem
    - ikrs.yucca
    - ikrs.httpd

[2012-12-12]
 - [DONE 2012-12-13]
   There is a new class: ikrs.util.KeyValuePair which can parse key-value-strings. 
   This class should be used in HTTPHeaderLine.parse( String ).
 - [DONE 2012-12-13]
   The httpd configuration can now be loaded into the server and it is possible to disable HTTP methods;
   but the HTTPHandler.getSupportedMethods() method still returns the whole list (ignoring the config)!
   Maybe better replace the Method's signature and return a List/Set.
 - [DONE 2013-01-03]
   See RFC 2616, page 171:
   "Servers MUST report a 400 (Bad Request) error if an HTTP/1.1
        request does not include a Host request-header."
 - Check if The request is HTTP/1.1 or HTTP/1.0.

[2012-12-11]
 - [DONE 2013-01-03]
   The HTTPHandler.rejectedExecution(...) is still empty (called if all preserved server threads are busy). 
   A fast (!) "503 Service Unavailable" response would be appropriate.
 - [DONE 2012-12-18]
   The customized HTTP config file (reference inside the yucca config) must be interpreted and applied.
 - [DONE 2012-12-11; Note: the file is defined in the yucca config, not in the http config. See HTTPConfiguration class]
   Add a 'fileHandlers' entity to the customized HTTP config.

[2012-12-10]
 - [DONE 2013-01-03]
   Yucca has still dozens of hard coded config identifiers; they should be defined in a common header file.

[2012-12-09]
 - [DONE 2012-12-15; ikrs.httpd.conf#DISABLE_METHOD.{METHOD} = { On | Off }]
   It must be possible to disable HTTP methods (particulary TRACE).

[2012-10-30]
 - [DONE 2012-11-27]
   The socketmanager must be capable to support secure sockets.

[2012-10-29]
 - [DONE 2012-10-29] 
   There is a new file called 'filehandlers.ini' in the working dir. It specifies the handler classes.
   Implement the SetType- and AddType-Handling directives from the htaccess (FileSystemResourceAccessor).

[2012-10-15]
 - There is an issue with the session/REMOTE_ADDR,REMOTE_HOST values; they are not properly passed to the
   CGI environment vars when using HTTP POST.
 - Make a new Directory-listing class with configurable/custom HTML templates.

[2012-10-12]
 - Include the 'AUTH_TYPE' field into the CGI environment vars (ikrs.http.filehandler.CGIHandler).
 - The CGI environment's 'PATH_TRANSLATED' must be a fully qualified global URL.
 - The HTTPRequestDistributor must - when initializing the session - put REMOTE_IDENT and REMOTE_USER (where get
   that from?).
 - [DONE 2012-10-15; actually this was a PHP config issue]
   PHPHandler: on HTTP POST with file transfer the post data must be pre-parsed, the file data stored into a 
   temp file and the POST params adapted (mind the _FILES array).
 - Move the PHPHandler into its own package (there is the CGIHandler which does most of the work). PHP has nothing
   to do with HTTP.

[2012-10-11]
 - The DefaultDirectoryResource should be splitted into two subclasses: one for TEXT listing and one for XHTML.
 - Specify a general DirectoryResource interface and make it configurable through the handler class.
 - Move class 'FileResource' from package 'ikrs.http' to 'ikrs.http.resource'.
 - [DONE 2012-10-15]
   Build the CGI interface (for PHP).
 - [DONE 2012-12-17; is configurable in .yuccasrv/ikrs.httpd.config#DOCUMENT_ROOT]
   Make HTTPHandler.documentRoot configurable.
 - Make MIME type list configurable by a config file (currently the list is hard coded).
 - .htgroups file has to be processed (if present). The current implementation only handles .htaccess and .htpasswd.
   (remember: 'Require' directive).
 - [DONE]
   .htaccess: 'DirectoryIndex' directive must be handled.

[2012-10-10]
 - [DONE 2012-12-04] 
   Build method: OPTIONS ('Supported Methods').
 - [DONE 2012-12-04]
   Build method: HEAD.
 - [DONE 2012-12-05]
   Build method: TRACE.
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
 - [DONE 2013-01-08; there is a nested SESSION.INTERNAL child now holding the environment data]
   The session handling needs to be built (internal SESSION.START and SESSION.END).

[2012-10-01]
 - [DONE 2012-10-10; formdata/multipart handler implemented] 
   Currently the server does not fully understand HTTP POST.

[2012-09-29]
 - finish the ikrs.io.BytePositionInputStream implementation (mark() and reset()).

 - [DONE 2012-09-30]
   The FileSystemResourceAccessor has a hard-coded PHP check (replace by global handler-map).

 - [DONE 2012-09-30] 
   If the PHP execution fails (exitValue != 0) the system still returns a "200 OK" response;
   There seems to be a special generated header field ('Status') indicating such errors, too;
   The resource.getMetaData().getOverrideHeaders() must be applied in the actual HTTP response.

 - [DONE 2012-10-10]
   Generating the directory listing is just a dirty hack so far ... replace by anything neat ...
   ... some HTML template stuff or a configurable HTML list generator or something like that.

 - YUCCA's security manager must be configured (I think anyone can flood me right now).




Important Notes
===============

Use PHP-CGI instead of PHP-CLI
------------------------------
 - To enable PHP install the 'php-cgi' package (not the commandline interface 'php-cli' or just 'php').
   Example:
   sudo apt-get install php5-cgi



The "File not found" issue
--------------------------
 - If the PHP interpreter is prompting on each PHP file you want to run (using your browser):

   "<b>Security Alert!</b> The PHP CGI cannot be accessed directly"

   The CGI handler requires to set some environment vars for php-cgi. By default this is not allowed
   by the 'cgi.force_redirect' directive in your php.ini (php-cgi, NOT php-cli!).
   
   Locate your php-cgi configuration file, something like
   /etc/php5/cgi/php.ini,  
   near line ~834
   ; cgi.force_redirect is necessary to provide security running PHP as a CGI under
   ; most web servers.  Left undefined, PHP turns this on by default.  You can
   ; turn it off here AT YOUR OWN RISK
   ; **You CAN safely turn this off for IIS, in fact, you MUST.**
   ; http://php.net/cgi.force-redirect
   ;cgi.force_redirect = 1

   Now set 'cgi.force_redirect' to 0 to allow modifications to php-cgi's environment:
   cgi.force_redirect = 0



 - If the PHP interpreter is not prompting 

   "No input file specified." (Status: 404 Not Found)

   you have to set the document-root in your php-cgi config file,
   near line ~804:
   ; The root of the PHP pages, used only if nonempty.
   ; if PHP was not compiled with FORCE_REDIRECT, you SHOULD set doc_root
   ; if you are running php as a CGI under any web server (other than IIS)
   ; see documentation for security issues.  The alternate is to use the
   ; cgi.force_redirect configuration below
   ; http://php.net/doc-root
   doc_root = 

   Now set 'doc_root' to your document root.
   Example:
   doc_root = "/home/your_user_name/java/document_root"

   If this does not fix the problem add an additional document-root directive:
   server.document_root = "/home/your_user_name/java/document_root"
   

   Now near line ~850:
   ; cgi.fix_pathinfo provides *real* PATH_INFO/PATH_TRANSLATED support for CGI.  PHP's
   ; previous behaviour was to set PATH_TRANSLATED to SCRIPT_FILENAME, and to not grok
   ; what PATH_INFO is.  For more information on PATH_INFO, see the cgi specs.  Setting
   ; this to 1 will cause PHP CGI to fix its paths to conform to the spec.  A setting
   ; of zero causes PHP to behave as before.  Default is 1.  You should fix your scripts
   ; to use SCRIPT_FILENAME rather than PATH_TRANSLATED.
   ; http://php.net/cgi.fix-pathinfo
   ;cgi.fix_pathinfo=1

   Set cgi.fix_pathinfo to 1:
   cgi.fix_pathinfo = 1

   
   NOTE 1: The ikrs.http.filehandler.CGIHandler class will add an additional security check field
	   REDIRECT_STATUS = 1
   	   to the environment vars which will finally fix the "File not found" issue.

   NOTE 2: I only changed my config in /etc/php5/cgi/, I left /etc/php5/cli unchanged.



File-Upload issue: $_FILES is always empty
------------------------------------------

As soon as PHP (CGI mode) is running there might be the problem of failing file uploads (HTTP POST):
PHP's $_FILES array is always empty, even if the file data was successfully sent using HTTP POST 
method.
  (i)  Does you upload form use method="post" and enctype="multipart/form-data"? :)
  (ii) Check your php.ini (in /etc/php5/cgi):
    - Check if 'file_uploads' is enabled.
    - Check if 'upload_tmp_dir' is set (to your system's tempfiles directory or a temp dir of your 
      choice) [near line ~890].
    - Check if your upload-file size does not exceed 'post_max_size'.



How to add custom file handlers
===============================  

There are three basic steps required to add a custom file- or directory handler.
 (i)   You need to build your own handler class that implements the 
       ikrs.httpd.FileHandler interface.
       Optionally you might just want to extend the abstract class 
       ikrs.httpd.filehandlers.AbstractFileHandler; 
       in that case you have to override the methods 'boolean requiresExistingFile()' 
       and 'Resource process(...)'.

       If you are not sure what to do just see the example handler in
       ikrs.httpd.filehandlers.IkarosExampleHandler.


 (ii)  You need to bind your handler into the system and assign an alias name.
       Do this by editing the {USER_HOME}/.yuccasrv/filehandlers.ini file; add a new
       line
		<your_handler_alias> = <your_handler_class> [ <list_of_associated_file_extensions> ]
		
       where
	   - <your_handler_alias>                 can be any name (not including white spaces), but 
	     					  it should be unique!
	   - <your_handler_class>                 must be the fully qualified class name of your 
	     					  handler (with package name!)
	   - <list_of_associated_file_extensions> Is an optional list of file extensions you want to
	     					  have associated with you handler BY DEFAULT (global!).

       Example:
	  IkarosExampleHandler=ikrs.httpd.filehandler.IkarosExampleHandler
	

 (iii) If you didn't already associate file extensions with your handler you have to do so in the last
       step. Configure the desired directory(~ies) by the use of htaccess' SetHandler/AddHandler
       directives.
         - 'SetHandler <your_handler_alias>' sets your handler for the current directory (and all files
	   inside and all sub directories).
	 - AddHandler <your_handler_alias> <list_of_associated_file_extensions>' addy your handler for
	   the given file extensions).

       Examples:
	  (a) SetHandler IkarosExampleHandler
	  (b) AddHandler IkarosExampleHandler .ikrs


 Note: file extensions are compared case-insensitive, so '.TXT' would also match '.txt'.





Files
=====

{USER_HOME}/.yuccasrv/
----------------------
This is the user specific directory containing all configuration files required to run ikrs.yucca/ikrs.httpd.


{USER_HOME}/.yuccasrv/server.xml
--------------------------------
 This is the global yucca server configuration file.


{USER_HOME}/.yuccasrv/ikrs.httpd.conf
-------------------------------------
 ...

{USER_HOME}/.yuccasrv/filehandlers.ini
--------------------------------------
 ...



{DOCUMENT_ROOT}/system/errors/Error.{STATUS_CODE}.html
------------------------------------------------------
 The error response files. If the file for a given error code does not exist the server will
 send an auto-generated response content.


{DOCUMENT_ROOT}/system/styles/directory.list.css
------------------------------------------------
 The stylesheet for HTML/XHTML directory listings.



Tools
=====

ikrs.httpd.MD5
-------------
 A password hash generator for htaccess 'Digest' authorization.
 Usage: java ikrs.httpd.MD5 -r <realm> [-s <salt>] -u <user> [-p <password>]
 	- salt must be 8 characters long.

 Example:
    > java ikrs.httpd.MD5 -r "My cool realm" -u testuser -p test
    Generating random salt ... 
    Your hashed password (line for .htpasswd): 
    testuser:${ikrs.http.MD5}$xSngy8uB$9f8d6ab4029a6f8ce8fcdbdb7e671490

 Store the last line printed into your .htpasswd file to add the user to the user list (AuthUserFile).


   
