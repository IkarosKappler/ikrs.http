ikrs.http
=========

A free tiny java written http server.




Changes
=======

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
