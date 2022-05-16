var pool = require ("./connections");

module.exports.loginUser = async function(email,pass) {
    try {
        let sql ="Select * from user where user_email = $1 and user_password = $2";
        let result = await pool.query(sql,[email,pass]);
        if (result.rows.length > 0)
            return { status:200, result:result.rows[0]};
        else return { status:401, result: {msg: "Wrong email or password"}};
    } catch (err) {
        console.log(err);
        return { status:500, result: err};
    }
}