var express = require('express');
var router = express.Router();
var acceptHelperServiceController = require('../controller/acceptHelperServiceController');


router.post('/', acceptHelperServiceController.acceptHelperService);

module.exports = router;