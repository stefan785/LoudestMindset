const express = require('express');
const messageController = require('../controllers/messageController');
const router = express.Router();

router.post('/createMessage', messageController.addMessage);
router.get('/getReceivedMessages/:userId', messageController.getReceivedMessages);
router.get('/getSentMessages/:userId', messageController.getSentMessages);
router.put('/editMessage', messageController.editMessage);
router.delete('/deleteMessage/:messageId', messageController.deleteMessage);

module.exports = router;
