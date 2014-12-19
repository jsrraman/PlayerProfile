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

              docPlayer.countryId = parseInt(countryId);
              docPlayer.playerUrl = PlayersDataScrape.baseScrapeUrl + playerUrlData.attr("href");

              var tempIndex1 = playerUrlData.attr("href").lastIndexOf('/');
              var tempIndex2 = playerUrlData.attr("href").indexOf('.html');
              docPlayer.playerId = parseInt(playerUrlData.attr("href").substring(tempIndex1 + 1, tempIndex2));

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

// Scrape and save player list for a particular country
PlayersDataScrape.scrapeAndSavePlayerProfileForPlayer = function(countryId, playerId, playerUrl, callback) {

  var fnResponse = {};

  if ( (countryId == null) || (countryId == undefined) ||
      (playerId == null) || (playerId == undefined) ||
      (playerUrl == null) || (playerUrl == undefined) ) {
    fnResponse.description = "None of country id, player id, player URL should be empty";
    callback(fnResponse, null);
  }

  debug("Going to get player profile for the requested player URL " + playerUrl +
                                                                " and store them in the database");

  // Scrape the players list from a particular country
  PlayersDataScrape.request(playerUrl, function (error, response, html) {
    if (error) {
      callback(error, null);
    } else {

      debug("Got the scraped data for " + playerUrl);

      // URL fetched successfully so load the html using cheerio library to give us jQuery functionality
      var $ = PlayersDataScrape.cheerio.load(html);

      var docPlayerProfile = {};
      var docBattingAndFieldingAvg = {};

      // Update the known fields so far
      docPlayerProfile.countryId = parseInt(countryId);
      docPlayerProfile.playerId = parseInt(playerId);
      docPlayerProfile.playerUrl = playerUrl;

      // Filter player profile data from the html
      $(".pnl490M").filter(function () {

        // Get the filtered data
        //var data = $(this);

        // Player name
        docPlayerProfile.name = $(".pnl490M .ciPlayernametxt div h1").text().trim();

        // Player country
        docPlayerProfile.country = $(".pnl490M .ciPlayernametxt div h3 b").text().trim();

        // Get the age from the following structure (only the relevant context is shown here)
        // <div class="pnl490M" ------------------------- 1
        //    <div class=".ciPlayernametxt" ------------- 2
        //    <div style="..." -------------------------- 3
        //      <div style="..." ------------------------ 4
        //        <p class="..." ------------------------ 5
        //        <p class="..." ------------------------ 6
        //        <p class="..." ------------------------ 7
        //          <b>Current age</b> ------------------ 8
        //          <span>27 years 184 days</span> ------ 9
        //        </p>
        //          ...
        // Explanation for the navigation is given below
        // 1) $(".pnl490M .ciPlayernametxt") will navigate to 2 above
        //
        // 2) $(".pnl490M .ciPlayernametxt").next() will navigate to next sibling i.e 3 above
        //
        // 3) $(".pnl490M .ciPlayernametxt").next().children(":nth-child(1)") will navigate to
        // first child among its children i.e 4 above.
        // Note:
        // 3a) :nth-child selector is 1 based index since that concept is borrowed from css specification.
        // according to jQuery documentation - http://api.jquery.com/nth-child-selector/
        //
        // 3b) This could also have been navigated like below
        // $(".pnl490M .ciPlayernametxt").next().children().first()
        //
        // 4) $(".pnl490M .ciPlayernametxt").next().children(":nth-child(1)").find("p") will
        // find all the "p" elements under 4.
        //
        // 5) $(".pnl490M .ciPlayernametxt").next().children(":nth-child(1)").find("p").eq(2) will
        // go to the actual "p" element we are interested in. Note eq(index) is used because we
        // have set of "p" elements and you need to go to a particular set, so eq(2) is a good option here.
        // eq(2) can be used only for the identical elements. Most of the items this would cause confusion like
        // applying this for non-matching elements, for ex, one <p> element and <div> element will behave
        // differently.
        //
        // 6) Finally find("span") under the particular "p" element will give you the age i.e 27 years 184 days
        //docPlayerProfile.age = $(".pnl490M .ciPlayernametxt").next().children(":nth-child(1)").
        //                                                    find("p").eq(2).find("span").text().trim();

        var tempData = $(".pnl490M .ciPlayernametxt").next().children(":nth-child(1)").find("p");

        // Player age
        docPlayerProfile.age = tempData.eq(2).find("span").text().trim();

        // Player Batting Style
        docPlayerProfile.battingStyle = tempData.eq(5).find("span").text().trim();

        // Player Bowling Style
        docPlayerProfile.bowlingStyle = tempData.eq(6).find("span").text().trim();

        // Batting and fielding averages

        // For reference*******Don't uncomment this**************
        // Test macthes data
        // temp = $(".pnl490M .engineTable tbody tr td");
        // Note: This is another way of getting data from each td in a loop
        //tempData.find("td").each(function () {
        //  var value = $(this);
        //});
        // For reference*******Don't uncomment this**************

        // Get the test match batting and field averages from the below data structure
        // <div class="pnl490M"
        //  <table class=engineTable>
        //    <thead>
        //      <tbody>
        //        <tr class="..."
        //          <td class="..." ------------ Test data
        //          <td ...
        //        <tr class="..."   ------------  ODI data

        // Test matches
        var docTestBattingAndFieldingAvg = {};

        var testBattingAndFieldingAvgData = $(".pnl490M .engineTable tbody tr td");

        docTestBattingAndFieldingAvg.mat = testBattingAndFieldingAvgData.eq(1).text().trim();
        docTestBattingAndFieldingAvg.runs = testBattingAndFieldingAvgData.eq(4).text().trim();
        docTestBattingAndFieldingAvg.highest = testBattingAndFieldingAvgData.eq(5).text().trim();
        docTestBattingAndFieldingAvg.average = testBattingAndFieldingAvgData.eq(6).text().trim();
        docTestBattingAndFieldingAvg.hundreds = testBattingAndFieldingAvgData.eq(9).text().trim();
        docTestBattingAndFieldingAvg.fifties = testBattingAndFieldingAvgData.eq(10).text().trim();

        docBattingAndFieldingAvg.tests = docTestBattingAndFieldingAvg;

        // ODIs
        var docOdiBattingAndFieldingAvg = {};

        var odiBattingAndFieldingAvgData = $(".pnl490M .engineTable tbody tr").next().find("td");

        docOdiBattingAndFieldingAvg.mat = odiBattingAndFieldingAvgData.eq(1).text().trim();
        docOdiBattingAndFieldingAvg.runs = odiBattingAndFieldingAvgData.eq(4).text().trim();
        docOdiBattingAndFieldingAvg.highest = odiBattingAndFieldingAvgData.eq(5).text().trim();
        docOdiBattingAndFieldingAvg.average = odiBattingAndFieldingAvgData.eq(6).text().trim();
        docOdiBattingAndFieldingAvg.hundreds = odiBattingAndFieldingAvgData.eq(9).text().trim();
        docOdiBattingAndFieldingAvg.fifties = odiBattingAndFieldingAvgData.eq(10).text().trim();

        docBattingAndFieldingAvg.odis = docOdiBattingAndFieldingAvg;
      });

      docPlayerProfile.battingAndFieldingAvg = docBattingAndFieldingAvg;

      //console.log(docPlayerProfile);

      // Save country info to the database
      PlayersDataScrape.db.savePlayerProfile(docPlayerProfile, function (error, result) {
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

PlayersDataScrape.scrapeTopLevelDataForPlayerProfile = function(htmlData, callback) {


  var $ = PlayersDataScrape.cheerio.load(htmlData);

  //$(".pnl490M .ciPlayernametxt h1").filter(function () {






}

module.exports = PlayersDataScrape;