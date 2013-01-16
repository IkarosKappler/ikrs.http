ikrs.httpd
==========

A free tiny java written http server.




Changes
=======

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
