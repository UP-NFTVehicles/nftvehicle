var express = require('express');
var router = express.Router();
var createController = require('../controller/createController');

/* GET users listing. */
/*
router.get('/', function(req, res, next) {
  res.send('This is the create service');
});
*/
//router.get('/', createController.default);
router.post('/', createController.createNFTVehicle);

module.exports = router;

