#!/bin/bash

# runMasterNode.sh <root dir>
# <root dir>: root directory where the log, chain, address, enode are saved
root=$1
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
./genGenesis.sh	$root

# 3. running master node demon
geth --datadir ~/$root/data/master init ~/$root/genesis.json
nohup geth --datadir ~/$root/data/master --rpc --rpcport "8540" --rpcapi "admin,db,eth,debug,miner,net,shh,txpool,personal,web3" \
--port 3030 --unlock=${accountAddr:10:40} --password=<(echo -n master) --nodiscover --mine > ~/$root/data/master/log 2>&1 &

# 4. save master node's enode address
geth --exec 'admin.nodeInfo.enode' attach http://localhost:8540 > ~/$root/data/master/enode