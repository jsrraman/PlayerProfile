/**
 * Created by rajaraman on 16/12/14.
 * Interface for accessing players-profile database
 */
var debug = require('debug')('PlayerProfileWebServices');

var PlayersDao = {};

PlayersDao.dbName = "players-profile";
PlayersDao.mongoClient = require('mongodb').MongoClient;
PlayersDao.connectionUrl = "mongodb://localhost:27017/" + PlayersDao.dbName;

// Deletes the database
PlayersDao.deleteDbs = function() {

    PlayersDao.mongoClient.connect(PlayersDao.connectionUrl, function (err, db) {

        if (err) {
            debug("Could not open " + PlayersDao.dbName + " database: " + err );
            callback(err, db);
        } else {
            db.dropDatabase(function (err, result) {
                if (!err) {
                    debug(PlayersDao.dbName + " database dropped successfully");
                }

                db.close();
            });
        }
    });
}

// Saves the scraped countries list to the database
PlayersDao.saveCountryList = function(docCountryList, callback) {

    PlayersDao.mongoClient.connect(PlayersDao.connectionUrl, function (err, db) {

        if (err) {
            debug("Could not open " + PlayersDao.dbName + " database: " + err );
            callback(err, db);
        } else {

            db.dropDatabase(function (err, result) {
                if (!err) {
                    debug(PlayersDao.dbName + " database cleaned up");
                }
            });

            var countryCollection = db.collection('countries');

            countryCollection.insert(docCountryList, function (err, result) {
                if (!err) {
                    debug('Country list saved successfully');
                }

                db.close();
                callback(err);
            });
        }
    });
};

// Get the country list
PlayersDao.getCountryList = function(callback) {

    PlayersDao.mongoClient.connect(PlayersDao.connectionUrl, function (err, db) {

        if (err) {
            debug("Could not open " + PlayersDao.dbName + " database: " + err );
            callback(err, db);
        } else {

            var countryCollection = db.collection('countries');

            // Get the records without "_id" column and sorted by country id
            countryCollection.find({},{_id:0}).sort({countryId:1}).toArray(function (err, result) {
                if (!err) {
                    debug('Country list fetched successfully');
                }

                db.close();

                callback(err, result);
            });
        }
    });
};

// Saves the scraped players list for a particular country to the database
PlayersDao.savePlayerList = function(docPlayerList, callback) {

    PlayersDao.mongoClient.connect(PlayersDao.connectionUrl, function (err, db) {

        if (err) {
            debug("Could not open " + PlayersDao.dbName + " database: " + err );
            callback(err, null);
        } else {

            var playerCollection = db.collection('players');

            playerCollection.insert(docPlayerList, function (err, result) {
                if (!err) {
                    debug('Player list saved successfully');
                }

                db.close();
                callback(null, null);
            });
        }
    });
};

// Given a player id, retrieves his country id and url (basic info)
PlayersDao.getPlayerBasicInfo = function(playerId, callback) {

    PlayersDao.mongoClient.connect(PlayersDao.connectionUrl, function (err, db) {

        if (err) {
            debug("Could not open " + PlayersDao.dbName + " database: " + err );
            callback(err, null);
        } else {

            var playerCollection = db.collection('players');

            var query = {};
            query.playerId = playerId;

            playerCollection.findOne(query, function (err, result) {
                if (err) {

                    debug('Player basic info could not be retrieved successfully');

                    db.close();
                    callback(err, null);

                } else {

                    debug('Player basic info retrieved successfully');
                    db.close();
                    callback(null, result);
                }
            });
        }
    });
}

// Saves the scraped player profile data for a particular player to the database
PlayersDao.savePlayerProfile = function(docPlayerProfile, callback) {

    PlayersDao.mongoClient.connect(PlayersDao.connectionUrl, function (err, db) {

        if (err) {
            debug("Could not open " + PlayersDao.dbName + " database: " + err );
            callback(err, null);
        } else {

            var playerCollection = db.collection('players');

            var query = {};
            query.playerId = docPlayerProfile.playerId;

            playerCollection.update(query, docPlayerProfile, function (err, result) {
                if (!err) {
                    debug('Player profile saved successfully');
                }

                db.close();
                callback(null, null);
            });
        }
    });
};

module.exports = PlayersDao;