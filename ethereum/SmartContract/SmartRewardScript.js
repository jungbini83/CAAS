// 1. setting default account
eth.defaultAccount = eth.coinbase;

// 2. write smart contract source
var smartRewardSource = 'contract SmartReward { address public policeAgency; mapping (address => uint) public coinBalanceOf; uint public nodeLength; /* 1,000,000,000 Gwei = 1 ether = about $80 (maximum = 4,860,000,000 where 60 second and 1 meter) */ uint public deviceTypeWeight; uint public monitoringTypeWeight; uint public monitoringResolutionWeight; uint public recordingTimeWeight; uint public targetDistanceWeight; /* implement 100 - (targetDistanceWeight * meter) in Constructor */ mapping (uint => NodeInfo) public nodes; event DetectCriminal(address reporter, uint deviceType, uint monitoringType, uint monitoringResolution, uint recordingTime, uint targetDistance, string sourceAddress); event RewardTransfer(address sender, address reporter, uint temp, uint amount); struct NodeInfo { address addr; uint weight; uint deviceType; uint monitoringType; uint monitoringResolution; uint recordingTime; uint targetDistance; string sourceAddress; } function SmartReward(uint totalReward) payable { policeAgency = msg.sender; coinBalanceOf[policeAgency] = totalReward; /* supply total reward from sender */ nodeLength = 0; deviceTypeWeight = 30; monitoringTypeWeight = 50; monitoringResolutionWeight = 25; recordingTimeWeight = 1; targetDistanceWeight = 1; /* implement 100 - (targetDistanceWeight * meter) in Constructor */ } function detectCriminal(address sender, uint _deviceType, uint _monitoringType, uint _monitoringResolution, uint _recordingTime, uint _targetDistance, string _sourceAddress) { nodes[nodeLength] = NodeInfo(sender, 0, _deviceType, _monitoringType, _monitoringResolution, _recordingTime, _targetDistance, _sourceAddress); DetectCriminal(sender, _deviceType, _monitoringType, _monitoringResolution, _recordingTime, _targetDistance, _sourceAddress); nodeLength += 1; } function sendReward() returns(bool sufficient) { bool result; uint sumWeight = 0; for (uint i = 0 ; i < nodeLength ; ++i) { if (nodes[i].weight == 0) { nodes[i].weight += (nodes[i].deviceType * deviceTypeWeight); nodes[i].weight += (nodes[i].monitoringType * monitoringTypeWeight); nodes[i].weight += (nodes[i].monitoringResolution * monitoringResolutionWeight); nodes[i].weight += (nodes[i].recordingTime * recordingTimeWeight); nodes[i].weight += (100-(targetDistanceWeight * nodes[i].targetDistance)); sumWeight = nodes[i].weight; } } for (uint j = 0 ; j < nodeLength ; ++j) { if (coinBalanceOf[policeAgency] < nodes[j].weight) return false; coinBalanceOf[policeAgency] -= nodes[j].weight; coinBalanceOf[nodes[j].addr] += nodes[j].weight; RewardTransfer(policeAgency, nodes[j].addr, nodes[j].deviceType * deviceTypeWeight, nodes[j].weight); } return true; } function kill() { if (msg.sender == policeAgency) suicide(policeAgency); } }';

// 3. compile the source code
var smartRewardCompiled = eth.compile.solidity(smartRewardSource);

// 4. deploy the compiled contract
var smartRewardContract = eth.contract(smartRewardCompiled.SmartReward.info.abiDefinition);

// 5. Creating smart contract using constructor
var smartReward = smartRewardContract.new(5000000, {from: eth.coinbase, data: smartRewardCompiled.SmartReward.code, gas: 1000000}, function(e, contract) {
	if(!e) {
		if(!contract.address) {						
			console.log(contract.transactionHash);				
			
		} else {
			console.log("Contract mined! Address: " + contract.address);				
		}
	}
});

// 6. Adding events when the contract is deployed
function checkAddress() {
	if(smartReward.address) {
		
		console.log(smartReward.address);

		// 6-1. Adding detecter watcher
		console.log("Adding DetectCriminal event...");
		var detectEvent = smartReward.DetectCriminal({}, '', function(error, result) {
			if (!error) {
				console.log("Detecting criminal from Addr: " + result.args.reporter + "!!\n1. Device Type: " + result.args.deviceType + "\n2. Monitoring Type: " + result.args.monitoringType +
							"\n3. Monitoring Resolution: " + result.args.monitoringResolution + "\n4. Recording Time: " + result.args.recordingTime +
							"(s)\n5. Target Distance: " + result.args.targetDistance + "(m)\n6. Source Address: " + result.args.sourceAddress);
			}
		});

		// 6-2. Adding transfer watcher
		console.log("Adding RewardTransfer event...");
		var transferEvent = smartReward.RewardTransfer({}, '', function(error, result) {
			if (!error) {
				console.log("Reward transfer: " + result.args.amount + " tokens were sent. Balances now are as follwing: \nPolice Agency:\t" + result.args.sender +
							" \t" + smartReward.coinBalanceOf.call(result.args.sender) + " tokens \n Reporter:\t" + result.args.reporter + 
							" \t" + smartReward.coinBalanceOf.call(result.args.reporter) + " tokens (temp: " + result.args.temp + ")");
			}
		});

		clearInterval(t);
	}
}

t = setInterval(checkAddress, 1000);