var express = require('express');
var router = express.Router();
var reportController = require('../controller/reportController');

router.post('/EndLifeCycle', reportController.reportEndLifeCycle);
router.post('/AsStolen', reportController.reportAsStolen);

module.exports = router;