var debug = require('debug')('PlayerProfileWebServices');

var request = require('request');
var cheerio = require('cheerio');
var express = require('express');
var router = express.Router();
var db = require('../db/players-dao');

/* GET the list of cricket playing countries and store it in the database */
router.get('/players/countries', function(httpReq, httpRes) {

  debug("Going to get the list of cricket playing countries and store it in the database");

  var fnResponse = {};

  // Scrape the country list from the following URL
  url = 'http://www.espncricinfo.com/ci/content/site/cricket_squads_teams/index.html';

  // Go ahead and scrape the data
  request(url, function (error, response, html) {
    if (error) {

      fnResponse.status = "failure";
      fnResponse.description = "There was error in getting the country list from " + url;
      httpRes.send(fnResponse);

    } else {

      debug("Got the scraped data...");

      // URL fetched successfully so load the html using cheerio library to give us jQuery functionality
      var $ = cheerio.load(html);

      var docCountryList = []; // new Array literal syntax :)

      // Filter the team list data from the html
      $('.teamList').filter(function () {

        // Get the filtered data
        var data = $(this);

        //debug("Teamlist data:\n" + data);

        // Extract a country's thumbnail image url, id and name
        $('.teamList tr').each(function () {

          $(this).find('td').each(function () {

            var colData = $(this);
            var imgData = colData.find('img');

            if ((imgData !== null) && (imgData !== undefined)) {

              // Thumbnail image url
              var thumbnailUrl = imgData.attr('src');

              if ((thumbnailUrl !== null) && (thumbnailUrl !== undefined)) {

                var docCountry = {}; // new Object literal syntax

                docCountry.thumbnailUrl = thumbnailUrl;

                // id
                var tempIndex1 = thumbnailUrl.lastIndexOf('/');
                var tempIndex2 = thumbnailUrl.indexOf('.jpg');
                docCountry.id = thumbnailUrl.substring(tempIndex1 + 1, tempIndex2);

                // Name
                docCountry.name = imgData.attr('title');

                docCountryList.push(docCountry);
              }
            }
          });
        });
      });

      // Save country info to the database
      db.saveCountryList(docCountryList, function (error, result) {

        if (error) {
          fnResponse.status = "failure";
          fnResponse.description = error;
        } else {
          fnResponse.status = "success";
          fnResponse.description = 'Country list stored successfully';
        }

        // Send the response to the API caller
        httpRes.send(fnResponse);
      });
    }
  });
});

module.exports = router;