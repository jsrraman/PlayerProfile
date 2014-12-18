var express = require('express');
var router = express.Router();
var PlayersDataScrape = require("../common/players-data-scrape");

/* GET home page. */
router.get('/', function(req, res) {
  res.render('index', { title: 'Player Profile Web Services' });
});

module.exports = router;