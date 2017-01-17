#!/bin/bash

# connectPeer.sh <rootDir> <srcNode> <peerNode>
# <rootDir>: root directory of nodes
# <srcNode>: the node that want to connect targetNode
# <peerNode>: the peer node that will be connected to <srcNode>
# <srcNode> regist <peerNode> as a kind of its peer nodes
root=$1
shift
sn=$1
snd=$(printf "%02d" $sn)		# change source node number to node ID like 01, 02, 03...
shift
pn=$1
pnd=$(printf "%02d" $pn)		# change peer node number to node ID like 01, 02, 03...

# 1. read enode of peer node
snEnode=$(cat ~/${root}/data/${pnd}/enode)

# 2. add peer node to source node
geth --exec "admin.addPeer($snEnode)" attach http://localhost:85$snd