const LocalStrategy = require('passport-local');
const bcrypt = require("bcryptjs");

module.exports = (usersRep) => (
    new LocalStrategy(
        {
            usernameField: 'email',
            passwordField: 'password',
            session: false
        },
        async (email, password, done) => {
            const user = await usersRep.getUserByField("email", email);
            if (!user) {
                return done({code: 1, message: "User with this email hasn't registered"}, null);
            }
            if (password !== user.password) {
                return done({code: 2, message: 'Invalid password'}, null);
            }
            return done(null, user);
        }
    )
);