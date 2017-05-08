dataDir=$1

mkdir ~/$dataDir/data/master/contract

geth --exec 'loadScript("../ethereum/SmartContract/SmartRewardScript.js")' attach http://localhost:9430
geth --exec 'loadScript("../ethereum/SmartContract/FindContractAddress.js")' attach http://localhost:9430

#geth --exec 'loadScript("../ethereum/SmartContract/SmartRewardScript.js")' attach http://localhost:9430 > ~/$dataDir/data/master/contract/transactionHash