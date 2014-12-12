var debug = require('debug')('PlayerProfileWebServices');

var express = require('express');
var router = express.Router();

/* GET users */
router.get('/userlist', function(req, res) {
  var db = req.db;
  var collection = db.get('users');

  collection.find({}, {}, function(err, docs) {
    if (err) {
      res.send("Users list could not be fetched");
    } else {
      res.json(docs);
    }
  });
});

/* Show new users page. */
router.get('/adduser', function(req, res) {
  res.render('users/adduser', {title:'Add New User'});
});


/* ADD new users. */
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
      res.send("There was error in adding the users");
    } else {
      //User added successfully, so redirect to the userlist
      res.location("userlist");
      res.redirect("userlist");
    }
  });
});

module.exports = router;