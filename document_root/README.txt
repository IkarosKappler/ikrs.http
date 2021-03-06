


TO DO:
[2013-10-01]
 - [DONE 2013-10-01]
   Typing 'HTTPD HEXDUMP' causes a RuntimeException to be thrown and the
   console dies! This must be fixed.
 - Additionally add a RuntimeException handler to fetch unexpected
   errors, so the console still continues working.

[2013-05-22]
 - The java.util.loggin.FileHandler does not support time stamps inside
   file names nor was it built to support subclasses that do.
   Solution: write a simple logfile cycle mechanism.

[2013-05-15]
 - [DONE 2013-07-25]
   Check the logging mechanism. It seems that the log files are not 
   properly written (line breaks are missing).

[2013-05-13]
 - [DONE 2013-05-15]
   The literals "ISO-8859-1" and "UTF-8" should be stored in global
   constants and replaced in all source code files.
 - Rediscuss the BasicArrayType class.
 - The BasicType parser (conversion from string) still does not 
   recognize arrays.
 - Find a way to store the input stream length in each form data item
   (if available); actually the input stream data is already buffered
   inside an ByteArrayInputStream.
 - The method ikrs.httpd.datatypes.DefaultFormData.get(String) misses
   a real search algorithm; currently it always return null!
 - Add the system requirements to the docs (jdk7 is required).

[2013-05-12]
 - [DONE 2013-05-13]
   Bind the GenericError.template.html document and make it 
   configurable. It should be used by the ErrorResponseBuilder if
   no configured error document is available (or configured but not 
   found).
 - Make a difference between general internal error messages and
   info/error messages that are suitable for public eyes
   (affects the FileSystemResourceAccessor and the 
   ErrorResponseBuilder class).

[2013-05-02]
 - [DONE 2013-05-08]
   Implement the HTTPConnectionUserID.toString() method so that equal
   IDs have equal string representations.

[2013-04-30]
 - [DONE 2013-05-02]
   Make the Headers-to-CGI mapping configurable (in the 
   ikrs.httpd.conf file).

[2013-04-29] 
 - Move the HexdumpOutputStream from ikrs.util to ikrs.io?

[2013-04-22]
 - On system start: print some output to show how many columns the hex
   output requires.
 - [DROPPED 2013-05-09; this is not possible with the default packages]
   Try to find the exact width for stdout before line breaks are 
   enforced.
 - [DONE; 2013-05-08; last error and last warning dropped (mind the 
   logger)]
   Add more runtime statistics:
     - #requests
     - #errors
     - #warnings
     - last error
     - last warning
 - [DROPPED; is already configurable]
   Make the HTTPHandler.isDisabledMethod() method configurable.

[2013-04-10]
 - Check the ikrs.io.MultiStopMarkInputStream.skip(long) method. Is
   skipping bytes on the upper stream enough?
 - [DONE 2013-04-11]
   Build a ReplacingInputStream based on the MultiStopMarkInputStream
   class.

[2013-04-08]
 - [DONE 2013-04-08; actually this was an mis-reported issue]
   On system start there is no check if the configured document root
   really exists (which might lead into a 50x Server Error). Add a check
   and console prompt if the user wishes to create the directory if not
   present.
   Solution: the HTTPConfiguration class manages the document root
   	     setting.
 - [DONE 2013-04-10]
   Write a MultiStopMarkInputStream that allows to pass a whole set of
   possible stop marks for the underlying stream. Idea: nest regular
   StopMarkInputStream into each other, each detected its own stop mark.
   This will be helpful for building the error document placeholder
   processor.

[2013-03-26]
 - [DONE 2013-04-03]
   The directory listing has trailing slashes in directory link names.

[2013-03-25]
 - [DONE 2013-04-02]
   The XMLExampleHandler counts exactly two more bytes as the file/sent
   data really has. Idea: some boundary bytes counted?

[2013-03-18]
 - The PostDataWrapper parses its FormData in one single step; this 
   should be optimized because large post data (~many MBs) will take 
   some time to be read. 
   Idea: use a realtime iterator/parser that reads one item after the 
   other, request by request. I fear the FormData interface has to be
   modified a bit for that ...
 - Additionally the FormData uses a linear seach algorithm to find 
   named items; this _could_ be optimized, but probably this will be
   obsolete once the issue above is solved.
 - [DONE 2013-03-25]
   The FormDataItem extends KeyValueStringPair, but it may also contain
   binary data! This has to be fixed.
 - [DONE 2013-03-25]
   The FormData has no method to retrieve the key set.

