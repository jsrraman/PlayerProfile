/**
 * Created by rajaraman on 16/12/14.
 */
var debug = require('debug')('PlayerProfileWebServices');

var mongoClient = require('mongodb').MongoClient;
//var monk = require('monk');
var connectionUrl = 'mongodb://localhost:27017/players-profile';

var playersDao = {};

//playersDao.saveCountryInfo = function(docCountry) {
//    var countryCollection = db.get('countries');
//
//    countryCollection.insert(docCountry, function (err, result) {
//
//        var resObj = {};
//
//        if (err) {
//            resObj.status = "failure";
//            resObj.description = "There was error in storing the country list";
//            resObj.result = result;
//        } else {
//            resObj.status = "success";
//            resObj.description = "User added successfully";
//        }
//
//        debug(resObj);
//
//        return resObj;
//    });
//};

playersDao.saveCountryList = function(docCountryList) {

    mongoClient.connect(connectionUrl, function (err, db) {

        if (err) {
            debug('Could not open database: ' + err );
        }

        debug('Database opened successfully' );

        var countryCollection = db.collection('countries');

        countryCollection.insert(docCountryList, function (err, result) {

            var resObj = {};

            if (err) {
                resObj.status = "failure";
                resObj.description = err;
            } else {
                resObj.status = "success";
                resObj.description = "Country list stored successfully";
            }

            db.close();

            debug(resObj);

            return resObj;
        });
    });
};

module.exports = playersDao;