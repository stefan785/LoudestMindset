const express = require('express');
const router = express.Router();
const photoController = require('../controllers/photoController');

router.post('/uploadPhoto/:userId', photoController.uploadPhoto);
router.get('/getPhoto/:id', photoController.getPhoto);
router.get('/getAllPhotos/:userId', photoController.getAllPhotos);
router.delete('/deletePhoto/:photoId', photoController.deletePhoto);

module.exports = router;