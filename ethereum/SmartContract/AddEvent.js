// 1. Adding detecter watcher
console.log("Adding DetectCriminal event...");
var detectEvent = smartReward.DetectCriminal({}, '', function(error, result) {
	if (!error) {
		console.log("Detecting criminal from Addr: " + result.args.reporter + "!!\n1. Device Type: " + result.args.deviceType + "\n2. Monitoring Type: " + result.args.monitoringType +
					"\n3. Monitoring Resolution: " + result.args.monitoringResolution + "\n4. Recording Time: " + result.args.recordingTime +
					"(s)\n5. Target Distance: " + result.args.targetDistance + "(m)\n6. Source Address: " + result.args.sourceAddress);
	}
});

// 2. Adding transfer watcher
console.log("Adding RewardTransfer event...");
var transferEvent = smartReward.RewardTransfer({}, '', function(error, result) {
	if (!error) {
		console.log("Reward transfer: " + result.args.amount + " tokens were sent. Balances now are as follwing: \nPolice Agency:\t" + result.args.sender +
					" \t" + smartReward.coinBalanceOf.call(result.args.sender) + " tokens \n Reporter:\t" + result.args.reporter + 
					" \t" + smartReward.coinBalanceOf.call(result.args.reporter) + " tokens (temp: " + result.args.temp + ")");
	}
});
