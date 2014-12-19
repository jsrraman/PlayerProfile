var debug = require('debug')('PlayerProfileWebServices');

var express = require('express');
var router = express.Router();
var PlayersDataScrape = require("../common/players-data-scrape");

/* GET the list of cricket playing countries */
router.get('/countries', function(httpReq, httpRes) {

    var fnResponse = {};

    PlayersDataScrape.scrapeAndSaveCountryList(function (error, result) {

        if (error) {
            fnResponse.status = "failure";
            fnResponse.description = error1;
        } else {
            fnResponse.status = "success";
            fnResponse.description = "Country list saved successfully";
        }

        httpRes.send(fnResponse);
    });
});

router.get('/players/country', function(httpReq, httpRes) {

    var countryId = httpReq.param("id");
    var countryName = httpReq.param("name");

    var fnResponse = {};

    if ( (countryId == null) || (countryId == undefined) ||
        (countryName == null) || (countryName == undefined) ) {
        fnResponse.status = "failure";
        fnResponse.description = "Country id or(and) name cannot be empty";

        httpRes.send(fnResponse);
    }

    PlayersDataScrape.scrapeAndSavePlayerListForCountry(countryId,
                                                            countryName, function (error, result) {
        if (error) {
            fnResponse.status = "failure";
            fnResponse.description = error;
        } else {
            fnResponse.status = "success";
            fnResponse.description = "Player list for country " + countryName + " saved successfully";
        }

        httpRes.send(fnResponse);
    });
});

router.get('/player', function(httpReq, httpRes) {

    var countryId = httpReq.param("countryId");
    var playerId = httpReq.param("playerId");
    var playerUrl = httpReq.param("url");

    var fnResponse = {};

    if ( (countryId == null) || (countryId == undefined) ||
        (playerId == null) || (playerId == undefined) ||
        (playerUrl == null) || (playerUrl == undefined) ) {

        fnResponse.status = "failure";
        fnResponse.description = "None of country id, player id, player URL should be empty";
        httpRes.send(fnResponse);
    }

    PlayersDataScrape.scrapeAndSavePlayerProfileForPlayer(countryId, playerId, playerUrl,
                                                                                function (error, result) {
        if (error) {
            fnResponse.status = "failure";
            fnResponse.description = error;
        } else {
            fnResponse.status = "success";
            fnResponse.description = "Player profile for " + playerUrl + " saved successfully";
        }

        httpRes.send(fnResponse);
    });
});

module.exports = router;