var express = require('express');
var router = express.Router();
var setCost4TransController = require('../controller/setCost4TransController');


router.post('/', setCost4TransController.setCost4Trans);

module.exports = router;