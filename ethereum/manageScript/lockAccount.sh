#!/bin/bash

# lockAccount.sh <rootDit> <node id>
# <rootDir>: node's root directory
# <node id>: node id you want to lock its address 
if [ "$#" -eq 1 ]; then

	if [ -f ../"root.cfg" ]; then
		root=$(cat ../root.cfg)
	else 
		echo 'root.cfg file is not exist. Then you can assigne root directory manually.'
		echo 'USAGE: lockAccount.sh <rootDit> <node id>'
		exit 1
	fi

elif [ "$#" -eq 2 ]; then
	root=$1
	shift
else
	echo 'USAGE: lockAccount.sh [rootDit] <node id>'
	exit 1
fi

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