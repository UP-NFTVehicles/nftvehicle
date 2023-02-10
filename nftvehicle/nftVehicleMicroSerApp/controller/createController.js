//const { isNull } = require('util');
var errorControl = require('./errors');
//var blockchainAddress = "ws://host.docker.internal:7545";
//var blockchainAddress = "ws://host.docker.internal:8545";
var blockchainAddress = "ws://172.18.1.3:8546";
var initializer = {};


function getContainFile(fileName){	
	const fs = require('fs');	
	const nftVehSol = fileName;
	const path = require('path');
	const roo = path.resolve('', '', nftVehSol);
	const source = fs.readFileSync(roo, 'UTF-8');
	compiledCode = JSON.parse(source);
	return compiledCode;
}


async function createNFTVehicleSC(req,fn){
	console.log("OK");
	var receiptG;
	var receipt;
	contractABI = getContainFile('./nftVehicles/VehicleNFT.abi');	
	//contractABI = require('./nftVehicles/VehicleNFT.abi');	
	contractByteCode = getContainFile('./nftVehicles/VehicleNFT.bytecode');	
	contractByteCodeObj = contractByteCode.object;

	from = req.body.from; // from = "0xa6ba79E509d7adb4594852E50D3e48BDcA15D07e";
	name_=req.body.name; // name_="CX5";
	symbol=req.body.symbol; // symbol="CX5S";
	data_=req.body.data;  //data_="CX5S_data";
	gas = req.body.gas; // gas = "3000000";
	console.log(gas);
	var resultado = 0;
	var Web3 = require('web3');
	//try{
	const web3 = new Web3(Web3.givenProvider || blockchainAddress);
	try {
		await web3.eth.net.isListening();
		console.log('Connected!');
		userContract = new web3.eth.Contract(contractABI);
		userContract.deploy({data: contractByteCodeObj, arguments: 
				[name_, symbol,data_]}).send({from: from, gas: gas}).on('receipt', 
					function(receipt){
						console.log("Entré no hay error");
						var y={	
							Result: "Success",
							transactionHash:receipt.transactionHash,
							contractAddress:receipt.contractAddress,
							gasUsed:receipt.gasUsed,
							blockNumber:receipt.blockNumber,
							blockHash:receipt.blockHash
						};
						 receiptG = y;
						 console.log(receipt);
						 fn(receiptG);
					 }).on('error', 
						function(error, receipt) {
							errNum = 2; //it is assigned in the /controller/errors.js
							resul = {
								Result: "Error",
								Num: errNum,
								Description : errorControl.errors(errNum)
							}		
							fn(resul);
											 });
	} catch (e) {
		console.log(e);
		errNum = 2; //it is assigned in the /controller/errors.js
		resul = {
			Result: "Error",
			Num: errNum,
			Description : errorControl.errors(errNum)
		}		
		fn(resul);
	}
}

deploy=function(req,fn){
	//It creates a smart contract
	var obj;
		createNFTVehicleSC(req,function(resul){
			fn(obj);
		});

}

// this function must be remote, but it will be local for now
getRole = function(key){
	var role="Not determined";
	switch(key){
		case "0xa6ba79E509d7adb4594852E50D3e48BDcA15D07e": role = "Manufacturer"; break;
		default: role = "Not determined"; break; 
	}
	return role;
}

// this function must be remote, but it will be local for now
tokenValid = function(idToken){
	var result;
	switch(idToken){
		case "1FMADSFJL2432SDKFJA": result = "Yes"; break;
		default: result = "Not"; break;
	}
	return result;
}



initializer.createNFTVehicle = function (req, res){
	var gas = req.body.gas;
	var name = req.body.name;
	var symbol = req.body.symbol;
	var idtoken = req.body.idtoken;
	var data = req.body.data;
	//var role = req.body.role;	
	var from = req.body.from;	
	//var keyR = req.body.keyR;	
	var resul = {Result: "Success"};
	var role = getRole(from);  //this function must be changed when Users Microservice is implemented
	var validToken = tokenValid(idtoken);  //this function must be changed when Users Microservice is implemented
	//It is required that role is the manufacturer
	if(role==="Manufacturer" && validToken==="Yes"){
		//It is required that all variables contain values (not empty)
		var obj={body:
			{
				gas:gas,
				name:name,
				symbol:symbol,
				idtoken:idtoken,
				data:data,
				role:role,
				from:from
				//keyR:keyR
			}};
		var errNum = errorControl.someFieldIsEmpty(obj);
		if(errNum){  //				
			resul = {
				Result: "Error",
				Num: errNum,
				Description : errorControl.errors(errNum)
			}		
		}else{
			createNFTVehicleSC(obj,function(resul){// this function is async
				res.send(resul); //because of that this line is required
			});

/*			deploy(obj,function(resul){ // this function is async							
				res.send(resul); //because of that this line is required
			});			*/
		}
	}else{
		resul = {
			Result: "Error",
			Num: 0,
			Description : errorControl.errors(0)
		}		
	}
	if(resul.Result=="Error"){
		res.send(resul);
	}
}

/*
initializer.default = function (req, res){
	var obj = {
		output: "This is the create service"
	};
	res.send(obj);
}
*/

module.exports = initializer;
