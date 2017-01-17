#!/bin/bash

# removePeer.sh <rootDir> <srcNode>
# <rootDir>: root directory of nodes
# <srcNode>: the node that want to check peer list
root=$1
shift
sn=$1
snd=$(printf "%02d" $sn)		# change source node number to node ID like 01, 02, 03...

# 1. check peer list 
geth --exec "admin.peers" attach http://localhost:85$snd