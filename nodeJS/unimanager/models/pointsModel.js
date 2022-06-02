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

module.exports.getAllRoutes = async function() {
    try {
        let sql ="Select * from routes";
        let result = await pool.query(sql);
        let routes = result.rows;
        return { status:200, result:routes};
    } catch (err) {
        console.log(err);
        return { status:500, result: err};
    }
}

module.exports.getRoutePoints = async function(id) {
    try {
        let sql ="SELECT points.id, points.name, points.description, points.longi, points.lati, audioexp.pathfile FROM routes INNER JOIN pir ON routes.id = pir.routes_id INNER JOIN points  ON pir.points_id = points.id left JOIN pire ON pir.id = pire.pir_id left join experience ON experience.id = pire.experience_id left JOIN audioexp ON audioexp.experience_id = experience.id WHERE routes.id = $1";
        let result = await pool.query(sql,[id]);
        let routes = result.rows;
        return { status:200, result:routes};
    } catch (err) {
        console.log(err);
        return { status:500, result: err};
    }
}