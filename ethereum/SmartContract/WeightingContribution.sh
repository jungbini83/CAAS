#!/bin/bash

rootDir=$1
shift
nodeID=$(printf "%02d" $1)
shift
deviceName=$(printf "%d" $1)
shift
monitoringType=$(printf "%d" $1)
shift
resolution=$(printf "%d" $1)
shift
recordTime=$(printf "%d" $1)
shift
distance=$(printf "%d" $1)
shift
sourceAddr=$1

# 1. Getting Master's contract address and abi
address=$(cat ~/$rootDir/data/master/contract/contractAddr)
address=${address:1:42}
abi=$(cat ~/$rootDir/data/master/contract/abi)
nodeAddr=$(cat ~/$rootDir/data/$nodeID/account)

# 2. Sending some ethers to client node
cmd='loadScript("../ethereum/SmartContract/WeightingContribution.js");weightingContribution('${abi}',"'${address}'","0x'${nodeAddr}'",'${deviceName}','${monitoringType}','${resolution}','${recordTime}','${distance}',"'${sourceAddr}'");'
geth --exec ${cmd} attach http://localhost:9430