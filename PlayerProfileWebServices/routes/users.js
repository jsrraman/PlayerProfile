var debug = require('debug')('PlayerProfileWebServices');

var express = require('express');
var router = express.Router();

/* GET users */
router.get('/userlist', function(req, res) {
  var db = req.db;
  var collection = db.get('users');

  collection.find({}, {}, function(err, docs) {

    var resObj = {};

    if (err) {
      resObj.status = "failure";
      resObj.description = "Users list could not be fetched";
    } else {
      //res.json(docs);
      resObj.status = "success";
      resObj.userlist = docs;
    }

    res.send(resObj);
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

    var resObj = {};

    if (err) {
      resObj.status = "failure";
      resObj.description = "There was error in adding the users";

      res.send(resObj);
  } else {
      resObj.status = "success";
      resObj.description = "User added successfully";

      res.location("userlist");
      res.redirect("userlist");
    }
  });
});

module.exports = router;