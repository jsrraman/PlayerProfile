/**
 * Created by rajaraman on 16/12/14.
 */
var debug = require('debug')('PlayerProfileWebServices');

var mongoClient = require('mongodb').MongoClient;
//var monk = require('monk');
var connectionUrl = 'mongodb://localhost:27017/players-profile';

var playersDao = {};

playersDao.saveCountryList = function(docCountryList, callback) {

    mongoClient.connect(connectionUrl, function (err, db) {

        if (err) {
            debug('Could not open database: ' + err );
            callback(err, db);
        } else {
            debug('Database opened successfully');

            var countryCollection = db.collection('countries');

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

module.exports = playersDao;