dataDir=$1

transactionHash=$(cat ~/$dataDir/data/master/contract/transaction.hash)

curl --data '{"jsonrpc":"2.0", "method":"eth_getTransactionReceipt", "params": ["'${transactionHash:0:66}'"], "id": 1}' localhost:9430 > ~/$dataDir/data/master/contract/contract.addr