#!/bin/bash

# removePeer.sh <rootDir> <srcNode> <peerNode>
# <rootDir>: root directory of nodes
# <srcNode>: the node that want to remove peer Node
# <peerNode>: the peer node that will be removed from <srcNode>
if [ "$#" -ne 3 ]; then
	echo 'USAGE: removePeer.sh <rootDir> <srcNode> <peerNode>'
	exit 1
fi

root=$1
shift
sn=$1
if [ $sn == 'master' ]; then
	snd='master'
	rpcport=8540
else
	snd=$(printf "%02d" $sn)		# change source node number to node ID like 01, 02, 03...
	rpcport=85$snd
shift
pn=$1
if [ $pn == 'master' ]; then
	pnd='master'
else
	pnd=$(printf "%02d" $pn)		# change peer node number to node ID like 01, 02, 03...

# 1. read enode of peer node
snEnode=$(cat ~/${root}/data/${pnd}/enode)

# 2. remove peer node from source node
geth --exec "admin.removePeer($snEnode)" attach http://localhost:$rpcport