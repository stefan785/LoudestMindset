const express = require('express');
const router = express.Router();
const commentController = require('../controllers/commentController'); 

router.post('/createComment', commentController.addComment);
router.get('/getComments/:shoutId', commentController.getCommentsByShoutId);
router.put('/editComment', commentController.editComment);
router.delete('/deleteComment/:commentId', commentController.deleteComment);

module.exports = router;
