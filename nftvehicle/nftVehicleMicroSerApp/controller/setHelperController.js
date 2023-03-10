var errorControl = require('./errors');
var utilities = require('./utilities');
var initializer = {};


async function callingSetHelper(req,fn){
	console.log("OK");
	contractABI = utilities.getContainFile(contractABIPath);	//contractABIPath is a global variable
	contractByteCode = utilities.getContainFile(contractByteCodePath); //contractByteCodePath  is a global variable
	contractByteCodeObj = contractByteCode.object;	
	gas = req.body.gas; 
	contractAdd=req.body.contractAdd;
    const legalOwner = req.body.legalOwner;
    tokenId = req.body.tokenId;
    const helper = req.body.helper;
    typeHelper = req.body.typeHelper;
    about = req.body.about;
	var Web3 = require('web3');
	const web3 = new Web3(Web3.givenProvider || blockchainAddress);
	try {
		await web3.eth.net.isListening();
		console.log('Connected!');
		userContract = new web3.eth.Contract(contractABI,contractAdd);
        try {
/***************************************/
			userContract.methods.setHelper(tokenId,helper,about,typeHelper).send({from: legalOwner, gas:gas })
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
initializer.setHelper = function (req, res){
    var gas = req.body.gas;	
    var contractAdd = req.body.contractAdd;	
    var legalOwner = req.body.legalOwner;
    var tokenId = req.body.tokenId;
    var helper = req.body.helper;
    var typeHelper = req.body.typeHelper;
    var about = req.body.about;
	var resul = {Result: "Success"};
	var obj={body:
			{
                gas : req.body.gas, 
                contractAdd:contractAdd,      
                legalOwner : req.body.legalOwner,
                tokenId : req.body.tokenId,
                tokenId : req.body.tokenId,
                helper : req.body.helper,
                typeHelper : req.body.typeHelper,
                about : req.body.about            
			}};
		var errNum = errorControl.someFieldIsEmpty(obj);
		if(errNum){  //				
			resul = {
				Result: "Error",
				Num: errNum,
				Description : errorControl.errors(errNum)
			}		
		}else{
			callingSetHelper(obj,function(resul){// this function is async
				res.send(resul); //because of that this line is required
			});
		}
	if(resul.Result=="Error"){
		res.send(resul);
	}
}

module.exports = initializer;
