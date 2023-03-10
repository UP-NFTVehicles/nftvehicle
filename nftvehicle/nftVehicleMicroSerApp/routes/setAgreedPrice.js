var express = require('express');
var router = express.Router();
var setAgreedPriceController = require('../controller/setAgreedPriceController');


router.post('/', setAgreedPriceController.setAgreedPrice);

module.exports = router;