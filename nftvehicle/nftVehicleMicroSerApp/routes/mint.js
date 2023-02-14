var express = require('express');
var router = express.Router();
var mintController = require('../controller/mintController');

router.post('/', mintController.mint);

module.exports = router;