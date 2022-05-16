var express = require('express');
var router = express.Router();
var userModel = require("../models/pointsModel");

router.post('/login',async function(req, res, next) {
    let email = req.body.email;
    let password = req.body.pass;
    let result = await userModel.loginUser(email,password);
    res.status(result.status).send(result.result);
});

module.exports = router;