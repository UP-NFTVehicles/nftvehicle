var errorControl = require('./errors');
var utilities = require('./utilities');
var initializer = {};


async function callingpayToTheGover(req){	
	contractABI = utilities.getContainFile(contractABIPath);	//contractABIPath is a global variable
	contractByteCode = utilities.getContainFile(contractByteCodePath); //contractByteCodePath  is a global variable
	contractByteCodeObj = contractByteCode.object;	
    const gas = req.body.gas; 
    const contractAdd=req.body.contractAdd;
    const value = req.body.value; 
    const transactionType = req.body.transactionType;
    const tokenId = req.body.tokenId;
    const government = req.body.government;
    const owner = req.body.owner;
	var y="";
	try {
		const { Web3 } = require('web3');
		//console.log("Entró1: " + from);
		const ws = await utilities.connectToServer();
		if(ws.Result==="Error") {
			console.log("Error10");
			throw new Error("9");			
		}else{
			console.log("Node is alive");
		}
		
		await new Promise(async (resolve,reject) => {
			try {
				console.log("Entré promise");
				const web3 = new Web3(Web3.givenProvider || blockchainAddress);
				const userContract = new web3.eth.Contract(contractABI,contractAdd);
				const contractAnswer = await userContract.methods.payToTheGovernment(government,tokenId,transactionType).send({from: owner, gas:gas, value:value})
						.on('receipt', function(receipt){
							receipt["Result"] = "Success";
							y = receipt;
						}).on('error', function(error){
							errNum = "7"; 
							y = {
								Result: "Error",
								Num: errNum,
								Description : errorControl.errors(errNum) + " " + error
							}		
							console.log("Error8:" + error);
						});
				web3.currentProvider.disconnect(); //after a request the connection must be closed
				resolve(y);
			}catch (error) {
                    //console.log("Entre al error de conexión 10" + error);
                    resul = {
                        Result: "Error",                        
                        from: owner,
						Num : "11",
                        Description : error.message
                    }           
                    console.log("Error6");
                    reject(resul);									
			}
			}).then((result) => {			
				y = result;
		  	})
		  	.catch((error) => {
				console.log("Error5");
				y = error;
		  	});
		return y;
	}catch (e) {
		console.log("Error4");
		throw new Error(e.message);
	}
}



//this is for public functions
initializer.payToTheGover = async function (req, res){
    const gas = req.body.gas; 
    const contractAdd = req.body.contractAdd; 
    const value = req.body.value; 
    const transactionType = req.body.transactionType;     
    const tokenId = req.body.tokenId;
    const government = req.body.government;
    const owner = req.body.owner;
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
	const errNum = errorControl.someFieldIsEmpty(obj);
	if(errNum){  //				
			resul = {
				Result: "Error",
				Num: errNum.toString(),
				Description : errorControl.errors(errNum)
			}
			res.send(resul);
	}else{
			try {
				const response = await callingpayToTheGover(obj).then((resul)=>{
					//let resHE = errorControl.handlingErrorOrNot(resul,manufacturerAdd);
					let resHE = JSON.parse(JSON.stringify(resul, utilities.replacer));					
					return resHE;
				}).catch((e)=>{
					console.log("Error0");
					y = {Result:e.message};
					return y;
				});
				if(response.Result==="9"){
					console.log("Error1");
					throw new Error("9");
				}else{
					res.send(response);
				}				
			} catch (error) {
				console.log("Error2"+ error.message);
				y = errorControl.connectionError(error.message,owner);
				res.send(y); 
			}
	}
}



module.exports = initializer;
