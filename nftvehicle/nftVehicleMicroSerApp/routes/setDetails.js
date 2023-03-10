var express = require('express');
var router = express.Router();
var setDetailsController = require('../controller/setDetailsController');

router.post('/setInsuranceDetails', setDetailsController.setInsuranceDetails);
router.post('/setMaintenanceService', setDetailsController.setMaintenanceDetails);

module.exports = router;