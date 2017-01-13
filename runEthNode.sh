#!/bin/bash

# runEthNode.sh <root dir> <node id> <connecting peer node id>
# <root dir>: root directory where the log, chain, address, enode are saved
# <node id>: node id written by 2 digit number format like 01, 02, 03...
# <connecting peer node id>: node id the node want to connect as peer
root=$1
shift
nn=$1
shift
cn=$1

# 1. make ethereum node account
dd=$(printf "%02d" $nn)		# change node number to node ID like 01, 02, 03...
accountAddr=$(geth --datadir ~/$root/data/$dd --password <(echo -n $dd) account new)
echo "Node#"$dd "'s account address: "${accountAddr:10:40}
echo ${accountAddr:10:40} >> ~/$root/data/$dd/account 	# save account address

# make static-nodes.json to connect this node to master's node
if [$cn -ne 'master']; then 		# if the cn variable is digit not 'master', 
	cd=$(printf "%02d" $cn)			# convert 2 digit format
else
	cd=$cn
fi

masterEnode=$(cat ~/${root}/data/$cd/enode)
fileContents='[
	'
fileContents=$fileContents$masterEnode
fileContents=$fileContents'
]'
echo $fileContents >> ~/$root/data/$dd/static-nodes.json

# 3. running ethereum node demon
geth --datadir ~/$root/data/$dd init ~/$root/genesis.json
nohup geth --datadir ~/$root/data/$dd --rpc --rpcport "85${dd}" --rpcapi "admin,db,eth,debug,miner,net,shh,txpool,personal,web3" \
--port "30${dd}" --unlock=${accountAddr:10:40} --password=<(echo -n $dd) --nodiscover &

# 4. save this node's enode address
geth --exec 'admin.nodeInfo.enode' attach http://localhost:85$dd > ~/$root/data/$dd/enode