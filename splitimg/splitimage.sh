#!/bin/bash

# Check file argument
if [ "$1" = "" ]; then
    echo "Please pass the input file"
    exit 1
fi


# build images for zoom level 15
java splitimg.SplitImg -r 1 -c 2 -s 1/4 -t png -p Stadtplan_MUC3_neu_z\(15\)_q\(%x,%y\).png -d map_splits $1
ec="$?"
if [ "$ec" -ne "0" ]; then
    echo "Sorry, failed to generate map tiles for zoom 15. Exit code $ec"
    exit 1
fi


# build images for zoom level 16
java splitimg.SplitImg -r 2 -c 4 -s 1/2 -t png -p Stadtplan_MUC3_neu_z\(16\)_q\(%x,%y\).png -d map_splits $1
ec="$?"
if [ "$ec" -ne "0" ]; then
    echo "Sorry, failed to generate map tiles for zoom 16. Exit code $ec"
    exit 1
fi


# build images for zoom level 17
java splitimg.SplitImg -r 4 -c 8 -s 1/1 -t png -p Stadtplan_MUC3_neu_z\(17\)_q\(%x,%y\).png -d map_splits $1
ec="$?"
if [ "$ec" -ne "0" ]; then
    echo "Sorry, failed to generate map tiles for zoom 17. Exit code $ec"
    exit 1
fi


# Done
exit 0
