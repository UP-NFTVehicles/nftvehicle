var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
var createRouter = require('./routes/create');
var consultRouter = require('./routes/consult');
var consultEvRouter = require('./routes/consultEvents');
var mintRouter = require('./routes/mint');
var requestORRouter = require('./routes/requestOwnerRight');
var setCost4TransRouter = require('./routes/setCost4Trans');
var payToTheGovernmentRouter = require('./routes/payToTheGovernment');
var setAgreedPriceRouter = require('./routes/setAgreedPrice');
var pay4ThePurchaseRouter = require('./routes/pay4ThePurchase');
var reportRouter = require('./routes/report');
var requestChangeStolenStatusRouter = require('./routes/requestChangeStolenStatus');
var setHelperRouter = require('./routes/setHelper');
var setDetailsRouter = require('./routes/setDetails');
var acceptHelperServiceRouter = require('./routes/acceptHelperService');
var proof = require('./routes/proof');

var app = express();

//establishing a global variable
global.blockchainAddress = "ws://172.18.1.2:8546";
global.contractABIPath = "./nftVehicles/VehicleNFT.abi";
global.contractByteCodePath = "./nftVehicles/VehicleNFT.bytecode";

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/create', createRouter);
app.use('/consultInfo', consultRouter);
app.use('/consultEvents', consultEvRouter);
app.use('/mint', mintRouter);
app.use('/requestOwnerRight', requestORRouter);
app.use('/setCost4Trans', setCost4TransRouter);
app.use('/payToTheGovernment', payToTheGovernmentRouter);
app.use('/setAgreedPrice', setAgreedPriceRouter);
app.use('/pay4ThePurchase', pay4ThePurchaseRouter);
app.use('/report', reportRouter);
app.use('/requestChangeStolenStatus', requestChangeStolenStatusRouter);
app.use('/setHelper', setHelperRouter);
app.use('/setDetails', setDetailsRouter); //setInsuranceDetails and 
                                          // setMaintenanceDetails
app.use('/acceptHelperService', acceptHelperServiceRouter);
app.use('/proof', proof);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
