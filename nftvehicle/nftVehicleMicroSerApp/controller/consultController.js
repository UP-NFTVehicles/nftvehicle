var errorControl = require('./errors');
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


async function callNFTVehicleSC(req,fn){
	console.log("OK");
	contractABI = getContainFile('./nftVehicles/VehicleNFT.abi');	
	contractByteCode = getContainFile('./nftVehicles/VehicleNFT.bytecode');	
	contractByteCodeObj = contractByteCode.object;
    contractAdd = req.body.contractAdd; 
    publicMethod = req.body.publicMethod; 
	var resultado = 0;
	var Web3 = require('web3');
	const web3 = new Web3(Web3.givenProvider || blockchainAddress);
	try {
		await web3.eth.net.isListening();
		console.log('Connected!');
		userContract = new web3.eth.Contract(contractABI,contractAdd);
        console.log(userContract);
        try {
            userContract.methods[publicMethod]().call().then(function(result) {
                console.log(result);
                fn(result);
              });
        } catch (error) {
            errNum = 4; 
            resul = {
                Result: "Error",
                Num: errNum,
                Description : errorControl.errors(errNum)
            }		
            fn(resul);          
        }
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


//this is for public functions
initializer.consultNFTVehicle = function (req, res){
    var contractAdd = req.body.contractAdd;	
    var publicMethod = req.body.publicMethod;
	var resul = {Result: "Success"};
		var obj={body:
			{
                contractAdd:contractAdd,
                publicMethod: publicMethod
			}};
		var errNum = errorControl.someFieldIsEmpty(obj);
		if(errNum){  //				
			resul = {
				Result: "Error",
				Num: errNum,
				Description : errorControl.errors(errNum)
			}		
		}else{
			callNFTVehicleSC(obj,function(resul){// this function is async
				res.send(resul); //because of that this line is required
			});
		}
	if(resul.Result=="Error"){
		res.send(resul);
	}
}

module.exports = initializer;
