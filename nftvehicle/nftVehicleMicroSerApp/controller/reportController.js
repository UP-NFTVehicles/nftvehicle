var errorControl = require('./errors');
var utilities = require('./utilities');
var initializer = {};


async function callingReport(req,method,fn){
	console.log("OK");
	contractABI = utilities.getContainFile(contractABIPath);	//contractABIPath is a global variable
	contractByteCode = utilities.getContainFile(contractByteCodePath); //contractByteCodePath  is a global variable
	contractByteCodeObj = contractByteCode.object;	
	gas = req.body.gas; 
	contractAdd=req.body.contractAdd;
	const legalOwner = req.body.legalOwner;
	tokenId = req.body.tokenId;	
    description = req.body.description;
	var Web3 = require('web3');
	const web3 = new Web3(Web3.givenProvider || blockchainAddress);
	try {
		await web3.eth.net.isListening();
		console.log('Connected!');
		userContract = new web3.eth.Contract(contractABI,contractAdd);
        try {
/***************************************/
			userContract.methods[method](tokenId,description).send({from: legalOwner, gas:gas })
			.on('transactionHash', function(hash){					
				console.log("Transaction Hash: ", hash);
			})
			.on('receipt', function(receipt){
				console.log("Transaction Receipt: ", receipt);
				fn(receipt);
			})
			.on('confirmation', function(confirmationNumber, receipt){
				console.log("Confirmation Number: ", confirmationNumber);
			})
			.on('error', function(error){
				console.error(error);
				fn(error);
			});
/***************************************/
		} catch (error) {
			console.log(error);
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


//this is for public functions
report = function (req, method, res){
    var gas = req.body.gas;	
    var contractAdd = req.body.contractAdd;	
    var legalOwner = req.body.legalOwner;
    var tokenId = req.body.tokenId;
    var description = req.body.description;
	var resul = {Result: "Success"};
	var obj={body:
			{
                gas : req.body.gas, 
                contractAdd:contractAdd,      
                legalOwner : legalOwner,
                tokenId : tokenId,
                description : description   
			}};
		var errNum = errorControl.someFieldIsEmpty(obj);
		if(errNum){  //				
			resul = {
				Result: "Error",
				Num: errNum,
				Description : errorControl.errors(errNum)
			}		
		}else{
			callingReport(obj,method,function(resul){// this function is async
				res.send(resul); //because of that this line is required
			});
		}
	if(resul.Result=="Error"){
		res.send(resul);
	}
}

initializer.reportEndLifeCycle = function (req, res){
    report(req,"reportEndLifeCycle",res);
}

initializer.reportAsStolen = function (req, res){
    report(req,"reportAsStolen",res);
}


module.exports = initializer;
