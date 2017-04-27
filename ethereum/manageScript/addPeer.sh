#!/bin/bash

# addPeer.sh <rootDir> <srcNode> <peerNode>
# <rootDir>: root directory of nodes
# <srcNode>: the node that want to connect targetNode
# <peerNode>: the peer node that will be connected to <srcNode>
if [ "$#" -eq 2 ]; then

	if [ -f ../"root.cfg" ]; then
		root=$(cat ../root.cfg)
	else
		echo 'root.cfg file is not exist. Then you can assigne root directory manually.'
		echo 'USAGE: addPeer.sh <rootDir> <srcNode> <peerNode>'
		exit 1
	fi

elif [ "$#" -eq 3 ]; then
	root=$1	
	shift
else
	echo 'USAGE: addPeer.sh [rootDir] <srcNode> <peerNode>'
	exit 1
fi

sn=$1
if [ $sn == 'master' ]; then
	snd='master'
	rpcport=9430
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
pnEnode=$(cat ~/${root}/data/${pnd}/enode)
pnEnode='admin.addPeer('$pnEnode')'

# 2. add peer node to source node
echo -n 'Adding node('$pnd') to node('$snd') result: '
geth --exec $pnEnode attach http://localhost:$rpcport