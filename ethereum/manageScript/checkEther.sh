#!/bin/bash

# checkEther.sh <rootDir> <chkNode>
# <rootDir>: node's root directory
# <chkNode>: node that receive ether from <sendNode> node
if [ "$#" -eq 1 ]; then

	if [ -f ../"root.cfg" ]; then
		root=$(cat ../root.cfg)
	else 
		echo 'root.cfg file is not exist. Then you can assigne root directory manually.'
		echo 'USAGE: checkEther.sh <rootDir> <chkNode>'
		exit 1
	fi

elif [ "$#" -eq 2 ]; then
	root=$1
	shift
else
	echo 'USAGE: checkEther.sh [rootDir] <chkNode>'
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

# 1. read <checkNode> account
cnAccount=$(cat ~/$root/data/${cnd}/account)

# 2. check ether from <chkNode>'s account
geth --exec "web3.fromWei(eth.getBalance('${cnAccount}'), 'ether')" attach http://localhost:$rpcport