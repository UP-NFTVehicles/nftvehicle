var express = require('express');
var router = express.Router();
var consultEvController = require('../controller/consultEvController');

router.post('/', consultEvController.consultEvNFTVehicle);
module.exports = router;
