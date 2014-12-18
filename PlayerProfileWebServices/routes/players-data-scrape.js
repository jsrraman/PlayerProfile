var debug = require('debug')('PlayerProfileWebServices');

var request = require('request');
var cheerio = require('cheerio');
var express = require('express');
var router = express.Router();
var db = require('../db/players-dao');
var baseScrapeUrl = "http://www.espncricinfo.com";

/* GET the list of cricket playing countries and store it in the database */
router.get("/countries", function(httpReq, httpRes) {

  debug("Going to get the list of cricket playing countries and store it in the database");

  var fnResponse = {};

  // Scrape the country list from the following URL
  var countryListUrl = '/ci/content/site/cricket_squads_teams/index.html';
  var actualScrapeUrl = baseScrapeUrl + countryListUrl;

  // Go ahead and scrape the data
  request(actualScrapeUrl, function (error, response, html) {
    if (error) {

      fnResponse.status = "failure";
      fnResponse.description = "There was error in getting the country list from " + actualScrapeUrl;
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
                docCountry.countryId = parseInt(thumbnailUrl.substring(tempIndex1 + 1, tempIndex2));

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

// GET the players for the requested country id
router.get("/players/country", function(httpReq, httpRes) {

  var fnResponse = {};

  var countryId = httpReq.param("id");
  var countryName = httpReq.param("name");

  if ((countryId == null ) || ((countryId == undefined )) ||
      (countryName == null ) || ((countryName == undefined ))) {
    fnResponse.status = "failure";
    fnResponse.description = "Country id cannot be empty";
    httpRes.send(fnResponse);
  }

  debug("Going to get the list of players for the requested country id = " + countryId +
                                      " and store them in the database");

  // Scrape the players list from a particular country
  //actualScrapeUrl = "http://www.espncricinfo.com/australia/content/player/country.html?country=2";
  var playerListUrl = '/content/player/country.html?country=';
  var actualScrapeUrl = baseScrapeUrl + "/" + countryName + playerListUrl + countryId;

  // Go ahead and scrape the data
  request(actualScrapeUrl, function (error, response, html) {
    if (error) {
      fnResponse.status = "failure";
      fnResponse.description = error;
      httpRes.send(fnResponse);

    } else {

      debug("Got the scraped data...");

      // URL fetched successfully so load the html using cheerio library to give us jQuery functionality
      var $ = cheerio.load(html);

      var docPlayerList = []; // new Array literal syntax :)

      // Filter "all player" data from the html
      $("#rectPlyr_Playerlistall").filter(function () {

        // Get the filtered data
        var data = $(this);

       //debug("data:\n" + data);

        // Extract a player's id and name
        $("#rectPlyr_Playerlistall .playersTable tr").each(function () {

          $(this).find('td').each(function () {

            var colData = $(this);
            var playerUrlData = colData.find('a');

            if ((playerUrlData !== null) && (playerUrlData !== undefined)) {

              var docPlayer = {};

              // Player URL
              docPlayer.countryId = countryId;
              docPlayer.playerUrl = baseScrapeUrl + playerUrlData.attr("href");
              docPlayer.name = playerUrlData.text();

              //console.log(docPlayer);

              docPlayerList.push(docPlayer);
            }
          });
        });
      });

      // Save country info to the database
      db.savePlayerList(docPlayerList, function (error, result) {

        if (error) {
          fnResponse.status = "failure";
          fnResponse.description = error;
        } else {
          fnResponse.status = "success";
          fnResponse.description = 'Player list stored successfully';
        }

        // Send the response to the API caller
        httpRes.send(fnResponse);
      });
    }
  });
});


module.exports = router;