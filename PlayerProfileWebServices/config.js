/**
 * Created by rajaraman on 12/12/14.
 */
var config = {};

// Mongo related initialization stuff
var mongo = require('mongodb');
var monk = require('monk');
var db = monk('localhost:27017/nodetest');


config.getDB = function () {
    return db;
};

module.exports = config;