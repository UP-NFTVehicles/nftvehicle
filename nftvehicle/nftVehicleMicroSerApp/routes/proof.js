var express = require('express');
var router = express.Router();
//var consultController = require('../controller/consultController');

router.post('/', function(req, res) {
    res.send('This is a proof');
  });

module.exports = router;