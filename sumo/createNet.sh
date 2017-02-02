#!/bin/bash

# createNet.sh <type> <options>
# <type>: grid, spider, random

if [ "$#" -eq 1 ]; then
	netType=$1
	shift

	if [ $netType == "Grid" ]; then
		netoption="--grid --grid.number=5 --grid.length=100"		
	elif [ $netType == "Spider" ]; then
		netoption="--spider --arms=5 --circles=3"
	elif [ $netType == "Random" ]; then
		netoption="--rand true --rand.iterations=30"
	fi

elif [ "$#" > 1 ]; then
	netType=$1
	shift

	if [ $netType == "Grid" ]; then
		netoption="--grid $*"		
	elif [ $netType == "Spider" ]; then
		netoption="--spider $*"
	elif [ $netType == "Random" ]; then
		netoption="--rand $*"
	fi
else
	"USAGE: runMasterNode.sh <rootDir>"
	exit 1
fi



mapDir="map/"$netType
if [ ! -d $mapDir ]; then
	mkdir -p $mapDir
fi

netcmd="netgenerate "${netoption}" --output-file=./map/${netType}/${netType}.net.xml"

echo -n 'Create '$netType 'network xml file result: '
$netcmd