#!/bin/bash

# addPeer.sh <rootDir> <srcNode> <peerNode>
# <rootDir>: root directory of nodes
# <srcNode>: the node that want to connect targetNode
# <peerNode>: the peer node that will be connected to <srcNode>
# <srcNode> regist <peerNode> as a kind of its peer nodes
if [ "$#" -ne 3 ]; then
	echo 'USAGE: addPeer.sh <rootDir> <srcNode> <peerNode>'
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

# 2. add peer node to source node
geth --exec "admin.addPeer($pnEnode)" attach http://localhost:$rpcport