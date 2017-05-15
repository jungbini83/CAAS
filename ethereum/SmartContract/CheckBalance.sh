rootDir=$1
shift
nn=$1
nodeID=$(printf "%02d" $nn)

# 1. Getting Master's contract address and abi
address=$(cat ~/$rootDir/data/master/contract/contractAddr)
abi=$(cat ~/$rootDir/data/master/contract/abi)

# 2. Registering contract address with abi
cmd='eth.contract('${abi}').at('${address}').coinBalanceOf(eth.accounts[0])'
echo $(geth --exec ${cmd} attach http://localhost:85${nodeID})