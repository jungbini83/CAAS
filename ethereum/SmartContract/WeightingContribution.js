// 1. setting default account
eth.defaultAccount = eth.coinbase;

var weightingContribution = function(abi, contractAddr, reporterAddr, deviceName, monitoringType, resolution, recordingTime, targetDistance, srcAddress) {
	contract = eth.contract(abi).at(contractAddr);	
	contract.detectCriminal.sendTransaction(reporterAddr, deviceName, monitoringType, resolution, recordingTime, targetDistance, srcAddress, {from:eth.coinbase, gas:1000000});
	contract.sendReward.sendTransaction({from:eth.coinbase, gas:1000000});			
}

var getWeight = function(abi, contractAddr, nodeIndex) {
	contract = eth.contract(abi).at(contractAddr);			
	console.log(contract.nodes(contract.nodeLength()-nodeIndex)[1]);
}