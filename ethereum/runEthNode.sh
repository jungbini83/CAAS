#!/bin/bash

# runEthNode.sh <root dir> <node id>
# <root dir>: root directory where the log, chain, address, enode are saved
# <node id>: node id written by 2 digit number format like 01, 02, 03...
if [ "$#" -eq 1 ]; then

	if [ -f ./"root.cfg" ]; then
		root=$(cat ./root.cfg)		
	else
		echo 'root.cfg file is not exist. Then you can assigne root directory manually.'
		echo 'USAGE: runEthNode.sh <rootDir> <node id>'
		exit 1
	fi

elif [ "$#" -eq 2 ]; then
	root=$1
	shift	
else
	echo 'USAGE: runEthNode.sh [rootDir] <node id>'
	exit 1
fi

nn=$1
dd=$(printf "%02d" $nn)		# change node number to node ID like 01, 02, 03...

# 1. make ethereum node account
accountAddr=$(geth --datadir ~/$root/data/$dd --password <(echo -n $dd) account new)
echo "Node#"$dd "'s account address: "${accountAddr:10:40}
echo ${accountAddr:10:40} >> ~/$root/data/$dd/account 	# save account address

# 2. running ethereum node demon
geth --datadir ~/$root/data/$dd init ~/$root/genesis.json
nohup geth --datadir ~/$root/data/$dd --rpc --rpcport "85${dd}" --rpcapi "admin,eth,miner,personal" \
--port "30${dd}" --unlock=${accountAddr:10:40} --password=<(echo -n $dd) --nodiscover > ~/$root/data/$dd/log 2>&1 &

# 3. save this node's enode address
geth --exec 'admin.nodeInfo.enode' attach http://localhost:85$dd > ~/$root/data/$dd/enode