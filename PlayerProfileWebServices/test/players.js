/**
 * Unit test cases related to Players
 */
var should = require('should');
var assert = require('assert');
var request = require('supertest');


describe('Player Profile REST APIs Unit Test Report', function() {
    var url = 'http://localhost:3000';

    it('should return the list of cricket playing countries successfully', function(done) {

        request(url)
            .get('/players/countries')
            .expect(200)
            .end(function(err, res) {
                if (err) {
                    throw err;
                }

                res.body.should.have.property('status', 'success');
                done();
            });
    });

    it('should add a user to the system successfully', function(done) {

        request(url)
            .post('/users/adduser')
            .send({ "username": 'ram', "useremail": 'ram@gmail.com' })
            .expect(302)// 302 status is for redirection. TODO: How to validate after redirecting???
            .end(function(err, res) {
                if (err) {
                    throw err;
                }

                // TODO: How to check the redirection response
                //res.header['location'].should.include('/userlist');
                done();
            });
    });
 });