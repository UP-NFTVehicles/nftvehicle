var express = require('express');
var router = express.Router();
var consultController = require('../controller/consultController');

router.post('/', consultController.consultNFTVehicle);

module.exports = router;