[2013-03-15]
 - [DONE 2013-05-12]
   The error documents should provide (optional) placeholders for the
   actual error messages generated by the server; something like
   {ERROR_MESSAGE}. Additionally prepare the same for {STATUS_CODE}
   and {REASON_PHRASE}.
 - Write a document telling what yucca/ikrs.httpd is.

[2013-03-13]
 - Yucca's command line has not (!) yet a finalize() method! The thread 
   cannot be interrupted.

[2013-03-11]
 - [DONE 2013-03-13]
   Yucca's command line currently does not allow additional white space
   chars between command name and additional arguments.

[2013-03-10]
 - [DONE 2013-03-15; actually there error did not exist]
   The error documents have a wrong favicon URI.

[2013-03-09]
 - [DONE 2013-03-10]
   Implement a VERSION command.
 - The ikrs.util.Command interface requires some self descriptive 
   methods, such as getDescription() oder describe( arg0, arg1, ... )?
 - Enhance the HELP command by adding the descriptive parts.
 - Implement the HTTPD STATUS command (method already exists but is
   still empty).
 - [DONE 2013-03-09]
   Implement a LOGLEVEL command for yucca.
 - [DONE 2013-03-09]
   Bild a HTTPD VERSION command.
 - There is an issue with the HTTP Date format: the hour part is wrong.

[2013-03-04]
 - [DONE 2013-03-09]
   Write a short documentation for all implemented commands.

[2013-03-02]
 - [DONE 2013-03-04]
   The STATUS command prints the current system configuration; the
   output also prints sensitive data such as the keystore and truststore
   passwords!

[2013-03-01]
 - [DONE 2013-03-02]
   The Log-Level passed at system start does not take effect.

[2013-02-28]
 - [DONE 2013-02-28]
   Add a DateFormat object that formats custom time stamps into a HTTP 
   Date format.
 - [DONE 2013-02-28]
   Add the HTTP Date format to the 'Date' header for 'Content-Range'
   replies in the ikrs.httpd.response.successful.OK class (status code
   206).
 - [DONE 2013-02-28]
   Add the HTTP Date format to the 'Expires' header for 'Content-Range'
   replies.
 - [DONE 2013-02-28]
   Expand the ResourceMetaData: 
    - last modified
    - etag

[2013-02-27]
 - [DONE 2013-02-28]
   The DefaultDirectoryResource adds one additional slash '/' 
   between path name and file name IF the path already ends with a
   slash.
 - [DONE 2013-02-27]
   Implement download recovery (from a specified byte position).
 - [DONE 2013-02-28]
   Server should send a status code 416 (Requested range not 
   satisfiable) on range errors; this requires a new exception type
   and the ResponseBuilder to be modified, I fear.
 - Write some test scripts to check if the 'Content-Range' request
   header is correctly processed.
 - [DONE 2013-02-28]
   Check if the 'Content-Range' header is also required in the response
   headers.
 - [DONE 2013-02-28]
   Requests containing the 'Content-Range' field SHOULD return a 206 
   (Partial Content) response.

[2013-02-26]
 - Large CGI output (> 100k?) should be buffered on the hard drive.
   Implement a block orientated buffered resource. And extend the
   CGIHandler (move the read process from the PHPHandler?).

[2013-02-25]
 - Build a customized ikrs.httpd-help command.
 - Build a structure that allows to create a Rewrite-Engine.
 - [DONE 2013-03-02]
   Change YuccaCommadFactory's command lookup order: parent commands 
   first, subclasses' commands later (make it possible to override 
   existing commands).
 - Think about the commandline- and command-factory interfaces one or 
   two more times; there might be some future issues regarding the type 
   params (Could the DefaultCommandFactory eventually take some type 
   params)?
 - Store the supported yucca commands inside global constants (affects
   YuccaCommandFactory and all custom command classes).
 - [2013-03-02 actually this was changed before]
   The ikrs.util.Command interface has a new method: 
   getSupportedCommands(). This method should be used in yucca's HELP
   command.
 - [DONE 2013-02-25]
   Move the favicon back to the document_root (pseudo convention in many 
   browsers).

[2013-02-05]
 - [DONE 2013-02-25]
   There is an issue with the FileSystemResourceLocator if the processed 
   resource comes from a FileHandler (such as the PHPHandler): the 
   eventually generated HTTP header containing the Content-Type is not 
   yet applied to the returned resource's meta data!

