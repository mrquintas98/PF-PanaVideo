var pool = require ("./connections");

module.exports.getAllPoints = async function() {
    try {
        let sql ="Select * from points";
        let result = await pool.query(sql);
        let points = result.rows;
        return { status:200, result:points};
    } catch (err) {
        console.log(err);
        return { status:500, result: err};
    }
}

module.exports.getRoutePoints = async function(id) {
    try {
        let sql ="Select * from points";
        let result = await pool.query(sql);
        let points = result.rows;
        return { status:200, result:points};
    } catch (err) {
        console.log(err);
        return { status:500, result: err};
    }
}