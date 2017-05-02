smartReward.detectCriminal.sendTransaction('0xc88202356b3bb5758138feda9a2ce4b912bee12c', 1, 2, 3, 40, 2, "http://", {from:eth.accounts[0], gas:1000000});
smartReward.detectCriminal.sendTransaction('0xd22023eb84ff2ca8e4e9024f0d92bbc8574aac16', 2, 1, 2, 20, 5, "http://", {from:eth.accounts[0], gas:1000000});
smartReward.detectCriminal.sendTransaction('0x5397119cd81fff6a32c5972b7b7eac9b00b837a2', 3, 2, 4, 60, 1, "http://", {from:eth.accounts[0], gas:1000000});

smartReward.sendReward.sendTransaction({from:eth.accounts[0], gas:1000000});
smartReward.kill.sendTransaction({from:eth.accounts[0], gas:1000000});