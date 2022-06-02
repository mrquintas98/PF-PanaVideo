var express = require('express');
var router = express.Router();
var spotModel = require("../models/pointsModel");

router.get('/points', async function(req, res, next) {
    let result = await spotModel.getAllPoints();
    res.status(result.status).send(result.result);
});

router.get('/routes', async function(req, res, next) {
    let result = await spotModel.getAllRoutes();
    res.status(result.status).send(result.result);
});

router.get('/:id/route', async function(req, res, next) {
    let id = req.params.id;
    console.log("Sending unit with id "+id);
    let result = await spotModel.getRoutePoints(id);
    res.status(result.status).send(result.result);
});

module.exports = router;