[2013-01-31]
 - [DONE 2013-01-05]
   Replace the internal maps by threadsafe versions!
 - [DONE 2013-01-05]
   The the directory list generator still uses the old (not valid any 
   more) favicon URL.
 - [DONE 2013-02-05]
   Clean up document_root directory: move all tests into a sub directory.

[2013-01-21]
 - [DONE 2013-01-23; in AbstractPreparedResponse.execute() fixed]
   Even if the request was HTTP version 1.0 the reply always comes in 
   version 1.1; this is wrong.

[2013-01-16]
 - [DONE 2013-01-23; the error document map is now configurable by 
   {USER_HOME}/.yuccasrv/ikrs.httpd.conf#ERROR_DOCUMENT.{STATUS_CODE}]
   The HTTP handlers internal default-error-document-map should be 
   configurable.

[2013-01-10]
 - [DONE 2013-01-16; the HTTP handler has a new method 
   'getDefaultErrorDocumentURI(...)'] 
   htaccess' ErrorDocument directives are not yet handled.

[2013-01-09]
 - [DONE 2013-01-09]
   The session timeout must be configurable. It is currently hard coded 
   to 300 seconds (5 minutes).
 - [DONE 2013-01-09] 
   Yucca's command line should be dynamically extensible by the 
   HTTHandler (add httpd configuration commands).

[2013-01-05]
 - Run some tests to check whether rejected requests really result into 
   an 503 Server Error.
 - Yucca.performQuit() still lacks some cleanup. Release listeners and 
   handlers.
 - [DONE 2013-03-15]
   The ikrs.http.HTTPHeaderLine is someting like a key-value-pair. It 
   should extend ikrs.util.KeyValueStringPair.
 - [DONE 2013-01-04]
   Build a nested htaccess evaluation. Currently the application just 
   searches up the document tree until the first htaccess file is found. 
   That is not correct! *All* htaccess files upon the request path must 
   be used; later occurrences override earlier stated stettings.
 - [DONE 2013-01-07]
   Currently the system writes a hexdump of requested files on stdout. 
   The output MUST be limited to a max mark, such as 1MB or so ... 
   otherwise the dump may take minutes to hours on large files before 
   the actual download can start.
 - [DONE 2013-01-07; log output moved to 
   ikrs.httpd.HTTPRequestDistributor]
   ikrs.httpd.HTTPHeaders.read(...) still writes debug data into stdout. 
   This should be configurable and (if enabled) written through the log 
   manager.

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
   There is a new class: ikrs.util.KeyValuePair which can parse 
   key-value-strings. 
   This class should be used in HTTPHeaderLine.parse( String ).
 - [DONE 2012-12-13]
   The httpd configuration can now be loaded into the server and it is 
   possible to disable HTTP methods; but the 
   HTTPHandler.getSupportedMethods() method still returns the whole list 
   (ignoring the config)!
   Maybe better replace the Method's signature and return a List/Set.
 - [DONE 2013-01-03]
   See RFC 2616, page 171:
   "Servers MUST report a 400 (Bad Request) error if an HTTP/1.1
        request does not include a Host request-header."
 - Check if The request is HTTP/1.1 or HTTP/1.0.

[2012-12-11]
 - [DONE 2013-01-03]
   The HTTPHandler.rejectedExecution(...) is still empty (called if all 
   preserved server threads are busy). 
   A fast (!) "503 Service Unavailable" response would be appropriate.
 - [DONE 2012-12-18]
   The customized HTTP config file (reference inside the yucca config) 
   must be interpreted and applied.
 - [DONE 2012-12-11; Note: the file is defined in the yucca config, not 
   in the http config. See HTTPConfiguration class].
   Add a 'fileHandlers' entity to the customized HTTP config.

[2012-12-10]
 - [DONE 2013-01-03]
   Yucca has still dozens of hard coded config identifiers; they should 
   be defined in a common header file.

[2012-12-09]
 - [DONE 2012-12-15; 
   ikrs.httpd.conf#DISABLE_METHOD.{METHOD} = { On | Off }]
   It must be possible to disable HTTP methods (particulary TRACE).

[2012-10-30]
 - [DONE 2012-11-27]
   The socketmanager must be capable to support secure sockets.

[2012-10-29]
 - [DONE 2012-10-29] 
   There is a new file called 'filehandlers.ini' in the working dir. It 
   specifies the handler classes.
   Implement the SetType- and AddType-Handling directives from the 
   htaccess (FileSystemResourceAccessor).

[2012-10-15]
 - There is an issue with the session/REMOTE_ADDR,REMOTE_HOST values; 
   they are not properly passed to the CGI environment vars when using 
   HTTP POST.
 - Make a new Directory-listing class with configurable/custom HTML 
   templates.

[2012-10-12]
 - Include the 'AUTH_TYPE' field into the CGI environment vars 
   (ikrs.http.filehandler.CGIHandler).
 - The CGI environment's 'PATH_TRANSLATED' must be a fully qualified 
   global URL.
 - The HTTPRequestDistributor must - when initializing the session - 
   put REMOTE_IDENT and REMOTE_USER (where get
   that from?).
 - [DONE 2012-10-15; actually this was a PHP config issue]
   PHPHandler: on HTTP POST with file transfer the post data must be 
   pre-parsed, the file data stored into a temp file and the POST params 
   adapted (mind the _FILES array).
 - Move the PHPHandler into its own package (there is the CGIHandler 
   which does most of the work). PHP has nothing to do with HTTP.

[2012-10-11]
 - The DefaultDirectoryResource should be splitted into two subclasses: 
   one for TEXT listing and one for XHTML.
 - Specify a general DirectoryResource interface and make it 
   configurable through the handler class.
 - Move class 'FileResource' from package 'ikrs.http' to 
   'ikrs.http.resource'.
 - [DONE 2012-10-15]
   Build the CGI interface (for PHP).
 - [DONE 2012-12-17; is configurable in 
   .yuccasrv/ikrs.httpd.config#DOCUMENT_ROOT]
   Make HTTPHandler.documentRoot configurable.
 - Make MIME type list configurable by a config file (currently the list 
   is hard coded).
 - .htgroups file has to be processed (if present). The current 
   implementation only handles .htaccess and .htpasswd. (remember: 
   'Require' directive).
 - [DONE]
   .htaccess: 'DirectoryIndex' directive must be handled.

[2012-10-10]
 - [DONE 2012-12-04] 
   Build method: OPTIONS ('Supported Methods').
 - [DONE 2012-12-04]
   Build method: HEAD.
 - [DONE 2012-12-05]
   Build method: TRACE.
 - The ErrorResponseBuilder should process all quotes and slashes on 
   client error 'Unauthorized'.

[2012-10-08]
 - [DONE 2012-10-10]
   ikrs.http.DefaultPostDataWrapper uses the 'AcceptHeaderParams' class 
   to parse the 'Content-Type' header data.
   The should be a more general parser class ('HeaderParams'?).


[2012-10-06]
 - [DONE 2012-10-11]
   Error 405 requires "Allow" header!
   (see: http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html)
   "10.4.6 405 Method Not Allowed
    The method specified in the Request-Line is not allowed for the 
    resource identified by the Request-URI. 
    The response MUST include an Allow header containing a list of valid 
    methods for the requested resource."

[2012-10-04]
 - Check if with 'Content-Type: application/x-www-form-urlencoded' 
   really the correct charset from the 'Accept-Charset' header is used! 
   (default is ISO-8859-1 --- is that really the correct default 
   value?).
 - [DONE 2013-01-08; there is a nested SESSION.INTERNAL child now 
   holding the environment data]
   The session handling needs to be built (internal SESSION.START and 
   SESSION.END).

[2012-10-01]
 - [DONE 2012-10-10; formdata/multipart handler implemented] 
   Currently the server does not fully understand HTTP POST.

[2012-09-29]
 - finish the ikrs.io.BytePositionInputStream implementation (mark() and 
   reset()).

 - [DONE 2012-09-30]
   The FileSystemResourceAccessor has a hard-coded PHP check (replace by 
   global handler-map).

 - [DONE 2012-09-30] 
   If the PHP execution fails (exitValue != 0) the system still returns 
   a "200 OK" response;
   There seems to be a special generated header field ('Status') 
   indicating such errors, too; 
   The resource.getMetaData().getOverrideHeaders() must be applied in 
   the actual HTTP response.

 - [DONE 2012-10-10]
   Generating the directory listing is just a dirty hack so far ... 
   replace by anything neat ...
   ... some HTML template stuff or a configurable HTML list generator or 
   something like that.

 - YUCCA's security manager must be configured (I think anyone can flood
   me right now).







The java documentation files are located at ./document_root/docs/ or
if 'document_root' is your configured {DOCUMENT_ROOT} (the default value)
the files will be available at http://127.0.0.1:8888/docs/.


Note: Some people asked: "Why port 8888?".
      For all who wonder about the strange port number; it's an adaption 
      of the 80/8080 HTTP port numbers (default and default alternative
      port).
      Alertá, alertá, antifascista!





Important Notes
===============

Use PHP-CGI instead of PHP-CLI
------------------------------
 - To enable PHP install the 'php-cgi' package (not the commandline 
   interface 'php-cli' or just 'php').
   Example:
   sudo apt-get install php5-cgi



The "File not found" issue
--------------------------
 - If the PHP interpreter is prompting on each PHP file you want to run 
   (using your browser):

   "<b>Security Alert!</b> The PHP CGI cannot be accessed directly"

   The CGI handler requires to set some environment vars for php-cgi. By 
   default this is not allowed by the 'cgi.force_redirect' directive in 
   your php.ini (php-cgi, NOT php-cli!).
   
   Locate your php-cgi configuration file, something like
   /etc/php5/cgi/php.ini,  
   near line ~834
   ; cgi.force_redirect is necessary to provide security running PHP as 
   a CGI under
   ; most web servers.  Left undefined, PHP turns this on by default.  
   You can
   ; turn it off here AT YOUR OWN RISK
   ; **You CAN safely turn this off for IIS, in fact, you MUST.**
   ; http://php.net/cgi.force-redirect
   ;cgi.force_redirect = 1

   Now set 'cgi.force_redirect' to 0 to allow modifications to php-cgi's 
   environment:
   cgi.force_redirect = 0



 - If the PHP interpreter is not prompting 

   "No input file specified." (Status: 404 Not Found)

   you have to set the document-root in your php-cgi config file,
   near line ~804:
   ; The root of the PHP pages, used only if nonempty.
   ; if PHP was not compiled with FORCE_REDIRECT, you SHOULD set 
   doc_root
   ; if you are running php as a CGI under any web server (other than 
   IIS)
   ; see documentation for security issues.  The alternate is to use the
   ; cgi.force_redirect configuration below
   ; http://php.net/doc-root
   doc_root = 

   Now set 'doc_root' to your document root.
   Example:
   doc_root = "/home/your_user_name/java/document_root"

   If this does not fix the problem add an additional document-root 
   directive:
   server.document_root = "/home/your_user_name/java/document_root"
   

   Now near line ~850:
   ; cgi.fix_pathinfo provides *real* PATH_INFO/PATH_TRANSLATED support 
   for CGI.  PHP's
   ; previous behaviour was to set PATH_TRANSLATED to SCRIPT_FILENAME, 
   and to not grok
   ; what PATH_INFO is.  For more information on PATH_INFO, see the cgi 
   specs.  Setting
   ; this to 1 will cause PHP CGI to fix its paths to conform to the 
   spec.  A setting
   ; of zero causes PHP to behave as before.  Default is 1.  You should 
   fix your scripts
   ; to use SCRIPT_FILENAME rather than PATH_TRANSLATED.
   ; http://php.net/cgi.fix-pathinfo
   ;cgi.fix_pathinfo=1

   Set cgi.fix_pathinfo to 1:
   cgi.fix_pathinfo = 1

   
   NOTE 1: The ikrs.http.filehandler.CGIHandler class will add an 
   	   additional security check field
	   REDIRECT_STATUS = 1
   	   to the environment vars which will finally fix the "File not 
	   found" issue.

   NOTE 2: I only changed my config in /etc/php5/cgi/, I left 
   	   /etc/php5/cli unchanged.



File-Upload issue: $_FILES is always empty
------------------------------------------

As soon as PHP (CGI mode) is running there might be the problem of 
failing file uploads (HTTP POST):
PHP's $_FILES array is always empty, even if the file data was 
successfully sent using HTTP POST method.
  (i)  Does you upload form use method="post" and 
       enctype="multipart/form-data"? :)
  (ii) Check your php.ini (in /etc/php5/cgi):
    - Check if 'file_uploads' is enabled.
    - Check if 'upload_tmp_dir' is set (to your system's tempfiles 
      directory or a temp dir of your choice) [near line ~890].
    - Check if your upload-file size does not exceed 'post_max_size'.



How to add custom file handlers
===============================  

There are three basic steps required to add a custom file- or directory 
handler.
 (i)   You need to build your own handler class that implements the 
       ikrs.httpd.FileHandler interface.
       Optionally you might just want to extend the abstract class 
       ikrs.httpd.filehandlers.AbstractFileHandler; 
       in that case you have to override the methods 
       'boolean requiresExistingFile()' and 'Resource process(...)'.

       If you are not sure what to do just see the example handler in
       ikrs.httpd.filehandlers.IkarosExampleHandler.


 (ii)  You need to bind your handler into the system and assign an alias 
       name.
       Do this by editing the {USER_HOME}/.yuccasrv/filehandlers.ini 
       file; add a new line
	<your_handler_alias> = <your_handler_class> 
			       [ <list_of_associated_file_extensions> ]
		
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
 The error response files. If the file for a given error code does not 
 exist the server will send an auto-generated response content.

 There is also a default error document for the case a passed status
 code does has no matching error document: 
 the GenericError.template.html file.


{DOCUMENT_ROOT}/system/styles/directory.list.css
------------------------------------------------
 The stylesheet for HTML/XHTML directory listings.





Realtime Commands
=================


HELP
----
 Prints a short summary of all available commands.
 Indeed the HELP command could be a bit mor verbose; a more enhanced
 version might come soon ...



HTTPD
-----
 This is a customized command for the httpd module (added to yucca's
 bind manager). Currently there is only one sub command, which is not
 fully implemented:
  - STATUS
    ------
    Prints the current status of the HTTPd module.

  - HEXDUMP FORMAT <format>
    -----------------------
    Changes the hexdump output column format. The expected value must
    be a comma separated integer list, each integer indicating the 
    respective column list and the number of integers the total number
    of columns.
    Example: the format '4,4,0,4,4' would generate a hexdump format
    	     like this:


    ~~~                                                    
    0x00000000  89504e47 0d0a1a0a  0000000d 49484452  .PNG........IHDR
    0x00000010  00000010 00000010  08060000 001ff3ff  ................
    0x00000020  61000000 01735247  4200aece 1ce90000  a....sRGB.......
    0x00000030  0006624b 474400ff  00ff00ff a0bda793  ..bKGD..........
    0x00000040  00000009 70485973  00000dd7 00000dd7  ....pHYs........
    0x00000050  0142289b 78000000  0774494d 4507dc0c  .B(.x....tIME...
    0x00000060  03112534 aaced90e  00000213 49444154  ..%4........IDAT
    0x00000070  38cba592 bfab5c65  10869f99 6fbedd73  8.....\e....o..s
    0x00000080  4eb20917 2f04ae08  c1340662 9b267083  N.../....4.b.&p.
    0x00000090  8db5a6cc 9f10b0f0  cf112d04 ab105258  ..........-...RX
    ~~~


LICENSE
-------
 Prints the license information.



LISTEN
------
 Add a new listening socket the yucca's bind manager:
 LISTEN [-p <protocol>] [-b <backlog>] <host> <port>

 If succeeded the bind manager fires a serverCreated() event and yucca
 will print a bind summary containing the created socket's ID (this 
 requires the log level to be at least set to Level.INFO; a command for
 changing the level at runtime will be coming soon).
 
 @See UNLISTEN <socketID>



LOGLEVEL
--------
 Prints or changes the current log level.
 LOGLEVEL [<level>]

 Valid log levels are: 
    - SEVERE (highest value)
    - WARNING
    - INFO
    - CONFIG
    - FINE
    - FINER
    - FINEST (lowest value) 

 These are the same values as in java.util.logging.Level (see java docs
 for further details).



QUIT
----
 This command quits Yucca. This means that all listening sockets will 
 be released, and the finalize() event will be fired to all running
 server threads and to all bound listeners (such as the HTTPd module).



STATUS
------
 Prints the current socket manager status summary containing basic 
 information such as bind addresses, ports, socket IDs, SSL 
 configuration, protocol information and backlog.
 
 Since version 1.0.3 this command is secure and does not print any
 sensitive data. 



UNLISTEN
--------
 Releases a listening socket specified by the passed socket ID:
 UNLISTEN <socketID>

 If you don't know a listening socket's ID just use the STATUS
 command. The socket ID is the unique number generated when the
 socket was bound by LISTEN.



VERSION
--------
 Prints the Yucca version number. This command does not take any
 arguments.



WARRANTY
--------
 Prints the warranty.





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


   
