#!/bin/bash

# checkPeer.sh <rootDir> <srcNode>
# <rootDir>: root directory of nodes
# <srcNode>: the node that want to check peer list
if [ "$#" -eq 1 ]; then

	if [ -f ../"root.cfg" ]; then
		root=$(cat ../root.cfg)
	else 
		echo 'root.cfg file is not exist. Then you can assigne root directory manually.'
		echo 'USAGE: checkPeer.sh <rootDir> <srcNode>'
		exit 1
	fi

elif [ "$#" -eq 2 ]; then
	root=$1
	shift
else
	echo 'USAGE: checkPeer.sh [rootDir] <srcNode>'
	exit 1
fi

sn=$1
if [ $sn == 'master' ]; then
	snd='master'
	rpcport=8540
else
	snd=$(printf "%02d" $sn)		# change source node number to node ID like 01, 02, 03...
	rpcport=85$snd
fi

# 1. check peer list 
geth --exec "admin.peers" attach http://localhost:$rpcport