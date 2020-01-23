let format = require('pg-format');

module.exports = class {
    constructor(db) {
        this.db = db;
    }

    async getBackups(user) {
        let userSelect = await this.db.query(`
            SELECT id, date
            FROM backups
            WHERE user = $user;
        `, {user: user})
            .catch((err) => {
                console.log(err);
            });

        return userSelect.rows[0];
    }

    getBackup() {

    }

    async saveBackup() {
        let insert = await this.db.query(`
            INSERT INTO backups
            (date, user)
            VALUES($date, $user)
            RETURNING id;
        `, {user, date: new Date()});
    }

    updateBackup() {

    }
};