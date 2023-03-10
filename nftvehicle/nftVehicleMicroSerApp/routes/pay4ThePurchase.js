var express = require('express');
var router = express.Router();
var pay4ThePurchaseController = require('../controller/pay4ThePurchaseController');


router.post('/', pay4ThePurchaseController.pay4ThePurchase);

module.exports = router;