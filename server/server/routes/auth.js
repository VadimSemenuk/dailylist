let router = require('express-promise-router')();
let config = require("../config");
let passport = require("passport");
let jwt = require("jsonwebtoken");
let bcrypt = require("bcryptjs");
let email = require("../utils/email");
let md5 = require('md5');

module.exports = function (authRep) {
	router.post('/sign-in', async (req, res, next) => {
		passport.authenticate('local', async (err, user) => {
			if (err) {
				res.json({
					errorCode: err.code
				});
			} else {
				let payload = {
					id: user.id
				};
				let token = jwt.sign(payload, config.jwtSecret);

				res.json({
					user: user,
					errorCode: -1,
					token: `Bearer ${token}`
				});
			}
		})(req, res, next);
	});

	router.post('/sign-up', async (req, res, next) => {
		let id = await authRep.createUser({
			...req.body,
			name: req.body.name,
			email: req.body.email,
			password: req.body.password
		});
		let payload = {
			id
		};
		let token = jwt.sign(payload, config.jwtSecret);

		let user = await authRep.getUserByField("id", id);

		res.json({
			errorCode: -1,
			user: user,
			token: `Bearer ${token}`
		});
	});

	return router
};