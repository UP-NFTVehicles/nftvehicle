var express = require('express');
var router = express.Router();
var consultController = require('../controller/consultController');

router.post('/', consultController.consultNFTVehicle);
router.post('/ownerOf', consultController.ownerOf);
module.exports = router;
