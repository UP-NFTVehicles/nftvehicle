//const { isNull } = require('util');
var errorControl = require('./errors');
var utilities = require('./utilities');
var initializer = {};


async function createNFTVehicleSC(req){
	contractABI = utilities.getContainFile(contractABIPath);	//contractABIPath is a global variable
	contractByteCode = utilities.getContainFile(contractByteCodePath); //contractByteCodePath  is a global variable
	contractByteCodeObj = contractByteCode.object;

	const from = req.body.from; 
	const name_=req.body.name; 
	const symbol=req.body.symbol; 
	const data_=req.body.data;  
	const gas = req.body.gas; 
	var y="";
	try {	
		const { Web3 } = require('web3');
		console.log("Entró1: " + from);
		const ws = await utilities.connectToServer();
		if(ws.Result==="Error") {
			throw new Error("9");			
		}else{
			console.log("Node is alive");
		}
		// ----------------------------------------------		
		await new Promise(async (resolve,reject) => {
			//const provider = new Web3.providers.WebsocketProvider(blockchainAddress);
			//provider.on('connect', async (resul) => {
				console.log("Entró2: " + from);	
			try {
				const web3 = new Web3(Web3.givenProvider || blockchainAddress);	
                const userContract = new web3.eth.Contract(contractABI);                          
                console.log("Entró3: " + from); 
                const nonce = await web3.eth.getTransactionCount(from);
				console.log("nonce:" + nonce);
                const contractAnswer = await userContract.deploy({data: contractByteCodeObj, arguments: [name_, symbol,data_]}).send(
                        { from: from, gas: gas,nonce:nonce}).on('receipt',function(receipt){
                            console.log("Entró4: " + from);
                            //from = from.toUpperCase();
                            const fromRet = receipt.from.toUpperCase(); 
                            if(from.toUpperCase()===fromRet){
                                y={ 
                                    Result: "Success",
                                    transactionHash:receipt.transactionHash.toString(),
                                    contractAddress:receipt.contractAddress.toString(),
                                    gasUsed:receipt.gasUsed.toString(),
                                    blockNumber:receipt.blockNumber.toString(),
                                    blockHash:receipt.blockHash.toString(),
                                    from:fromRet.toString(),
                                    fromorigin:from.toString()
                                };
                                //console.log("Success: from:" + from + "\n" + "fromRet:" + fromRet + "\n" + "nonce:" + nonce );
                                //console.log("Success: from:" + from + "\n" + "fromRet:" + fromRet);
                            }else{
                                y={ 
                                    Result: "ErrRare",
                                    transactionHash:receipt.transactionHash.toString(),
                                    contractAddress:receipt.contractAddress.toString(),
                                    gasUsed:receipt.gasUsed.toString(),
                                    blockNumber:receipt.blockNumber.toString(),
                                    blockHash:receipt.blockHash.toString(),
                                    from:fromRet.toString(),
                                    fromorigin:from.toString()
                                };
                                //console.log("ErrRare: from:" + from + "\n" + "fromRet:" + fromRet + "\n" + "nonce:" + nonce );
                                console.log("ErrRare: from:" + from + "\n" + "fromRet:" + fromRet);
                            }
                        }).on("error",function(error){
							//console.log("Error: " + error + "\n from: " + from);
                            y = { //antes tenía resul
                                Result: "Error",
                                from:from,
								Num : "11",
                                Description : error.message
                            }               
                            console.log(y);
                        });
				web3.currentProvider.disconnect(); //after a request the connection must be closed
				resolve(y);
			} catch (error) {
                    //console.log("Entre al error de conexión 10" + error);
                    resul = {
                        Result: "Error",                        
                        from:from,
						Num : "11",
                        Description : error.message
                    }           
                    console.log(resul);
                    reject(resul);					
			}
		}).then((result) => {
		  //console.log("Promise resolved:", result.Result);
		  y = result;
		})
		.catch((error) => {
		  console.error("Promise rejected:", error.Result);
		  y = error;
		});
		//console.log("Imprimo pero espero promise: " + from);
		return y;	
	} catch (e) {
		console.error("Error:", e.message);
		throw new Error(e.message);
	}
}




// this function must be remote, but it will be local for now
getRole = function(key){
	var role="Not determined";
	switch(key){
		case "0x2CFcBB9Cf2910fBa7E7E7a8092aa1a40BC5BA341": role = "Manufacturer"; break;
		case "0xa6ba79E509d7adb4594852E50D3e48BDcA15D07e": role = "Manufacturer"; break;
		case "0x32d680Aa90D45B677BBa0fFE9Af3d3578dcB4a83": role = "Manufacturer"; break;
		case "0x207Ee448397059BA705629674b2F8c9Df1CA594b": role = "Manufacturer"; break;
		//case "0x207Ee448397059BA705629674b2F8c9Df1CA594": role = "Manufacturer"; break;
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



initializer.createNFTVehicle = async function (req, res){	
	const gas = req.body.gas;
	const name = req.body.name;
	const symbol = req.body.symbol;
	const token = req.body.token;
	const data = req.body.data;
	const from = req.body.from;	
	console.log("Request from: " + from);
	var resul = {Result: "Success"};
	const role = getRole(from);  //this function must be changed when Users Microservice is implemented
	const validToken = tokenValid(token);  //this function must be changed when Users Microservice is implemented
	//It is required that role is the manufacturer
	if(role==="Manufacturer" && validToken==="Yes"){
		//It is required that all variables contain values (not empty)
		const obj={body:
			{
				gas:gas,
				name:name,
				symbol:symbol,
				token:token,
				data:data,
				role:role,
				from:from
				//keyR:keyR
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
			console.log("Processing request from: " + from);
				try{
					const response = await createNFTVehicleSC(obj).then(resul => {					
						let resHE = errorControl.handlingErrorOrNot(resul,from);
						return resHE;
						}).catch((e) =>{
							//console.log("sending error from: " + from + "\nError:" + e.message);
							y = {Result:e.message};
							return y;
						});
					if(response.Result==="9"){
						throw new Error("9");
					}else{
						res.send(response);
					}
				} catch (error) {
					y = errorControl.connectionError(error.message,from);
					res.send(y); 
				}	
			
		}	
	}else{
		resul = {
			Result: "Error",
			Num: "0",
			Description : errorControl.errors(0)
		}
		console.log("Extraño1");
		res.send(resul);
	}
}



module.exports = initializer;
