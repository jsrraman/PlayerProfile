/**
 * Created by rajaraman on 12/12/14.
 */
var should = require('should');
var assert = require('assert');
var request = require('supertest');
//var config = require('../config');

describe('Users Routing', function() {
    var url = 'http://localhost:3000';

    before(function(done) {
        done();
    });

    describe('users', function() {
        it('should return the list of the users in the system successfully', function(done) {

        request(url)
            .get('/users/userlist')
            .end(function(err, res) {
                if (err) {
                    throw err;
                }

                //res.should.have.status(200);
                done();
            });
        });
    });
});