#!/bin/bash

rootDir=$1
shift
nodeID=$(printf "%02d" $1)
shift
index=$(printf "%02d" $1)

# 1. Getting Master's contract address and abi
address=$(cat ~/$rootDir/data/master/contract/contractAddr)
address=${address:1:42}
abi=$(cat ~/$rootDir/data/master/contract/abi)
nodeAddr=$(cat ~/$rootDir/data/$nodeID/account)

# 2. Sending some ethers to client node
cmd='loadScript("../ethereum/SmartContract/WeightingContribution.js");getWeight('${abi}',"'${address}'",'${index}');'
geth --exec ${cmd} attach http://localhost:9430