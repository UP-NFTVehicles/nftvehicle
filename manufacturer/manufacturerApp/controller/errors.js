var initializer = {};

initializer.errors = function(numError){
    let resultError=[];
    resultError[0]="ERROR: It is required to me a manufacturer";
    resultError[1]="ERROR: It must not contain empty values";
    return resultError[numError];
}

initializer.anyEmpty = function(objVarJSON){
    var objVar = objVarJSON;
    console.log(objVar);
    objVar.array.forEach(element => {
      if(element === null || element.match(/^ *$/) !== null){
        return true;
      }  
    });
    return false;
}

initializer.someFieldIsEmpty = function(ob){
	var obj = ob.body;
	var n = Object.keys(obj).length;
	for(var i=0;i<n;i++){
		var field=Object.keys(obj)[i];
		var fieldV= obj[field];
		if(fieldV==""){
			return 1; // it means that an empty field was found
		}
	}
	return 0;	
}


module.exports = initializer;
