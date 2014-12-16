/**
 * Unit test cases related to Players
 */
var should = require('should');
var assert = require('assert');
var request = require('supertest');


describe('Player Data Scrape Unit Test Report', function() {
    var url = 'http://localhost:3000';

    // This is going to take a while so set a longer timeout (15 seconds)
    this.timeout(15000);

    it('should store the list of cricket playing countries successfully', function(done) {

    request(url)
        .get('/scrape/players/countries')
        .expect(200)
        .end(function(err, res) {
            if (err) {
                throw err;
            }

            res.body.should.have.property('status', 'success');
            done();
        });
    });
 });