#!/bin/bash

# lockAccount.sh <rootDit> <node id>
# <rootDir>: node's root directory
# <node id>: node id you want to lock its address 
if [ "$#" -ne 2 ]; then
	echo 'USAGE: lockAccount.sh <rootDit> <node id>'
	exit 1
fi

root=$1
shift
nid=$1
if [ $nid == 'master' ]; then
	nidd='master'
	rpcport=8540
else
	nidd=$(printf "%02d" $nid)
	rpcport=85$nidd

# 1. read account address of node #nidd
addr=$(cat ~/${root}/data/${nidd}/account)
echo $addr

geth --exec "personal.lockAccount('${addr}')" attach http://localhost:$rpcport