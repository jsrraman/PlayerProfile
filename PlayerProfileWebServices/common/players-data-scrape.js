var debug = require('debug')('PlayerProfileWebServices');

// Create the root namespace and make sure it is not overwritten
var PlayersDataScrape = PlayersDataScrape || {};

PlayersDataScrape.request = require('request');
PlayersDataScrape.cheerio = require('cheerio');
PlayersDataScrape.fs = require('fs');
PlayersDataScrape.db = require('../db/players-dao');
PlayersDataScrape.baseScrapeUrl = "http://www.espncricinfo.com";

// Scrape the country list data and save it to the database
PlayersDataScrape.scrapeAndSaveCountryList = function(callback) {

  debug("Going to get the list of cricket playing countries and store it in the database");

  // Scrape the country list from the following URL
  var countryListUrl = '/ci/content/site/cricket_squads_teams/index.html';
  var actualScrapeUrl = PlayersDataScrape.baseScrapeUrl + countryListUrl;

  // Go ahead and scrape the data
  PlayersDataScrape.request(actualScrapeUrl, function (error, response, html) {
    if (error) {
        callback(error, response);
    } else {

      debug("Got the scraped data for " + actualScrapeUrl);

      // URL fetched successfully so load the html using cheerio library to give us jQuery functionality
      var $ = PlayersDataScrape.cheerio.load(html);

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
      PlayersDataScrape.db.saveCountryList(docCountryList, function (error, result) {
        // Send the response to the API caller
        callback(error, result);
      });
    }
  });
};

// Scrape and save player list for a particular country
PlayersDataScrape.scrapeAndSavePlayerListForCountry = function(countryId, countryName, callback) {

  var fnResponse = {};

  if ( (countryId == null ) || (countryId == undefined ) ||
      (countryName == null ) || (countryName == undefined ) ) {
    fnResponse.description = "Country id or(and) name cannot be empty";
    callback(fnResponse, null);
  }

  debug("Going to get the list of players for the requested country id = " + countryId +
  " and store them in the database");

  // Scrape the players list from a particular country
  //actualScrapeUrl = "http://www.espncricinfo.com/australia/content/player/country.html?country=2";
  var playerListUrl = '/content/player/country.html?country=';
  var actualScrapeUrl = PlayersDataScrape.baseScrapeUrl + "/" + countryName + playerListUrl + countryId;

  PlayersDataScrape.request(actualScrapeUrl, function (error, response, html) {
    if (error) {
      callback(error, null);
    } else {

      debug("Got the scraped data for " + actualScrapeUrl);

      // URL fetched successfully so load the html using cheerio library to give us jQuery functionality
      var $ = PlayersDataScrape.cheerio.load(html);

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
              docPlayer.countryId = parseInt(countryId);
              docPlayer.playerUrl = PlayersDataScrape.baseScrapeUrl + playerUrlData.attr("href");
              docPlayer.name = playerUrlData.text();

              //console.log(docPlayer);

              docPlayerList.push(docPlayer);
            }
          });
        });
      });

      // Save country info to the database
      PlayersDataScrape.db.savePlayerList(docPlayerList, function (error, result) {
        // Send the response to the API caller
        if (error)
          callback(error, null);
        else {
          callback(null, null);
        }
      });
    }
  });
};

module.exports = PlayersDataScrape;