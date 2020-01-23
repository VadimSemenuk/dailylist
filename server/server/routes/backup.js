let router = require('express-promise-router')();
let passport = require('passport');

module.exports = function (backupRep) {
    router.get('/', passport.authenticate('jwt', {session: false}), async (req, res, next) => {
        let backups = await backupRep.getBackups(req.user.id);
        res.send(backups);
    });

    router.get('/:id', passport.authenticate('jwt', {session: false}), async (req, res, next) => {
        let backup = await backupRep.getBackup(req.params.id, req.user.id);
        res.send(backup);
    });

    router.post('/', passport.authenticate('jwt', {session: false}), (req, res, next) => {
        backupRep.saveBackup(req.body, req.user.id);
        res.end(); 
    });

    router.post('/:id', passport.authenticate('jwt', {session: false}), (req, res, next) => {
        backupRep.updateBackup(req.body, req.user.id, req.params.id);
        res.end();
    });

    return router
};