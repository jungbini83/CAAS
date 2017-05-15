#!/bin/bash

rootDir=$1
shift
nodeID=$(printf "%02d" $1)
shift
reward=$1

masterAddr=$(cat ~/$rootDir/data/master/account)
nodeAddr=$(cat ~/$rootDir/data/$nodeID/account)

# 2. Sending some ethers to client node
tx="{from:\"0x$masterAddr\", to:\"0x$nodeAddr\", value: web3.toWei($reward, 'ether')}"
geth --exec "eth.sendTransaction(${tx})" attach http://localhost:9430