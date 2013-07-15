#!/bin/sh
#
# @author Ikaros Kappler
# @date 2012-05-01
# @modified 2012-12-19 Ikaros Kappler
# @modified 2013-05-15 Ikaros Kappler (changed to Java7; no Xlint messages requesting).
# @version 1.0.0


# Check file argument?
# if [ "$1" = "" ]; then
#    echo "Please pass the input file"
#    exit 1
# fi


# Compile the utils
echo "Compile ikrs.util ..."
javac -Xlint:none ikrs/util/*.java ikrs/util/session/*.java
ec="$?"
if [ "$ec" -ne "0" ]; then
    echo "Sorry, failed to compile ikrs.util. Exit code $ec"
    exit 1
fi


echo "Compile ikrs.typesystem ..."
javac -Xlint:none ikrs/typesystem/*.java
ec="$?"
if [ "$ec" -ne "0" ]; then
    echo "Sorry, failed to compile ikrs.typesystem. Exit code $ec"
    exit 1
fi


echo "Compile ikrs.io ..."
javac -Xlint:none ikrs/io/*.java ikrs/io/fileio/*.java ikrs/io/fileio/htaccess/*.java ikrs/io/fileio/inifile/*.java
ec="$?"
if [ "$ec" -ne "0" ]; then
    echo "Sorry, failed to compile ikrs.io. Exit code $ec"
    exit 1
fi


echo "Compile ikrs.yucca ..."
javac -Xlint:none ikrs/yuccasrv/*.java ikrs/yuccasrv/ui/*.java ikrs/yuccasrv/commandline/*.java ikrs/yuccasrv/socketmngr/*.java
ec="$?"
if [ "$ec" -ne "0" ]; then
    echo "Sorry, failed to compile ikrs.yucca. Exit code $ec"
    exit 1
fi


echo "Compile ikrs.httpd ..."
javac -Xlint:none ikrs/httpd/*.java ikrs/httpd/response/successful/*.java ikrs/httpd/response/*.java ikrs/httpd/resource/*.java ikrs/httpd/filehandler/*.java ikrs/httpd/datatype/*.java
ec="$?"
if [ "$ec" -ne "0" ]; then
    echo "Sorry, failed to compile ikrs.httpd. Exit code $ec"
    exit 1
fi


echo "Compile XMLExampleHandler ..."
javac -Xlint:none XMLExampleHandler.java
ec="$?"
if [ "$ec" -ne "0" ]; then
    echo "Sorry, failed to compile XMLExampleHandler. Exit code $ec"
    exit 1
fi


echo "Compile JSONRPCExampleHandler ..."
javac -Xlint:none JSONRPCExampleHandler.java
ec="$?"
if [ "$ec" -ne "0" ]; then
    echo "Sorry, failed to compile JSONRPCExampleHandler. Exit code $ec"
    exit 1
fi

echo "Done."


# This is the old compiler command: compiling *all* in a bunch
# javac -Xlint:unchecked ikrs/yuccasrv/*.java ikrs/yuccasrv/ui/*.java ikrs/util/*.java ikrs/yuccasrv/commandline/*.java ikrs/yuccasrv/socketmngr/*.java ikrs/httpd/*.java ikrs/httpd/response/successful/*.java ikrs/httpd/response/*.java ikrs/httpd/resource/*.java ikrs/util/session/*.java ikrs/typesystem/*.java ikrs/io/fileio/htaccess/*.java ikrs/io/fileio/inifile/*.java ikrs/httpd/filehandler/*.java ikrs/io/*.java ikrs/io/fileio/*.java ikrs/httpd/datatype/*.java



