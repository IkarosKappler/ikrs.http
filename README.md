ikrs.httpd
==========

A free tiny java written http server.





Changes
=======

[2013-04-29]
  - Added javadocs at ./document_root/docs/

[2013-04-23]
  - HTTPD command 'HEXDUMP FORMAT' for hexdump output configuration added.
  - Moved the HexDumpOutputStream creation from AbstractPreparedResponse
    to HTTPHandler.
  - Added 'HEXDUMP_FORMAT' to the ikrs.httpd.conf file.

[2013-04-22]
  - Added the HTTPDRuntimeStatistics class.
  - Added a static HTTPHandler.sharedInstance field.
  - Extended the HTTPD STATUS command.

[2013-04-11]
  - Built a ReplacingInputStream based on the MultiStopMarkInputStream
    class.

[2013-04-10]
  - The ikrs.io.StopMarkInputStream.stopMarkReached() method did not
    return a proper value; even if the stop mark was reached it 
    returned false.
  - Wrote a MultiStopMarkInputStream that allows to pass a whole set of
    possible stop marks for the underlying stream. Idea: use the new
    stream 'tokenizer' to build configurable error documents 
    (placeholder processor).
  - Added the ikrs.io.StopMarkInputStream.continueStream() and 
    *.continueStream(boolean overrideEOI) methods.
  - Added ikrs.util.ByteArrayComparator.

[2013-04-03]
  - Fixed: the directory listing missed trailing slashes in directory 
    link names.
  - Finished the XML example parser class: XMLExampleHandler.
    Call {DOCUMENT_ROOT}/tests_and_examples/xml_example_parser/ from
    your browser to run it.

[2013-04-02]
  - The DefaultPostDataWrapper didn't remove the trailing bytes CRLF
    from each multipart item; this was fixed and the XMLExampleHandler
    now counts the correct number of data bytes.

[2013-03-25]
  - Header field 'Content-Disposition' declared in 
    ikrs.httpd.HTTPHeaderLine.
  - Fixed an EOI issue (0 read bytes replaces by -1, EOI) in 
    CustomUtil.transfer(...).
  - Built advanced HeaderParam methods 
    (again: the ikrs.httpd.datatypes.* classes are still experimental!).
  - Built basic POST data handling for the XMLExampleHandler class.
    See source for details (not yet finished).
    For a working basic test see 
    {DOCUMENT_ROOT}/tests_and_examples/xml_example_parser. A detailed
    description is located in the source code of the 'example.xmlparser'
    file.
  - Fixed: the FormDataItem extended KeyValueStringPair, but it may also 
    contain binary data! Each FormDataItem has a header part and an 
    internal input stream to read from.
  - The FormData had no method(s) to retrieve the key set; as it 
    consists of a sequence of form data items which has a header set
    each such a method is not required any more.
  - Fixed a white-space issue in the HeaderParam parser; all values are
    trimmed now and do not contain any trailing or leading white space
    characters any more.
   

[2013-03-22]
  - The ikrs.httpd.datatype.HeaderParams is now a three-dimensional 
    structure recocnizing the delimiter chars [ ',', ';', '=' ] in 
    exactly this order; the structure was two-dimensional before.
  - The ikrs.httpd.datatype.FormData and all its implementing classes
    were slightly changed.
  - The QueryFormDataDelegation class is now deprecated and MUST NOT be
    used any more.

    Note that the whole ikrs.httpd.datatype package is highly 
    experimental and should be used for test cases only!
    Actually it does not cover RFC 2616 at all as it contains very basic	
    MIME tools and some very simple parser classes.

[2013-03-18]
  - Fixed an issue with the directory listing: 
    the DefaultDirectoryResource did not add the trailing slash '/'
    after directory hyperlinks, so HTTP forms might have sent their
    GET/POST data into the wrong directory (the parent).

[2013-03-15]
 - The ikrs.http.HTTPHeaderLine is someting like a key-value-pair. It 
   extends ikrs.util.KeyValueStringPair now.

[2013-03-13]
 - Yucca's command line currently did not allow additional white space
   chars between command name and additional arguments.
   Actually it was a regex problem in the 
   ikrs.util.AbstractCommandFactory.parse(...) method.
 - Renamed ikrs.http.LocalCommand to ModuleCommand, and also it's 
   factory.

[2013-03-09]
  - Yucca command LOGLEVEL built.
  - Yucca command VERSION built.
  - Commandline documentation written (see README).

[2013-03-06]
  - Added the response-characterSet (name) to the ResourceMetaData.
  - Slighty modified the custom_directory_handler example by adding
    the {DOCUMENT_ROOT} to the output; also added some fancy text-box
    stuff ;) ... actually this is the most important example class as it
    shows how to extend the HTTP handler without modifying the handler
    code!

[2013-03-04]
  - The STATUS command prints the current system configuration; there is
    a fix that hides sensitive data such das keystore and truststore 
    passwords (otherwise the passwords would be written into the log 
    files!).

[2013-03-02]
  - Solved the logger's log level issue; both (yucca and ikrs.http) now
    use the same logger instance.
  - Changed YuccaCommadFactory's command lookup order: parent commands 
    first, subclasses' commands later (makes it possible to override 
    existing commands).

