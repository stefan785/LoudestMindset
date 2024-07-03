const express = require('express');
const router = express.Router();
const listenController = require('../controllers/listenController');

router.post('/listenToUser', listenController.listenToUser);
router.get('/getListening/:userId', listenController.getListeningByUserId);
router.put('/unlistenToUser', listenController.unlistenToUser);

module.exports = router;
