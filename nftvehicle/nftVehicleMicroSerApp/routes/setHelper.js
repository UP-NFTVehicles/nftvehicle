var express = require('express');
var router = express.Router();
var setHelperController = require('../controller/setHelperController');


router.post('/', setHelperController.setHelper);

module.exports = router;