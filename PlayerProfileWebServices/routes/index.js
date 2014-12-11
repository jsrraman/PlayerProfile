var debug = require('debug')('PlayerProfileWebScraper');
//var bodyParser = require('body-parser');
var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res) {
  res.render('index', { title: 'Express' });
});

/* GET users listing. */
router.get('/userlist', function(req, res) {
    var db = req.db;
    var collection = db.get('users');

    debug("Getting the userlist ...");

    collection.find({}, {}, function(err, docs) {
        if (err) {
            res.send("Users list could not be fetched");
        } else {
            res.render('userlist', {
                "userlist": docs
            });
        }
    });
});

/* Show new user page. */
router.get('/adduser', function(req, res) {
    res.render('adduser', {title:'Add New User'});
});


/* ADD new user. */
router.post('/adduser', function(req, res) {

    // Set our db value
    var db = req.db;

    // Get the form values
    var username = req.body.username;
    var useremail = req.body.useremail;

    // Set our collection
    var collection = db.get('users');

    // Submit to DB
    var doc = {"username": username, "email" : useremail};

    collection.insert(doc, function(err, doc) {

        if (err) {
            res.send("There was error in adding the user");
        } else {
            //User added successfully, so redirect to the userlist
            res.location("userlist");
            res.redirect("userlist");
        }
    });
});

module.exports = router;