[2013-03-01]
  - Enhanced the ikrs.util.session.Default- and AbstractSessionManager
    by adding a new (optional) 'threadSafe' param; this allows to tell
    the manager to use synchronized session maps and so does the 
    HTTPHandler now.

[2013-02-28]
  - Status code for 'Content-Range' responses changed to 206 (Partial
    Content); was still 200 before.
  - Added the ParametrizedHTTPException class to the ResponseBuilder
    to allow to override the status code/reason phrase at the error
    response builder.
  - The ResouceMetaData now has a last-modified field.
  - There is now an ETag class (Entity tag) and the OK response already
    uses the generated ETag hash for response headers.
  - The HTTPHandler now has a global DateFormat that should be used to
    generate Date strings in the required HTTP Date format; for details
    see http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html section
    14.18.
  - The '206 Partial Content' response now contains the 'Date' and 
    'Expires' headers (sent if the 'Content-Range' request is present).
  - Solved an issue with the DefaultDirectoryResource; in some cases an
    additional slash ('/') was added between the path base and the 
    linked file(s).

[2013-02-27]
  - EOI bug in ikrs.httpd.ReadLimitInputStream solved [reaching the 
    virtual end did not necesarily cause -1 but 0 (zero) to be 
    returned which caused an infinite loop in the RangedRessource's
    inputstream read procedures].
  - Implemented the ikrs.httpd.resource.ResourceDelegation and
    RangedResource class.
  - Implemented download recovery (from a specified byte position):
    the server now recognizes the 'Content-Range' header; some new
    classes were added and still need some testing, but the 
    ikrs.httpd.resource.RangedResource seems to work fine so far.

[2013-02-25]
  - Bug fixed in the ProcessableResource class: the open(boolean) method
    blocked on the system process's internal output buffer overflow.
    The output data is now being read while (!) the process is running.
  - New test script added to check if binary data (such as images) is
    correctly handled and displayed:
      {DOCUMENT_ROOT}/tests_and_examples/binary_test/load_image.php
  - The 'Status' header handled by the PHPHandler now applies the 
    original HTTP version number (from the request headers; was always
    1.1 before).
  - The PHPHandler now applies the generated MIME type (from the PHP 
    output headers) to the resource's meta data (default was text/plain
    before).

[2013-02-05]
  - Yucca's ConnectionHandler (TCP) instance can now be re-used (shared 
    instance). 
    This is set by the flag 'sharedHandlerInstance' in the server.xml 
    file.
  - The the directory list generator still used an old (not valid any 
    more) favicon URL. This is fixed.
  - Cleaned up document_root directory: moved all tests into the sub 
    directory 'tests_and_examples'.
  - Replaced the HTTPHandler internal maps by threadsafe versions (from 
    java's util.Collections class).!

[2013-01-23]
  - The AbstractPreparedResponse now uses the same HTTP version such as 
    in the request headers.
    This causes the server to send HTTP/1.0 replies when the request is 
    version 1.0 (response was always sent in version 1.1 before).
  - The default error document map is now configurable by 
    {USER_HOME}/.yuccasrv/ikrs.httpd.conf#ERROR_DOCUMENT.{STATUS_CODE}

[2013-01-16]
  - The HypertextAccessFile class now recoginzes the 'ErrorDocument' directive.
    Additionally the HTTP handler has a new method 'getDefaultErrorDocumentURI(...)'].
    Result: htaccess' ErrorDocument directives are now handled (though the default error document
    	    set is not yet configurable).

[2013-01-10]
 - The CGI PATH_INFO and PATH_TRANSLATED were fixed (contained corrupt values before).
 - The CGI environment vars DOCUMENT_ROOT, SCRIPT_FILENAME and REQUEST_URI were added.
 - The internal session now stores REMOTE_ADDRESS, REMOTE_PORT, REMOTE_HOST and also LOCAL_ADDRESS,
   LOCAL_HOST and LOCAL_PORT.
 - The ikrs.httpd.filehandler.CGIHandler class has an internal HEADER-to-CGI set now that explicitly
   allows a specified subset of HTTP headers to be mapped into the GCI environment.
   By default these headers are:
      - Accept
      - Accept-Charset
      - Accept-Encoding
      - Accept-Language
      - Connection
      - Cookie
      - Host
      - Referer
      - User-Agent
 - Tested the server with Drupal-7.15. Worked after some drupal configuration issues :)

[2013-01-09]
 - Additional error documents added.
 - Reqest-Line parser method fixed (for the case a fully malformed non-HTTP request was sent).
 - The session timeout is now configurable using the ikrs.http.conf file:
   {USER_HOME}/.yuccasrv/ikrs.httpd.conf#SESSION_TIMEOUT
 - The yucca command line is now extensible and capable to handle advanced httpd commands (none exist
   so far, though).

[2013-01-08]
 - There was an issue with the session timeout handling and it is fixed now. 
   Additionally I created a nested SESSION.INTERNAL child now holding the environment data used in the
   CGIHandler.
 - The file handler example now also shows how to use sessions.

