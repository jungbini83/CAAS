#!/bin/bash

root=$1
m_addr=$(cat ~/${root}/data/master/account)
echo $m_addr

genesisContent='{
	"alloc": "'
genesisContent=$genesisContent$m_addr
genesisContent=$genesisContent'"
			balance": "5000000000000000000000000000"
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