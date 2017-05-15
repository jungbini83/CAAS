#!/bin/bash

dataDir=$1

if [ -d ~/"$dataDir/data/master/contract" ]; then
	echo $root 'direcotry already existing.'
	echo 'Therefore, remove it before running master node.'
	rm -r ~/$dataDir/data/master/contract
fi

mkdir ~/$dataDir/data/master/contract

# 1. Running deploy script
geth --exec 'loadScript("../ethereum/SmartContract/SmartRewardScript.js")' attach http://localhost:9430 > ~/$dataDir/data/master/contract/transactionHash

# 2. Getting contract source's abi
solc ../ethereum/SmartContract/SmartReward.sol --abi > SmartReward.abi
echo $(sed -n '4p' SmartReward.abi) > ~/$dataDir/data/master/contract/abi

sleep 3

# 3. Getting deployed contract address
transactionHash=$(cat ~/$dataDir/data/master/contract/transactionHash)
transactionHash='"'${transactionHash:0:66}'"'

geth --exec 'eth.getTransactionReceipt('$transactionHash').contractAddress' attach http://localhost:9430 > ~/$dataDir/data/master/contract/contractAddr
echo 'Contract Address: '$(cat ~/$dataDir/data/master/contract/contractAddr)