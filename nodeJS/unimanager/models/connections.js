var pg = require ('pg') ;


//const connectionString = "postgres://rqurnntiaehtiu:0f22fc1088b462dfed8af852dd8802a60375926934fcbbb51e9eb5e435983162@ec2-54-228-32-29.eu-west-1.compute.amazonaws.com:5432/d7am31p2kcc8is"
const connectionString = "postgres://postgres:opfl@localhost:5432/PP"
const Pool = pg.Pool
const pool = new Pool({
    connectionString,
    max: 10,
    /*
    ssl: {
        require: true,
        rejectUnauthorized: false
     }
     */
    })
    
    module.exports = pool;