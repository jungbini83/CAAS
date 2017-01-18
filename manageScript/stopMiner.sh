#!/bin/bash

# stopMiner.sh <rootDir> <Node id>
# <rootDir>: node's root directory
# <Node id>: node id to stop miner
if [ "$#" -eq 1 ]; then

	if [ -f ../"root.cfg" ]; then
		root=$(cat ../root.cfg)
	else 
		echo 'root.cfg file is not exist. Then you can assigne root directory manually.'
		echo 'USAGE: stopMiner.sh <rootDir> <Node id>'
		exit 1
	fi

elif [ "$#" -eq 2 ]; then
	root=$1
	shift
else
	echo 'USAGE: stopMiner.sh [rootDir] <Node id>'
	exit 1
fi

cn=$1
if [ $cn == 'master' ]; then
	cnd='master'
	rpcport=8540
else
	cnd=$(printf "%02d" $cn)		# convert 2 digit format
	rpcport=85$cnd
fi

# 1. stop miner of node $cnd (default number of thread=1)
echo -n "1. Stop miner result: "
geth --exec "miner.stop()" attach http://localhost:$rpcport
echo -n "2. Mining status: "
geth --exec "eth.mining" attach http://localhost:$rpcport