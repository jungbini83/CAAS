#!/bin/bash

# removePeer.sh <rootDir> <srcNode> <peerNode>
# <rootDir>: root directory of nodes
# <srcNode>: the node that want to remove peer Node
# <peerNode>: the peer node that will be removed from <srcNode>
root=$1
shift
sn=$1
snd=$(printf "%02d" $sn)		# change source node number to node ID like 01, 02, 03...
shift
pn=$1
pnd=$(printf "%02d" $pn)		# change peer node number to node ID like 01, 02, 03...

# 1. read enode of peer node
snEnode=$(cat ~/${root}/data/${pnd}/enode)

# 2. remove peer node from source node
geth --exec "admin.removePeer($snEnode)" attach http://localhost:85$snd