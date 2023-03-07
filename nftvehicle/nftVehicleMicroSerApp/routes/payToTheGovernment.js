var express = require('express');
var router = express.Router();
var payToTheGoverController = require('../controller/payToTheGoverController');


router.post('/', payToTheGoverController.payToTheGover);

module.exports = router;