var debug = require('debug')('PlayerProfileWebServices');

var express = require('express');
var router = express.Router();
var db = require('../db/players-dao');

/* GET the list of cricket playing countries */
router.get('/countries', function(httpReq, httpRes) {

  var fnResponse = {};

  db.getCountryList(function (error, result) {
    if (error) {
      fnResponse.status = "failure";
      fnResponse.description = error;
    } else {
      fnResponse.status = "success";
      fnResponse.countryList = result;
    }

    // Send the response to the API caller
    httpRes.send(fnResponse);
  });
});

module.exports = router;