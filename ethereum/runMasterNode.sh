#!/bin/bash

# runMasterNode.sh <root dir>
# <root dir>: root directory where the log, chain, address, enode are saved
if [ "$#" -eq 1 ]; then
	root=$1
else
	"USAGE: runMasterNode.sh <rootDir>"
	exit 1
fi

echo $root > root.cfg			# save root directory information

# 0. clean existing ethereum data files
if [ -d ~/"$root" ]; then
	echo $root 'direcotry already existing.'
	echo 'Therefore, remove it before running master node.'
	rm -r ~/$root/
fi

# 1. make master's account
accountAddr=$(geth --datadir ~/$root/data/master --password <(echo -n master) account new)
echo "Master's account address: "${accountAddr:10:40}
echo ${accountAddr:10:40} >> ~/$root/data/master/account 	# save account address

# 2. make genesis.json file including master's account
genesisContent='{
	"alloc": {
		"'
genesisContent=$genesisContent${accountAddr:10:40}
genesisContent=$genesisContent'": {
			"balance": "5000000000000000000000000000"			
		}
	},

	"nonce": "0x0000000000000000",
	"difficulty": "0x010000",
	"mixhash": "0x0000000000000000000000000000000000000000000000000000000000000000",
	"coinbase": "0x0000000000000000000000000000000000000000",
	"timestamp": "0x00",
	"parentHash": "0x0000000000000000000000000000000000000000000000000000000000000000",
	"extraData": "0x",
	"gasLimit": "0x1000000000"
}'

echo "$genesisContent" >> ~/${root}/genesis.json

# 3. running master node demon
geth --datadir ~/$root/data/master init ~/$root/genesis.json
nohup geth --datadir ~/$root/data/master --rpc --rpcport "9430" --rpcapi "admin,eth,miner,personal" \
--port 4430 --unlock=${accountAddr:10:40} --password=<(echo -n master) --nodiscover --mine > ~/$root/data/master/log 2>&1 &

# 4. save master node's enode address
geth --exec 'admin.nodeInfo.enode' attach http://localhost:9430 > ~/$root/data/master/enode
enodeAddr=$(cat ~/$root/data/master/enode)
echo "Master's enode address: "$enodeAddr
