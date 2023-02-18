var express = require('express');
var router = express.Router();
var requestORController = require('../controller/requestORController');

router.post('/', requestORController.requestOR);

module.exports = router;