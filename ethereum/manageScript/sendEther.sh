#!/bin/bash

# sendEther.sh <rootDir> <sendNode> <receiveNode> <ether>
# <rootDir>: node's root directory
# <receiveNode>: node that receive ether from <sendNode> node
# <ether>: amount of ether
if [ "$#" -eq 3 ]; then

	if [ -f ../"root.cfg" ]; then
		root=$(cat ../root.cfg)
	else 
		echo 'root.cfg file is not exist. Then you can assigne root directory manually.'
		echo 'USAGE: sendEther.sh <rootDir> <sendNode> <receiveNode> <ether>'
		exit 1
	fi

elif [ "$#" -eq 4 ]; then
	root=$1
	shift
else
	echo 'USAGE: sendEther.sh [rootDir] <sendNode> <receiveNode> <ether>'
	exit 1
fi

sn=$1
if [ $sn == "master" ]; then
	snd='master'
	rpcport=9430
else
	snd=$(printf "%02d" $sn)
	rpcport=85$snd
fi
shift
rn=$1
if [ $rn == "master" ]; then
	rnd='master'	
else
	rnd=$(printf "%02d" $rn)
fi
shift
ether=$1

# 1. read <sendNode> account
snAccount=$(cat ~/$root/data/$snd/account)

# 2. read <receiveNode> account
rnAccount=$(cat ~/$root/data/$rnd/account)

# 3. unlock account
echo -n '1. Unlock account of node('$snd') result: '
./unlockAccount.sh $root $snd

# 4. trans ether from <sendNode> to <receiveNode>
echo '2. Sending '$ether' ether from node('$snd') to node('$rnd')'
tx="{from:\"$snAccount\", to:\"$rnAccount\", value: web3.toWei($ether, 'ether')}"
echo -n '3. Transaction result: '
geth --exec "eth.sendTransaction(${tx})" attach http://localhost:$rpcport

# 4. Check ether of receive node, but the transaction result is not applied directly cause POW
echo -n "4. Node('$rnd')'s balance: "
./checkEther.sh $root $rnd