var errorControl = require('./errors');
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


async function callingMintEraseable(req,fn){
	console.log("OK");
	contractABI = getContainFile(contractABIPath);	//contractABIPath is a global variable
	contractByteCode = getContainFile(contractByteCodePath); //contractByteCodePath  is a global variable
	contractByteCodeObj = contractByteCode.object;	
	gas = req.body.gas; 
	contractAdd=req.body.contractAdd;
	manufacturerAdd = req.body.manufacturerAdd;
	governmentAdd = req.body.governmentAdd;
	tokenId = req.body.tokenId;	
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

async function callingMintBorrable2(req,fn){
	console.log("OK");
	contractABI = getContainFile(contractABIPath);	//contractABIPath is a global variable
	contractByteCode = getContainFile(contractByteCodePath); //contractByteCodePath  is a global variable
	contractByteCodeObj = contractByteCode.object;	
	gas = req.body.gas; 
	contractAdd=req.body.contractAdd;
	manufacturerAdd = req.body.manufacturerAdd;
	governmentAdd = req.body.governmentAdd;
	tokenId = req.body.tokenId;	
	var Web3 = require('web3');
	const web3 = new Web3(Web3.givenProvider || blockchainAddress);
	try {
		await web3.eth.net.isListening();
		console.log('Connected!');
		userContract = new web3.eth.Contract(contractABI,contractAdd);
        try {
/***************************************/
			//const governmentAccount = ethers.BigNumber.from(governmentAdd); // convert string to public key
			const governmentAccount = governmentAdd;
			console.log(governmentAccount);
			const methodToModify = userContract.methods.mint({_government:governmentAccount, _tokenId:tokenId});
			const encodedABI = methodToModify.encodeABI();
			const nonce = await web3.eth.getTransactionCount(web3.eth.defaultAccount);
			const gasPrice = await web3.eth.getGasPrice();
			const rawTransaction = {
			    nonce: web3.utils.toHex(nonce),
			    gasPrice: web3.utils.toHex(gasPrice),
			    gasLimit: web3.utils.toHex(gas),
			    to: contractAdd,
			    data: encodedABI
			};
			const privateKey = manufacturerAdd; 
			//const privateKey = ethers.BigNumber.from(manufacturerAdd); // convert string to public key
			console.log(privateKey);
			//const privateKey = web3.utils.sha3(manufacturerAdd);// Manufacturer's private key
			const signedTransaction = await web3.eth.accounts.signTransaction(rawTransaction, privateKey);
			const transactionHash = await web3.eth.sendSignedTransaction(signedTransaction.rawTransaction);
			console.log("Transaction Hash: ", transactionHash);
			fn("{'TransactionHash': '" + transactionHash + "'}");
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



async function callingMint(req,fn){
	console.log("OK");
	contractABI = getContainFile(contractABIPath);	//contractABIPath is a global variable
	contractByteCode = getContainFile(contractByteCodePath); //contractByteCodePath  is a global variable
	contractByteCodeObj = contractByteCode.object;	
	gas = req.body.gas; 
	contractAdd=req.body.contractAdd;
	const manufacturerAdd = req.body.manufacturerAdd;
	const governmentAdd = req.body.governmentAdd;
	tokenId = req.body.tokenId;	
	var Web3 = require('web3');
	const web3 = new Web3(Web3.givenProvider || blockchainAddress);
	try {
		await web3.eth.net.isListening();
		console.log('Connected!');
		userContract = new web3.eth.Contract(contractABI,contractAdd);
        try {
/***************************************/
			userContract.methods.mint(governmentAdd, tokenId).send({from: manufacturerAdd, gas:gas })
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
initializer.mint = function (req, res){
    var gas = req.body.gas;	
    var contractAdd = req.body.contractAdd;	
    var manufacturerAdd = req.body.manufacturerAdd;    
    var governmentAdd = req.body.governmentAdd;
    var tokenId = req.body.tokenId;
	var resul = {Result: "Success"};
	var obj={body:
			{
                gas : req.body.gas, 
                contractAdd:contractAdd,      
                manufacturerAdd : manufacturerAdd,                          
                governmentAdd : governmentAdd,
                tokenId : tokenId            
			}};
		var errNum = errorControl.someFieldIsEmpty(obj);
		if(errNum){  //				
			resul = {
				Result: "Error",
				Num: errNum,
				Description : errorControl.errors(errNum)
			}		
		}else{
			callingMint(obj,function(resul){// this function is async
				res.send(resul); //because of that this line is required
			});
		}
	if(resul.Result=="Error"){
		res.send(resul);
	}
}

module.exports = initializer;
