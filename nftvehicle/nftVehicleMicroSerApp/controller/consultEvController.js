var errorControl = require('./errors');
var utilities = require('./utilities');
var initializer = {};

async function getEventNFTVehicleSC(req,fn){
	console.log("OK");
	contractABI = utilities.getContainFile(contractABIPath);	//contractABIPath is a global variable
	contractByteCode = utilities.getContainFile(contractByteCodePath); //contractByteCodePath  is a global variable
	contractByteCodeObj = contractByteCode.object;
	//gas = req.body.gas;
    contractAdd = req.body.contractAdd; 
    eventSC = req.body.eventSC; 
	//from = req.body.from;
	var resultado = 0;
	var Web3 = require('web3');
	const web3 = new Web3(Web3.givenProvider || blockchainAddress);
	try {
		await web3.eth.net.isListening();
		console.log('Connected!');
		userContract = new web3.eth.Contract(contractABI,contractAdd);
        //console.log(from);
		//console.log(gas);
        try {
			//{from:from,gas:gas}
            userContract.getPastEvents(eventSC,{fromBlock: 0, toBlock:'latest'}).then(function(result) {
                console.log(result);
                fn(result);
              });
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
initializer.consultEvNFTVehicle = function (req, res){
	//var gas = req.body.gas;	
    var contractAdd = req.body.contractAdd;	
    var eventSC = req.body.eventSC;
	//var from = req.body.from;
	var resul = {Result: "Success"};
	var obj={body:
			{	
                //gas:gas,
                contractAdd:contractAdd,
                eventSC: eventSC
				//from:from
			}};
	var errNum = errorControl.someFieldIsEmpty(obj);
	if(errNum){  //				
			resul = {
				Result: "Error",
				Num: errNum,
				Description : errorControl.errors(errNum)
			}		
	}else{
			getEventNFTVehicleSC(obj,function(resul){// this function is async
				res.send(resul); //because of that this line is required
			});	
	}
	if(resul.Result=="Error"){
		res.send(resul);
	}
}


module.exports = initializer;