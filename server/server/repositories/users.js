module.exports = class {
    constructor(db) {
        this.db = db;
    }

    async getUserByField(fieldName, fieldValue) {
        let userSelect = await this.db.query(`
            SELECT id, email, name, password
            FROM users
            WHERE ${fieldName} = $value;
        `, {value: fieldValue})
            .catch((err) => {
                console.log(err);
            });

        return userSelect.rows[0];
    }

    async createUser(user) {
        let insert = await this.db.query(`
            INSERT INTO users
            (name, password, email)
            VALUES($name, $password, $email)
            RETURNING id;
        `, user);

        return insert.rows[0].id;
    }
};