#!/bin/bash

# removePeer.sh <rootDir> <srcNode> <peerNode>
# <rootDir>: root directory of nodes
# <srcNode>: the node that want to remove peer Node
# <peerNode>: the peer node that will be removed from <srcNode>
if [ "$#" -eq 2 ]; then

	if [ -f ../"root.cfg" ]; then
		root=$(cat ../root.cfg)
	else 
		echo 'root.cfg file is not exist. Then you can assigne root directory manually.'
		echo 'USAGE: removePeer.sh <rootDir> <srcNode> <peerNode>'
		exit 1
	fi

elif [ "$#" -eq 3 ]; then
	root=$1
	shift
else
	echo 'USAGE: removePeer.sh [rootDir] <srcNode> <peerNode>'
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
shift
pn=$1
if [ $pn == 'master' ]; then
	pnd='master'
else
	pnd=$(printf "%02d" $pn)		# change peer node number to node ID like 01, 02, 03...
fi

# 1. read enode of peer node
snEnode=$(cat ~/${root}/data/${pnd}/enode)

# 2. remove peer node from source node
echo -n 'Removing node('$pnd') to node('$snd') result: '
geth --exec "admin.removePeer($snEnode)" attach http://localhost:$rpcport