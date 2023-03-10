var express = require('express');
var router = express.Router();
var requestChangeStolenStatusController = require('../controller/requestChangeStolenSController');

router.post('/', requestChangeStolenStatusController.requestChangeStolenStatus);

module.exports = router;