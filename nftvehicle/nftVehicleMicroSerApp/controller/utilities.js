var initializer = {};

initializer.getContainFile = function(fileName){	
	const fs = require('fs');	
	const nftVehSol = fileName;
	const path = require('path');
	const roo = path.resolve('', '', nftVehSol);
	const source = fs.readFileSync(roo, 'UTF-8');
	compiledCode = JSON.parse(source);
	return compiledCode;
}

module.exports = initializer;
