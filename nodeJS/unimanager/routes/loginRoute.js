var express = require('express');
var router = express.Router();
var loginModel = require("../models/loginModel");

router.post('/login',async function(req, res, next) {
    let email = req.body.email;
    let password = req.body.password;
    let result = await loginModel.loginUser(email,password);
    res.status(result.status).send(result.result);
});

module.exports = router;