#!/bin/sh

# This would be the testing mode with existing (!) configuration directories.
# java ikrs.yuccasrv.Yucca --config {USER_HOME}/.yuccasrv/server.xml -c -l FINEST

# Start yucca through httpd.Run (this allows modifications to the command line)
java ikrs.httpd.Run --config {USER_HOME}/.yuccasrv/server.xml -c -l FINEST

# This is the real command to run ikrs.yucca/ikrs.httpd
# java ikrs.yuccasrv.Yucca --config {USER_HOME}/.yuccasrv/server.xml -c -l FINEST

