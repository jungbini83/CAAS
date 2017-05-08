if (typeof web3 !== 'undefined') {
	web3 = new Web3(web3.currentProvider);
} else {
	web3 = new Web3(new Web3.providers.HttpProvider("http://localhost:9430"));
}

var filter = web3.eth.filter('lastest');
filter.watch(function(error, result) {
	var block = web3.eth.getBlock(result, true);
	
	if(!error) {
		console.log('Hello~!');
		console.log('block #' + block.number);
		console.dir(block.transactions);
	} else {
		console.error(error);
	}
})