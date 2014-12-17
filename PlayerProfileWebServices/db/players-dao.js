/**
 * Created by rajaraman on 16/12/14.
 * Interface for accessing players-profile database
 */
var debug = require('debug')('PlayerProfileWebServices');

var playersDao = {};

playersDao.dbName = "players-profile";
playersDao.mongoClient = require('mongodb').MongoClient;
playersDao.connectionUrl = "mongodb://localhost:27017/" + playersDao.dbName;

// Deletes the database
playersDao.deleteDbs = function() {

    this.mongoClient.connect(this.connectionUrl, function (err, db) {

        if (err) {
            debug("Could not open " + this.dbName + " database: " + err );
            callback(err, db);
        } else {
            db.dropDatabase(function (err, result) {

                debug(this.dbName + " database opened successfully");

                if (!err) {
                    debug(this.dbName + " database dropped successfully");
                }

                db.close();
            });
        }
    });
}

// Saves the scraped countries list to the database
playersDao.saveCountryList = function(docCountryList, callback) {

    this.mongoClient.connect(this.connectionUrl, function (err, db) {

        if (err) {
            debug("Could not open " + this.dbName + " database: " + err );
            callback(err, db);
        } else {

            var countryCollection = db.collection('countries');

            // Remove the records from the collection before insert
            countryCollection.remove({}, function (err, result) {
                if (!err) {
                    debug('Country list removed successfully');
                }
            });

            countryCollection.insert(docCountryList, function (err, result) {
                if (!err) {
                    debug('Country list saved successfully');
                }

                db.close();
                callback(err, result);
            });
        }
    });
};

// Get the country list
playersDao.getCountryList = function(callback) {

    this.mongoClient.connect(this.connectionUrl, function (err, db) {

        if (err) {
            debug("Could not open " + this.dbName + " database: " + err );
            callback(err, db);
        } else {

            var countryCollection = db.collection('countries');

            // Get the records without "_id" column
            countryCollection.find({},{_id:0}).toArray(function (err, result) {
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
playersDao.savePlayerList = function(docPlayerList, callback) {

    this.mongoClient.connect(this.connectionUrl, function (err, db) {

        if (err) {
            debug("Could not open " + this.dbName + " database: " + err );
            callback(err, db);
        } else {

            var playerCollection = db.collection('players');

            playerCollection.insert(docPlayerList, function (err, result) {
                if (!err) {
                    debug('Player list saved successfully');
                }

                db.close();
                callback(err, result);
            });
        }
    });
};


module.exports = playersDao;