[2013-01-07]
 - Some tests with customized file/directory handlers (htaccess, SetHandler) exposed a flaw inside the
   system when using non-existing files; it is now possible to configure FileHandlers by overriding the
   'requiresExistingFile()' method and mock the ResourceAccessor this way: you can now also pass virtual 
   URIs, if you want to.
   See the ikrs.httpd.filehandlers.IkarosExampleHandler for details.
   A configured example directory is located in the default document root at /custom_directory_handler.
   Try to request a non existing file or sub directory, such like /custom_directory_handler/not_existing
   and see what happens :)
 - The hexdump output of requested files during transfer is now restricted up to 10KB of data. Then the 
   output on stdout stops to avoid flooding.
 - ikrs.httpd.HTTPHeaders.read(...) still wrote debug data into stdout. The debug/info output was moved
   to ikrs.httpd.HTTPRequestDistributor.run().

[2013-01-05]
 - Built a nested htaccess evaluation. Before the application just searched up the document
   tree until the first htaccess file was found. That was not correct! *All* htaccess files
   upon the request path are now used; later occurrences override earlier stated stettings.
   The HyptertextAccessFile class got a new merge() method for this purpose.

[2013-01-03]
  - Moved ikrs.httpd.datatype.KeyValueStringPair to ikrs.util.KeyValueStringPair.
  - Added a handler method in HTTPHandler.rejectedExecution(...) that processes rejected requests fast and 
    painless.
  - SSL default example configuration issue solved.

[2012-12-21]
  - There is a new utility class: ikrs.io.fileio.FileCopy that can be used to copy files and nested directories.
  - The yucca main class now has a first-run-check. If the configuration files or -directory do not exist the 
    program performs a short survey and creates the config files and -directories automatically.
    The templates are located in '_.templates/'.

[2012-12-17]
  - File paths may now contain the '{USER_HOME}' placeholder (for yucca and httpd).
  - Renamed document root 'document_root_alpha' to 'document_root'.
  - Moved configuration directory '_.yucca/' to '{USER_HOME}/.yucca/'.
  - Buy in method ikrs.httpd.CustomUtil.classImplementsInterface(...) fixed.
  - DOCUMENT_ROOT is now configurable via .yuccasrv/ikrs.httpd.config#DOCUMENT_ROOT.
  - New yucca-commands: 'warranty' and 'license'.

[2012-12-16]
  - Changed package name from 'ikrs.http' to 'ikrs.httpd'.

[2012-12-15]
  - HTTP methods can now be disabled: ikrs.httpd.conf#DISABLE_METHOD.{METHOD} = { On | Off }

[2012-12-12]
  - INI-file parser added.

[2012-12-11]
  - The ConnectionHandler has a customizable init() method now which applies the whole yucca server config
    into the handler.
  - The yucca config has an (optional) 'httpConfig' node now that can be used to define additional custom
    config settings to the HTTP handler.
  - The filehandlers config is now defined inside the yucca config: httpConfig-node.

[2012-12-05]
  - The HTTP server is now capable to process HTTP TRACE.

[2012-12-04]
  - The HTTP server is now capable to process HTTP OPTIONS and HTTP HEAD.


[2012-11-27]
  - The socket manager ikrs.yuccasrv.socketmngr.BindManager supports secure sockets using SSL/TLS now.

[2012-10-29]
  - The 'filehandlers.ini' is now interpreted and contains the file handler mappings.
  - The htaccess 'SetHandler' and 'AddHandler' directives work now.

[2012-10-15]
  - HTTP POST works now with PHP (actually it was an unsolved php-cgi issue).

[2012-10-12]	
  - Replaced HTTPHandler.getServerName() by HTTPHandler.getSoftwareName() to avoid CGI name clash.
  - Built new HTTPHandler.getServerName() method.
  - Built basic CGI/1.1 handler.
  - Finished PHP POST handling.


[2012-10-11]
  - htaccess 'DirectoryIndex' implemented.
  - htaccess 'Options {+|-}Indexes' implemented.
  - New class for directory listings: ikrs.http.resource.DefaultDirectoryResource.
 
[2012-10-10]
  - The error response 405 (Method Not Allowed) now includes the 'Allow' header.

[2012-09-30]
  - Bug fixed: server sent an '200 OK' even if the external file handler reported runtime errors.

[2012-09-15]
  - htaccess 'Digest' authorization implemented (not apache compatible: ikrs.http.MD5).

[2012-09-03]
  - htaccess 'Basic' authorizaion implemented (apache compatible).





The java documentation files are located at ./document_root/docs/ or
if 'document_root' is your configured {DOCUMENT_ROOT} (the default value)
the files will be available at http://127.0.0.1:8888/docs/.








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
    ~~~~~~
    Prints the current status of the HTTPd module.

  - HEXDUMP FORMAT <format>
    ~~~~~~~~~~~~~~~~~~~~~~~
    Changes the hexdump output column format. The expected value must
    be a comma separated integer list, each integer indicating the 
    respective column list and the number of integers the total number
    of columns.
    Example: the format '4,4,0,4,4' would generate a hexdump format
    	     like this:

                                                    
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


   
