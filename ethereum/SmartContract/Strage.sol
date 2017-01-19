pragma solidity ^0.4.0;

contract SimpleStorage {
	unit storedData;

	function set(unit x) {
		storedData = x;
	}

	function get() constant returns (unit) {
		return storedData;
	}
}