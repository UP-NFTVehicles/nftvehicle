var errorControl = require('./errors');
var utilities = require('./utilities');
var initializer = {};



async function callingMint(req){	
	contractABI = utilities.getContainFile(contractABIPath);	//contractABIPath is a global variable
	contractByteCode = utilities.getContainFile(contractByteCodePath); //contractByteCodePath  is a global variable
	contractByteCodeObj = contractByteCode.object;	
	const gas = req.body.gas; 
	const contractAdd=req.body.contractAdd;
	const manufacturerAdd = req.body.manufacturerAdd;
	const governmentAdd = req.body.governmentAdd;
	const tokenId = req.body.tokenId;	
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
				const contractAnswer = await userContract.methods.mint(governmentAdd, tokenId).send({from: manufacturerAdd, gas:gas})
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
                        from:manufacturerAdd,
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
initializer.mint = async function (req, res){
    const gas = req.body.gas;	
    const contractAdd = req.body.contractAdd;	
    const manufacturerAdd = req.body.manufacturerAdd;    
    const governmentAdd = req.body.governmentAdd;
    const tokenId = req.body.tokenId;
	const resul = {Result: "Success"};
	const obj={body:
			{
                gas : req.body.gas, 
                contractAdd:contractAdd,      
                manufacturerAdd : manufacturerAdd,                          
                governmentAdd : governmentAdd,
                tokenId : tokenId            
			}};
		const errNum = errorControl.someFieldIsEmpty(obj);
		if(errNum){  //				
			resul = {
				Result: "Error",
				Num: errNum.toString(),
				Description : errorControl.errors(errNum)
			}
			console.log("Error en Mint");	
			res.send(resul);
		}else{
			try {
				const response = await callingMint(obj).then((resul)=>{
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
				y = errorControl.connectionError(error.message,manufacturerAdd);
				res.send(y); 
			}
		}
}

module.exports = initializer;
