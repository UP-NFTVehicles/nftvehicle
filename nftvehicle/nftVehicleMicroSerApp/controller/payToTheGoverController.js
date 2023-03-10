var errorControl = require('./errors');
var utilities = require('./utilities');
var initializer = {};


async function callingpayToTheGover(req,fn){
	console.log("OK");
	contractABI = utilities.getContainFile(contractABIPath);	//contractABIPath is a global variable
	contractByteCode = utilities.getContainFile(contractByteCodePath); //contractByteCodePath  is a global variable
	contractByteCodeObj = contractByteCode.object;	
	gas = req.body.gas; 
	contractAdd=req.body.contractAdd;
    value = req.body.value;	
    transactionType = req.body.transactionType;
    tokenId = req.body.tokenId;
    const government = req.body.government;
    const owner = req.body.owner;
	var Web3 = require('web3');
	const web3 = new Web3(Web3.givenProvider || blockchainAddress);
	try {
		await web3.eth.net.isListening();
		console.log('Connected!');
		userContract = new web3.eth.Contract(contractABI,contractAdd);
        try {
/***************************************/
			userContract.methods.payToTheGovernment(government,tokenId,transactionType).send({from: owner, gas:gas, value:value})
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
initializer.payToTheGover = function (req, res){
    var gas = req.body.gas;	
    var contractAdd = req.body.contractAdd;	
    var value = req.body.value;	
    var transactionType = req.body.transactionType;	    
    var tokenId = req.body.tokenId;
    var government = req.body.government;
    var owner = req.body.owner;
	var resul = {Result: "Success"};
	var obj={body:
			{
                gas : req.body.gas, 
                contractAdd:contractAdd,      
                value:value,
                transactionType:transactionType,
                tokenId : tokenId,
                government : government,
                owner : owner
			}};
		var errNum = errorControl.someFieldIsEmpty(obj);
		if(errNum){  //				
			resul = {
				Result: "Error",
				Num: errNum,
				Description : errorControl.errors(errNum)
			}		
		}else{
			callingpayToTheGover(obj,function(resul){// this function is async
				res.send(resul); //because of that this line is required
			});
		}
	if(resul.Result=="Error"){
		res.send(resul);
	}
}

module.exports = initializer;
